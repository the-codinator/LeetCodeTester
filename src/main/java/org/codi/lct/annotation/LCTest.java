package org.codi.lct.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import org.codi.lct.core.LCExtension;
import org.codi.lct.impl.AutoCustomTestCaseProvider;
import org.codi.lct.impl.AutoDataFileTestCaseProvider;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.TestTemplate;
import org.junit.jupiter.api.extension.ExtendWith;

@TestTemplate
@ExtendWith(LCExtension.class)
@ExtendWith(AutoDataFileTestCaseProvider.class)
@ExtendWith(AutoCustomTestCaseProvider.class)
@DisplayName("Auto LC Test Runner")
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface LCTest {

}
