package org.codi.lct.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * LeetCode also has question where you need to design & implement a Data Structure
 *
 * This marker annotation indicates that the test should be run as a data structure.
 * The input would contain a list of function call invocations (function name + parameters).
 * The output would be corresponding function call responses.
 *
 * This annotation is incompatible with {@link LCSolution}
 *
 * TODO: feature, consider merging with @LCSolution
 */
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
public @interface LCDataStructureTest {

    /**
     * The class implementing the Data Structure.
     * It must match the Class name in the test case input so that we know when the constructor is being called.
     * Default value is {@code Void.class} which indicates usage of the annotated class itself.
     *
     * Note: the primary test class MUST have a default constructor for instantiation, hence DataStructure classes with
     * custom constructors MUST be defined as static nested class.
     */
    Class<?> value() default Void.class;
}
