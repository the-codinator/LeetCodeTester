package org.codi.lct.core;

import java.util.Collections;
import java.util.List;
import lombok.Builder;
import lombok.Singular;
import lombok.Value;

@Value
@Builder
public class LCTestCase {

    Object expected;
    @Singular List<Object> inputs;

    public LCTestCase(Object expected, List<Object> inputs) {
        this.expected = expected;
        this.inputs = inputs == null ? Collections.emptyList() : List.copyOf(inputs);
    }

    public LCTestCase(Object expected, Object... inputs) {
        this.expected = expected;
        this.inputs = inputs == null ? Collections.emptyList() : List.of(inputs);
    }

    public static class LCTestCaseBuilder {
        // So that Javadoc doesn't cry...
    }
}
