package org.codi.lct.junit;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import org.codi.lct.annotation.LCSolution;

/**
 * Fields annotated with LCInput are considered inputs to the {@link LCSolution} program.
 */
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface LCInput {

}
