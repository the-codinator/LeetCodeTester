package org.codi.lct.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Marker annotation on a method which returns test cases.
 *
 * Annotated method must:
 * - be {@code public}
 * - be {@code static}
 * - take no arguments
 * - return a list of test cases {@code List&lt;LCTestCase&gt;}
 */
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface LCTestCaseGenerator {

}
