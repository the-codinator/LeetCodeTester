package org.codi.lct.example;

import java.util.Arrays;
import org.codi.lct.annotation.LCSolution;
import org.codi.lct.core.LCTester;

/**
 * A very simple first example.
 *
 * Test cases are loaded automatically from the TestCase data file at {@code
 * resources/lct/org/codi/lct/example/Example01ASimpleFirstExample}
 *
 * {@link LCSolution} annotation is not required since we have a single public non-static method
 */
public class Example01ASimpleFirstExample extends LCTester {

    // @LCSolution -> Optional annotation since we have a single public non-static method
    public int sum(int[] nums) {
        return Arrays.stream(nums).sum();
    }
}
