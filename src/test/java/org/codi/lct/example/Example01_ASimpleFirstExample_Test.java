package org.codi.lct.example;

import java.util.Arrays;
import org.codi.lct.annotation.LCSolution;
import org.codi.lct.core.tester.LCTester;

/**
 * A very simple first example.
 *
 * Test cases are loaded automatically from the TestCase data file at {@code
 * resources/lct/org/codi/lct/example/Example01ASimpleFirstExample}
 */
public class Example01_ASimpleFirstExample_Test extends LCTester {

    @LCSolution
    public int sum(int[] nums) {
        return Arrays.stream(nums).sum();
    }
}
