package org.codi.lct.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Configuration for LCTester
 */
@Target({ElementType.METHOD, ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
public @interface LCConfig {

    /**
     * Whether annotated member's tests should be executed
     */
    boolean enabled() default true;

    /**
     * Whether metrics around execution time should be captured
     */
    boolean timed() default false;

    /**
     * List of files to read testcases from. Paths are relative to classpath. Defaults to a single file under the "lct"
     * directory under classpath resources with the same path as the fully qualified class name
     */
    String[] files() default {};

    /**
     * Whether the tester should stop / crash after the first test failure
     */
    boolean crash() default false; // TODO

    /**
     * Milliseconds for Time Limit Exceeded check
     * 0 indicates check is ignored
     */
    int tle() default 1000; // TODO
}
