package org.codi.lct.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Marker annotation on a method which returns test cases.
 *
 * Annotated method must:
 *
 * <ul>
 *   <li> be {@code public} </li>
 *   <li> be {@code static} </li>
 *   <li> take no arguments </li>
 *   <li> return either:
 *     <ul>
 *       <li> a single test case {@code LCTestCase} </li>
 *       <li> a list of test cases {@code List&lt;LCTestCase&gt;} </li>
 *     </ul>
 *   </li>
 * </ul>
 *
 * Note: If a method named "lcTestCases" with the exact signature {@code public static List<LCTestCase> lcTestCases()}
 * OR {@code public static LCTestCase lcTestCases()} exists, it is automatically picked up, irrespective of the presence
 * of this annotation. See {@link #AUTO_DISCOVERY_METHOD_NAME}
 */
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface LCTestCaseGenerator {

    String AUTO_DISCOVERY_METHOD_NAME = "lcTestCases";
}
