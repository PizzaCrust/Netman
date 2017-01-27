package online.pizzacrust.netman.api;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;

/**
 * Represents a serializer for bytes from object data.
 *
 * @param <OBJECT_TYPE> Represents the object type that will be serialized.
 *
 * @since 1.0-SNAPSHOT
 * @author PizzaCrust
 */
public interface ByteSerializer<OBJECT_TYPE> {

    /**
     * Serializes the specified {@link OBJECT_TYPE} into bytes in the given
     * {@link DataOutputStream}.
     *
     * @param object
     * @param byteBuf
     */
    void serialize(OBJECT_TYPE object, DataOutputStream byteBuf);

    /**
     * Deserializes the specified {@link DataInputStream} into {@link OBJECT_TYPE}.
     * @param byteBuf
     * @param objectClass
     * @return
     */
    OBJECT_TYPE deserialize(DataInputStream byteBuf, Class<?> objectClass);

}