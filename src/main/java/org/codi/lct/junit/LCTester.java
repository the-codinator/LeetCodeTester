package org.codi.lct.junit;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import lombok.experimental.ExtensionMethod;
import org.codi.lct.core.LCExecutor;
import org.codi.lct.core.LCTestCase;
import org.codi.lct.data.LCExecutable;
import org.codi.lct.impl.Util;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.DynamicContainer;
import org.junit.jupiter.api.DynamicTest;
import org.junit.jupiter.api.TestFactory;
import org.junit.jupiter.api.extension.ExtendWith;

/**
 * Provides a simple base class that allows running testcases in a data driven fashion.
 * Essentially, reads testcases from the file and uses the {@link LCExtension} to run the testcases
 */
@ExtendWith(LCExtension.class)
@ExtensionMethod(Util.class)
public abstract class LCTester {

    /**
     * The driver function for executing test cases
     *
     * @param executor test case runner
     */
    @TestFactory
    @DisplayName("LC Auto Test Runner")
    public final List<DynamicContainer> test(LCExecutor executor) {
        return new LinkedList<>().addIfPresent(generateTestSuite("Custom Test Cases", executor, customTestCases()))
            .addIfPresent(generateTestSuite("Data File Test Cases", executor, dataFileTestCases()))
            .throwIfEmpty("No tests found!");
    }

    /**
     * Generate test cases from the data file
     */
    private List<LCTestCase> dataFileTestCases() {
        // TODO
        return List.of(new LCTestCase(List.of(), null),new LCTestCase(List.of(), null));
    }

    /**
     * Override this method to provide custom / hand written test cases
     */
    protected List<LCTestCase> customTestCases() {
        return null;
    }

    private DynamicContainer generateTestSuite(String name, LCExecutor executor, List<LCTestCase> testCases) {
        if (testCases == null || testCases.isEmpty()) {
            return null;
        }
        List<DynamicTest> tests = new ArrayList<>();
        int testNumber = 1;
        for (LCTestCase testCase : testCases) {
            tests.add(DynamicTest.dynamicTest("Test Case " + testNumber++, new LCExecutable(executor, testCase)));
        }
        return DynamicContainer.dynamicContainer(name, tests);
    }
}
