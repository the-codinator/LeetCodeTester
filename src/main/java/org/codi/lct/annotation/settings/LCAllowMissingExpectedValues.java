package org.codi.lct.annotation.settings;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Whether we are ok to have missing expected values for some of the scenarios.
 *
 * For test cases where the value is missing, the result is logged instead.
 * 'n' expected values will be used to match against the first 'n' test cases
 */
@Target({ElementType.METHOD, ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
public @interface LCAllowMissingExpectedValues {

    boolean value() default true;
}
