package org.codi.lct.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Marker annotation on a method which provides a means to perform any kind of post-transformations on the returned
 * value of the solution method
 *
 * Annotated method must:
 * - be {@code public}
 * - be {@code static}
 * - take exactly 1 argument which must be assignable from the return type of the solution methods
 * - have return type of the final comparable type
 *
 * Note: The return type can be as simple as boolean if the correctness check is too complex / manually performed
 */
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface LCOutputTransformation {
    // TODO: feature
}
