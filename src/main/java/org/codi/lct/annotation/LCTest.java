package org.codi.lct.annotation;

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
public @interface LCTest {

}
