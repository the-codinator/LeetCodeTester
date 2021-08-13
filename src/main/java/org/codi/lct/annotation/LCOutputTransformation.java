package org.codi.lct.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Marker annotation on a method which provides a means to perform any kind of post-transformations on the returned
 * value of the solution method.
 *
 * Annotated method must:
 *
 * <ul>
 *   <li> be {@code public} </li>
 *   <li> be {@code static} </li>
 *   <li> have return type of the final comparable type </li>
 *   <li> have 1st argument which must match the return type of all solution methods </li>
 *   <li> either:
 *     <ul>
 *       <li> have no more arguments </li>
 *       <li> have exact arguments matching the solution method (this is provided as a means for manually validating the
 *       result) </li>
 *     </ul>
 *   </li>
 * </ul>
 *
 * Tip: The return type can be as simple as boolean if the correctness check is too complex / manually performed.
 *
 * Note: If a method names "lcTransform" exists and follows the above signature rules, it is automatically picked up,
 * irrespective of the presence of this annotation. See {@link #AUTO_DISCOVERY_METHOD_NAME}
 */
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface LCOutputTransformation {

    String AUTO_DISCOVERY_METHOD_NAME = "lcTransform";
}
