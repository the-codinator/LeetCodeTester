package org.codi.lct.example;

import java.util.List;
import java.util.stream.Collectors;
import org.codi.lct.annotation.LCSolution;
import org.codi.lct.core.tester.LCExecutor;
import org.codi.lct.core.tester.LCExtension;
import org.codi.lct.core.tester.LCTestCase;
import org.codi.lct.core.tester.LCTester;
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
        executor.executeTestCase(LCTestCase.builder().input(List.of(1, 2, 3)).expected(List.of(1, 4, 9)).build());
        // Additional tests ...
    }

    @LCSolution
    public List<Integer> squares(List<Integer> nums) {
        return nums.stream().map(x -> x * x).collect(Collectors.toList());
    }
}
