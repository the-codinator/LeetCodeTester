package org.codi.lct.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import org.codi.lct.impl.Constants;

/**
 * Marker annotation on a method which provides a means to perform any kind of post-transformations on the returned
 * value of the solution method.
 *
 * Annotated method must:
 *
 * <ul>
 * <li> be {@code public} </li>
 * <li> be {@code static} </li>
 * <li> take exactly 1 argument which must be assignable from the return type of the solution methods </li>
 * <li> have return type of the final comparable type </li>
 * </ul>
 *
 * Tip: The return type can be as simple as boolean if the correctness check is too complex / manually performed.
 *
 * Note: If a method names "transform" exists and follows the above signature rules, it is automatically picked up,
 * irrespective of the presence of this annotation. See {@link Constants#DEFAULT_OUTPUT_TRANSFORMATION_METHOD_NAME}
 */
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface LCOutputTransformation {
    // TODO: feature
}
