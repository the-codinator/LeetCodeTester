package org.codi.lct.example;

import java.util.Arrays;
import org.codi.lct.annotation.LCSolution;
import org.codi.lct.junit.LCTester;

public class ASimpleFirstExample extends LCTester {

    @LCSolution
    public int sum(int[] nums) {
        return Arrays.stream(nums).sum();
    }
}
