package org.codi.lct.core;

import org.codi.lct.impl.AutoCustomTestCaseProvider;
import org.codi.lct.impl.AutoDataFileTestCaseProvider;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.TestTemplate;
import org.junit.jupiter.api.extension.ExtendWith;

/**
 * Provides a simple base class that allows running testcases in a data driven fashion.
 * Essentially, reads testcases from the file and uses the {@link LCExtension} to run the testcases
 */
@ExtendWith(LCExtension.class)
public abstract class LCTester {

    @TestTemplate
    @ExtendWith(AutoDataFileTestCaseProvider.class)
    @ExtendWith(AutoCustomTestCaseProvider.class)
    @DisplayName("Auto LC Test Runner")
    final void autoTestLC(LCTestCase testCase, LCExecutor executor) {
        executor.executeTestCase(testCase);
    }
}
