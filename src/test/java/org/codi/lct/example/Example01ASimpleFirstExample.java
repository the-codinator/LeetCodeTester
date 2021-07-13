package org.codi.lct.example;

import java.util.Arrays;
import org.codi.lct.annotation.LCSolution;
import org.codi.lct.core.LCExecutor;
import org.codi.lct.junit.LCTester;
import org.junit.jupiter.api.Test;

/**
 * A very simple first example.
 *
 * Test cases are loaded automatically from the TestCase data file at {@code
 * resources/lct/org/codi/lct/example/Example01ASimpleFirstExample}
 *
 * {@link LCSolution} annotation is not required since we have a single public non-static method
 */
public class Example01ASimpleFirstExample extends LCTester {

    public Example01ASimpleFirstExample(){
        System.out.println("LIFECYCLE: Test Instance " + this);
    }

    @Test
    public void x(LCExecutor executor) {
        System.out.println(123);
    }

    public int sum(int[] nums) {
        return Arrays.stream(nums).sum();
    }
}
