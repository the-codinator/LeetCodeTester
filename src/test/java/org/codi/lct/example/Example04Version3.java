package org.codi.lct.example;

import java.util.Arrays;
import java.util.List;
import org.codi.lct.annotation.LCTest;
import org.codi.lct.annotation.LCTestCaseGenerator;
import org.codi.lct.core.LCExtension;
import org.codi.lct.core.LCTestCase;
import org.codi.lct.impl.AutoCustomTestCaseProvider;
import org.codi.lct.impl.AutoDataFileTestCaseProvider;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.TestTemplate;
import org.junit.jupiter.api.extension.ExtendWith;

/**
 *
 */
public class Example04Version3 {

    public void v3(LCTestCase testCase) {
        // JUST NEED TO LEVERAGE JUNIT's execution and use ParameterResolvers
        // Can use ObjectMapper.convertValue to transform input to required type while using custom serializers...
        // Research how to convert Java's generic info to Jackson's JavaType/TypeReference
        // ONLY really need to implement the equality checked & data file parser & serializer/deserializer
    }

    @LCTestCaseGenerator
    public static List<LCTestCase> customTestCases() {
        return List.of( // Different approaches for creating testcases
            LCTestCase.builder().input(5).input(10).expected(15).build(), // providing one input at a time to builder
            LCTestCase.builder().inputs(List.of(2, 3)).expected(5).build(), // using list input builder
            new LCTestCase(11, 5, 6), // using varargs input constructor
            new LCTestCase(11, List.of(5, 6)) // using list input constructor
        );
    }

    @LCTest
    public void sum(int[] nums) {
        // THE PROBLEM HERE IS THAT JUNIT NEEDS THE RETURN TYPE TO BE "VOID"
        Arrays.stream(nums).sum();
    }
}
