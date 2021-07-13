package org.codi.lct.annotation.settings;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Provide a timeout for executing a test.
 *
 * You can configure the threshold value in milliseconds. Default is 1 second.
 *
 * Exceeding the timeout duration will trigger LeetCode's infamous TLE. A exception would be thrown for the test for
 * Time Limit Exceeded.
 */
@Target({ElementType.METHOD, ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
public @interface LCExecutionTimeLimit {

    int value() default 1000;
}
