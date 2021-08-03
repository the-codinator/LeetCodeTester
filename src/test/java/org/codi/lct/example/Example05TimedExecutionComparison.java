package org.codi.lct.example;

import org.codi.lct.annotation.LCSolution;
import org.codi.lct.annotation.settings.LCTrackExecutionTime;
import org.codi.lct.core.LCTestCase;
import org.codi.lct.core.LCTester;

/**
 * This test demonstrates how you can define custom test cases from code, rather than using the data file
 *
 * Note: all {@code @LCSolution} methods are run on the same test instance
 */
@LCTrackExecutionTime
public class Example05TimedExecutionComparison extends LCTester {

    // We can also return a single test case instead of a list
    public static LCTestCase testCases() {
        return new LCTestCase(1_000_000_001, 1_000_000_000, 1);
    }

    @LCSolution
    public int sum1(int a, int b) throws InterruptedException {
        Thread.sleep(100);
        return a + b;
    }

    @LCSolution
    public int sum2(int a, int b) throws InterruptedException {
        while (a > 0) {
            b++;
            a--;
        }
        return b;
    }
}
