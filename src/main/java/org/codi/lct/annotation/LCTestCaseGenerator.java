package org.codi.lct.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import org.codi.lct.impl.Constants;

/**
 * Marker annotation on a method which returns test cases.
 *
 * Annotated method must:
 *
 * <ul>
 * <li> be {@code public} </li>
 * <li> be {@code static} </li>
 * <li> take no arguments </li>
 * <li> return a single test case {@code LCTestCase} OR a list of test cases {@code List&lt;LCTestCase&gt;} </li>
 * </ul>
 *
 * Note: If a method name "testCases" with the exact signature {@code public static List<LCTestCase> testCases()} OR
 * {@code public static LCTestCase testCases()} exists, it is automatically picked up, irrespective of the presence of
 * this annotation. See {@link Constants#DEFAULT_TEST_CASE_GENERATOR_METHOD_NAME}
 */
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface LCTestCaseGenerator {

}
