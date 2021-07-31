package org.codi.lct.example;

import java.util.List;
import org.codi.lct.annotation.LCTestCaseGenerator;
import org.codi.lct.core.LCTester;

/**
 * This test demonstrates how you can define custom test cases from code, rather than using the data file
 *
 * TODO: Note: each test is run on a separate test instance
 */
public class Example02CustomTestCase extends LCTester {

    @LCTestCaseGenerator
    public static List<org.codi.lct.core.LCTestCase> customTestCases() {
        return List.of( // Different approaches for creating testcases
            org.codi.lct.core.LCTestCase.builder().input(5).input(10).expected(15).build(), // providing one input at a time to builder
            org.codi.lct.core.LCTestCase.builder().inputs(List.of(2, 3)).expected(5).build(), // using list input builder
            new org.codi.lct.core.LCTestCase(11, 5, 6), // using varargs input constructor
            new org.codi.lct.core.LCTestCase(11, List.of(5, 6)) // using list input constructor
        );
    }

    public int sum(int a, int b) {
        return a + b;
    }
}
