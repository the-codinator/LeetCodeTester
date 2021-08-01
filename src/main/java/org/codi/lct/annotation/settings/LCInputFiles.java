package org.codi.lct.annotation.settings;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Annotate class / method to include custom files to read testcases from.
 * Files paths are relative to program classpath.
 *
 * Note: using this annotation OVERRIDES (not append) the previous file list
 */
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
public @interface LCInputFiles {

    /**
     * Empty string represents the default file path. To include the default path in a custom list, include {@code ""} in
     * the file list.
     *
     * Default path is the "lct" directory under classpath resources with the same name or path as the fully qualified
     * class name
     *
     * For Example: If test class is {@code org.codi.example.MyTestClass} then default path of the data file is either
     * {@code lct/org.codi.example.MyTestClass.txt} or {@code lct/org/codi/example/MyTestClass.txt} (Former takes
     * precedence).
     */
    String[] value() default "";
}
