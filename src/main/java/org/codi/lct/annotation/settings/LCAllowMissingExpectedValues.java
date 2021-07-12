package org.codi.lct.annotation.settings;

/**
 * Whether we are ok to have missing expected values for some of the scenarios.
 *
 * For test cases where the value is missing, the result is logged instead.
 * 'n' expected values will be used to match against the first 'n' test cases
 */
public @interface LCAllowMissingExpectedValues {

    boolean value() default true;
}
