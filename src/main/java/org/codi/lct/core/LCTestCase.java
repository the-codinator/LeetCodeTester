package org.codi.lct.core;

import java.util.List;
import lombok.Value;

@Value
public class LCTestCase {

    List<Object> input;
    Object expected;

    public LCTestCase(List<Object> input, Object expected) {
        this.input = input == null ? null : List.copyOf(input);
        this.expected = expected;
    }
}
