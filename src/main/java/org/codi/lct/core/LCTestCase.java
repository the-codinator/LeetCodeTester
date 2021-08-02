package org.codi.lct.core;

import java.util.Collections;
import java.util.List;
import lombok.Builder;
import lombok.Singular;
import lombok.Value;

/**
 * Defines a test case based on expected output & provided inputs.
 * Inputs can be in any format, and the executor will attempt to convert it to the right format prior to execution.
 */
@Value
@Builder(toBuilder = true)
public class LCTestCase {

    Object expected;
    @Singular
    List<Object> inputs;

    public LCTestCase(Object expected, List<Object> inputs) {
        this.expected = expected;
        this.inputs = inputs == null ? Collections.emptyList() : List.copyOf(inputs);
    }

    public LCTestCase(Object expected, Object... inputs) {
        this.expected = expected;
        this.inputs = inputs == null ? Collections.emptyList() : List.of(inputs);
    }
}
