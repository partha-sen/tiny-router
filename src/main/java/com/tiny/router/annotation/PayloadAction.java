package com.tiny.router.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * This annotation is used to denote target of message destination.
 * MessageRouter class this to define destination.
 */

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
public @interface PayloadAction {

    /**
     * @return value
     */
    String value() default "";
}
