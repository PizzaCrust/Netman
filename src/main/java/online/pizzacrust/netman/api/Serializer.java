package online.pizzacrust.netman.api;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Defines the annotated element as a node that uses a custom serializer.
 *
 * @since 1.0-SNAPSHOT
 * @author PizzaCrust
 */
@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
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
