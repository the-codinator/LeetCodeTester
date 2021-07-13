package org.codi.lct.junit;

import lombok.experimental.ExtensionMethod;
import org.codi.lct.core.LCExecutor;
import org.codi.lct.core.LCTestCase;
import org.codi.lct.impl.Util;
import org.codi.lct.impl.junit.AutoTestCaseProvider;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.TestTemplate;
import org.junit.jupiter.api.extension.ExtendWith;

/**
 * Provides a simple base class that allows running testcases in a data driven fashion.
 * Essentially, reads testcases from the file and uses the {@link LCExtension} to run the testcases
 */
@ExtendWith(LCExtension.class)
@ExtensionMethod(Util.class)
public abstract class LCTester {

    @TestTemplate
    @ExtendWith(AutoTestCaseProvider.class)
    @DisplayName("Auto LC Test Runner")
    final void autoTestLC(LCTestCase testCase, LCExecutor executor) {
        executor.executeTestCase(testCase);
    }
}
