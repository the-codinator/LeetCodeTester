package org.codi.lct.example;

import java.util.Arrays;
import java.util.List;
import org.codi.lct.annotation.LCSolution;
import org.codi.lct.annotation.LCTest;
import org.codi.lct.annotation.LCTestCaseGenerator;
import org.codi.lct.core.LCExecutor;
import org.codi.lct.core.LCExtension;
import org.codi.lct.core.LCTestCase;
import org.codi.lct.core.LCTester;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

/**
 *
 */
public class Example04Version3 {

    @LCTestCaseGenerator
    public void v3(LCTestCase testCase) {
        // JUST NEED TO LEVERAGE JUNIT's execution and use ParameterResolvers
        // Can use ObjectMapper.convertValue to transform input to required type while using custom serializers...
        // Research how to convert Java's generic info to Jackson's JavaType/TypeReference
        // ONLY really need to implement the equality checked & data file parser & serializer/deserializer
    }

    @LCTest
    public int sum(int[] nums) {
        return Arrays.stream(nums).sum();
    }
}
