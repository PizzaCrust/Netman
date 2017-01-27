package online.pizzacrust.netman.api;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

import javafx.util.Pair;

/**
 * Serializes a class into a series of bytes.
 * The default implementation to convert classes into bytes.
 * This serializer requires a instance of a class.
 *
 * @since 1.0-SNAPSHOT
 * @author PizzaCrust
 */
public class ClassSerializer implements ByteSerializer<Object> {

    public static final Class<?>[] SUPPORTED_DATA_TYPES = new Class[] {
            Integer.class, String.class, Float.class, Double.class, Long.class, Short.class
    };

    private boolean isTypeSupported(Class<?> type) {
        for (Class<?> dataType : SUPPORTED_DATA_TYPES) {
            if (dataType == type) {
                return true;
            }
        }
        return false;
    }

    private boolean hasSerializer(Field field) {
        if (!isTypeSupported(field.getType())) {
            Optional<SerializerMetadata> metadata = getSerializerMetadata(field);
            return metadata.map(serializerMetadata -> true).orElse(false);
        }
        return true;
    }

    private boolean isFieldSupported(Field field) {
        return isTypeSupported(field.getType()) && field.isAccessible();
    }

    private Optional<SerializerMetadata> getSerializerMetadata(Field field) {
        if (!field.isAnnotationPresent(SerializerMetadata.class)) {
            return Optional.empty();
        }
        return Optional.of(field.getAnnotation(SerializerMetadata.class));
    }

    private void write(Object object, DataOutputStream
            byteArrayOutputStream) {
        for (Method method : byteArrayOutputStream.getClass().getDeclaredMethods()) {
            if (method.getParameterTypes().length >= 1) {
                if (method.getParameterTypes()[0] == object.getClass()) {
                    try {
                        method.invoke(byteArrayOutputStream, object);
                    } catch (IllegalAccessException e) {
                        e.printStackTrace();
                    } catch (InvocationTargetException e) {
                        e.printStackTrace();
                    }
                }
            }
        }
    }

    private Object read(Field field, DataInputStream dataInputStream) {
        for (Method method : dataInputStream.getClass().getDeclaredMethods()) {
            if (method.getReturnType() == field.getType()) {
                try {
                    return method.invoke(dataInputStream);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
        return null;
    }

    private Optional<ByteSerializer> newSerializer(Class<?> serializerClass) {
        try {
            return Optional.of((ByteSerializer) serializerClass.newInstance());
        } catch (Exception e) {
            e.printStackTrace();
        }
        return Optional.empty();
    }

    @Override
    public void serialize(Object object, DataOutputStream byteBuf) {
        Class<?> theClassOfObject = object.getClass();
        for (Field field : theClassOfObject.getDeclaredFields()) {
            if (isFieldSupported(field) && hasSerializer(field)) {
                Optional<SerializerMetadata> metadata = getSerializerMetadata(field);
                Optional<Class<?>> customSerializer = Optional.empty();
                if (metadata.isPresent()) {
                    SerializerMetadata serializerMetadata = metadata.get();
                    if (!serializerMetadata.serialize()) {
                        continue;
                    }
                    customSerializer = Optional.of(serializerMetadata.serializer());
                }
                if (!customSerializer.isPresent()) {
                    try {
                        write(field.get(object), byteBuf);
                    } catch (Exception e) {
                        e.printStackTrace();
                        throw new RuntimeException(e);
                    }
                    continue;
                }
                Optional<ByteSerializer> serializer = newSerializer(customSerializer.get());
                serializer.ifPresent(byteSerializer -> {
                    try {
                        byteSerializer.serialize(field.get(object), byteBuf);
                    } catch (IllegalAccessException e) {
                        e.printStackTrace();
                    }
                });
            }
        }
        try {
            byteBuf.close();
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        }
    }

    private Optional<Object> newInstance(Class<?> theClass) {
        try {
            return Optional.of(theClass.newInstance());
        } catch (Exception e) {
            e.printStackTrace();
        }
        return Optional.empty();
    }

    @Override
    public Object deserialize(DataInputStream byteBuf, Class<?> objectClass) {
        Optional<Object> objectOptional = newInstance(objectClass);
        if (objectOptional.isPresent()) {
            Map<Field, Object> fieldToObject = new HashMap<>();
            for (Field field : objectClass.getDeclaredFields()) {
                if (isFieldSupported(field) && hasSerializer(field)) {
                    Optional<SerializerMetadata> metadata = getSerializerMetadata(field);
                    Optional<Class<?>> customSerializer = Optional.empty();
                    if (metadata.isPresent()) {
                        SerializerMetadata serializerMetadata = metadata.get();
                        if (!serializerMetadata.serialize()) {
                            continue;
                        }
                        customSerializer = Optional.of(serializerMetadata.serializer());
                    }
                    if (!customSerializer.isPresent()) {
                        fieldToObject.put(field, read(field, byteBuf));
                        continue;
                    }
                    Optional<ByteSerializer> serializer = newSerializer(customSerializer.get());
                    serializer.ifPresent(byteSerializer -> {
                        Object object = byteSerializer.deserialize(byteBuf,
                                field.getType());
                        fieldToObject.put(field, object);
                    });
                }
            }
            Object object = objectOptional.get();
            for (Map.Entry<Field, Object> entry : fieldToObject.entrySet()) {
                try {
                    entry.getKey().set(object, entry.getValue());
                } catch (IllegalAccessException e) {
                    e.printStackTrace();
                }
            }
            try {
                byteBuf.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
            return object;
        }
        return null;
    }

    static class ExampleClass {
        public int test = 154243124;
        @SerializerMetadata(serializer = CustomSerializer.class)
        public Pair<Integer, Integer> meow = new Pair<>(1,2);
        @SerializerMetadata(serialize = false)
        public String meow2 = "meow";
    }

    class CustomSerializer implements ByteSerializer<Pair<Integer, Integer>> {

        @Override
        public void serialize(Pair<Integer, Integer> object, DataOutputStream byteBuf) {
            try {
                byteBuf.writeInt(object.getKey());
                byteBuf.writeInt(object.getValue());
            } catch (Exception e) {
                e.printStackTrace();
                throw new RuntimeException(e);
            }
        }

        @Override
        public Pair<Integer, Integer> deserialize(DataInputStream byteBuf, Class<?>
                objectClass) {
            try {
                int a = byteBuf.readInt();
                int b = byteBuf.readInt();
                return new Pair<>(a, b);
            } catch (Exception e) {
                e.printStackTrace();
                throw new RuntimeException(e);
            }
        }
    }

    public static void main(String... args) throws Exception {
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        ClassSerializer classSerializer = new ClassSerializer();
        ExampleClass exampleClass1 = new ExampleClass();
        exampleClass1.meow2 = "lol";
        classSerializer.serialize(exampleClass1, new DataOutputStream(byteArrayOutputStream));
        ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream
                (byteArrayOutputStream.toByteArray());
        ExampleClass exampleClass = (ExampleClass) classSerializer.deserialize(new DataInputStream
                (byteArrayInputStream), ExampleClass.class);
        System.out.println(exampleClass == null);
        System.out.println(exampleClass.meow.getKey() + " " + exampleClass.meow.getValue());
        System.out.println(exampleClass.test);
        System.out.println(exampleClass.meow2);
    }

}
