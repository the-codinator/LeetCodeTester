package org.codi.lct.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface LCSolution {

    /**
     * Whether execution time stats should be logged for the Solution or not
     */
    boolean timed() default false;
}
