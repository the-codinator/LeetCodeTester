package org.codi.lct.example;

import java.util.List;
import org.codi.lct.annotation.LCSolution;
import org.codi.lct.annotation.LCTestCaseGenerator;
import org.codi.lct.core.LCTestCase;
import org.codi.lct.core.LCTester;

/**
 * This test demonstrates how you can define custom test cases from code, rather than using the data file
 *
 * Note: each test case is run on a separate test instance
 */
public class Example02CustomTestCase extends LCTester {

    /**
     * {@link LCTestCaseGenerator} annotation is not required since the signature matches {@code public static
     * List<LCTestCase> testCases()}
     */
    // @LCTestCaseGenerator
    public static List<LCTestCase> testCases() {
        return List.of( // Different approaches for creating testcases
            LCTestCase.builder().input(5).input(10).expected(15).build(), // providing one input at a time to builder
            LCTestCase.builder().inputs(List.of(2, 3)).expected(5).build(), // using list input builder
            new LCTestCase(11, 5, 6), // using varargs input constructor
            new LCTestCase(11, List.of(5, 6)), // using list input constructor
            LCTestCase.parse("333", "111 222"), // parsing from string (similar to data file)
            LCTestCase.parse("111 222 333") // parsing from single string (note: inputs before expected here)
        );
    }

    /**
     * {@link LCSolution} annotation is not required since we have a single public non-static method
     */
    // @LCSolution
    public int sum(int a, int b) {
        return a + b;
    }
}
