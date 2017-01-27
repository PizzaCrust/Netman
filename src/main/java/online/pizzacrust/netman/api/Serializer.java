package online.pizzacrust.netman.api;

/**
 * Defines the annotated element as a node that uses a custom serializer.
 *
 * @since 1.0-SNAPSHOT
 * @author PizzaCrust
 */
public @interface Serializer {

    /**
     * Retrieves if it will serialize the specified element.
     * @return
     */
    boolean serialize() default true;

    /**
     * Defines the serializer class of the field.
     * @return
     */
    Class<? extends ByteSerializer> serializer();

}
