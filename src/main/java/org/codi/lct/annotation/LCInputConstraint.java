package org.codi.lct.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * A set of validations that can be applied on the input to handle constraints
 *
 * Note: If a method named "lcConstraint" exists and follows the above signature rules, it is automatically picked up,
 * irrespective of the presence of this annotation. See {@link #AUTO_DISCOVERY_METHOD_NAME}
 */
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface LCInputConstraint {

    String AUTO_DISCOVERY_METHOD_NAME = "lcConstraint";

    // TODO: feature
}
