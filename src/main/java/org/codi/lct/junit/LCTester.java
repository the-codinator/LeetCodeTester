package org.codi.lct.junit;

import java.util.ArrayList;
import java.util.List;
import lombok.experimental.ExtensionMethod;
import org.codi.lct.core.LCExecutor;
import org.codi.lct.core.LCTestCase;
import org.codi.lct.data.LCExecutable;
import org.codi.lct.impl.Util;
import org.codi.lct.impl.junit.AutoTestCaseProvider;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.DynamicContainer;
import org.junit.jupiter.api.DynamicTest;
import org.junit.jupiter.api.TestTemplate;
import org.junit.jupiter.api.condition.EnabledIf;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;

/**
 * Provides a simple base class that allows running testcases in a data driven fashion.
 * Essentially, reads testcases from the file and uses the {@link LCExtension} to run the testcases
 */
@ExtendWith(LCExtension.class)
@ExtensionMethod(Util.class)
public abstract class LCTester {

    private static final LCTestCase dummy = new LCTestCase(null, List.of());

    @TestTemplate
    @ExtendWith(AutoTestCaseProvider.class)

    @EnabledIf(value = "func", disabledReason = "No data files found!")
    @DisplayName("Data File Tests")
    @ParameterizedTest(name = "Test Case #{index}")
    @MethodSource("dataFileTestCases")
    void dataFileTests(LCTestCase testCase, LCExecutor executor) {
        System.out.println("LIFECYCLE: Test Case " + this);
    }

    private static boolean func() {
        return true;
    }

    // /**
    //  * The driver function for executing test cases
    //  *
    //  * @param executor test case runner
    //  */
    // @TestFactory
    // @DisplayName("LC Auto Test Runner")
    // final List<DynamicContainer> autotestLC(LCExecutor executor) {
    //     return new LinkedList<>().addIfPresent(generateTestSuite("Custom Test Cases", executor, customTestCases()))
    //         .addIfPresent(generateTestSuite("Data File Test Cases", executor, dataFileTestCases()))
    //         .throwIfEmpty("No tests found!");
    // }

    /**
     * Generate test cases from the data file
     */
    private static List<LCTestCase> dataFileTestCases() {
        // TODO: impl
        return List.of(dummy, dummy);
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
            tests.add(DynamicTest.dynamicTest("Test Case #" + testNumber++, new LCExecutable(executor, testCase)));
        }
        return DynamicContainer.dynamicContainer(name, tests);
    }
}
