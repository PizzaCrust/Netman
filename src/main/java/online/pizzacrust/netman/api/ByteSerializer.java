package online.pizzacrust.netman.api;

import java.io.ByteArrayOutputStream;

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
     * {@link ByteArrayOutputStream}.
     *
     * @param object
     * @param byteBuf
     */
    void serialize(OBJECT_TYPE object, ByteArrayOutputStream byteBuf);

}