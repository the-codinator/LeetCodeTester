package org.codi.lct.core.tester;

import java.util.Collections;
import java.util.List;
import lombok.Builder;
import lombok.Singular;
import lombok.Value;
import lombok.experimental.ExtensionMethod;
import org.codi.lct.impl.helper.JacksonHelper;

/**
 * Defines a test case based on expected output & provided inputs.
 * Inputs can be in any format, and the executor will attempt to convert it to the right format prior to execution.
 */
@Value
@Builder(toBuilder = true)
@ExtensionMethod(JacksonHelper.class)
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

    /**
     * A convenient way of creating a test case using string format without a data file
     *
     * @param expected stringified expected value
     * @param inputs stringified inputs
     * @return parsed test case
     */
    public static LCTestCase parse(String expected, String inputs) {
        return new LCTestCase(expected.deserialize(Object.class), inputs.readAllValues());
    }

    /**
     * A convenient way of creating a test case using string format without a data file
     *
     * @param inputsAndExpected stringified inputs followed by expected value
     * @return parsed test case
     */
    public static LCTestCase parse(String inputsAndExpected) {
        List<Object> items = inputsAndExpected.readAllValues();
        return new LCTestCase(items.remove(items.size() - 1), items);
    }
}
