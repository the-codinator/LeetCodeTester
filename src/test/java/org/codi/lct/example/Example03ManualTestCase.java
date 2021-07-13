package org.codi.lct.example;

import java.util.Arrays;
import java.util.List;
import org.codi.lct.annotation.LCSolution;
import org.codi.lct.core.LCExecutor;
import org.codi.lct.core.LCTestCase;
import org.codi.lct.junit.LCExtension;
import org.codi.lct.junit.LCTester;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

/**
 * Here we use the {@link LCExtension} instead of extending {@link LCTester}
 *
 * We define one or more {@link Test} annotated methods to run our teams. These methods can accept a parameter of type
 * {@link LCExecutor} which executes each {@link LCTestCase}
 *
 * Note: the test instance is shared within a single @Test method, so if you have state, you might want to be careful.
 * To avoid sharing the test instance, create multiple @Test methods instead
 */
@ExtendWith(LCExtension.class)
public class Example03ManualTestCase {

    @Test
    @DisplayName("My manual test runner")
    public void myManualTest(LCExecutor executor) {
        executor.executeTestCase(LCTestCase.builder().input(List.of(new int[]{1, 2, 3, 4})).expected(10).build());
        // Additional tests ...
    }

    @LCSolution
    public int sum(int[] nums) {
        return Arrays.stream(nums).sum();
    }
}
