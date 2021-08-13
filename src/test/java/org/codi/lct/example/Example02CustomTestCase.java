package org.codi.lct.example;

import java.util.List;
import org.codi.lct.annotation.LCOutputTransformation;
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
     * List<LCTestCase> lcTestCases()}. Return type can also be {@code LCTestCase} instead of a list.
     */
    // @LCTestCaseGenerator
    public static List<LCTestCase> lcTestCases() {
        return List.of( // Different approaches for creating testcases
            LCTestCase.builder().input(5).input(10).expected(510).build(), // providing one input at a time to builder
            LCTestCase.builder().inputs(List.of(2, 3)).expected(23).build(), // using list input builder
            new LCTestCase(56, 5, 6), // using varargs input constructor
            new LCTestCase(56, List.of(5, 6)), // using list input constructor
            LCTestCase.parse("111222", "111 222"), // parsing from string (similar to data file)
            LCTestCase.parse("111 222 111222") // parsing from single string (note: inputs before expected here)
        );
    }

    /**
     * {@link LCOutputTransformation} annotation is not required since the signature matches {@code public static
     * Object lcTransform(int x)}. Return type should match type of expected value in test case. Parameters should be
     * the return type of the solution method, and optionally the inputs of the solution method.
     */
    // @LCOutputTransformation
    public static int lcTransform(String s) {
        return Integer.parseInt(s);
    }

    /**
     * {@link LCSolution} annotation is not required since we have a single public non-static method
     */
    // @LCSolution
    public String concat(int a, int b) {
        return "" + a + b;
    }
}
