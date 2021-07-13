package org.codi.lct.annotation.settings;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Annotate class / method to customize the list of files to read testcases from.
 * Paths are relative to classpath. Defaults to a single file under the "lct" directory under classpath resources with
 * the same path as the fully qualified class name after replacing '.' with '/'
 *
 * Note: using this annotation OVERRIDES (not append) the previous file list
 */
@Target({ElementType.METHOD, ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
public @interface LCInputFiles {

    String[] value();
}
