package org.codi.lct.impl;

import java.lang.reflect.Method;
import java.util.Collections;
import java.util.List;
import java.util.function.BiConsumer;
import java.util.stream.Collectors;
import lombok.extern.slf4j.Slf4j;
import org.codi.lct.core.LCTestCase;
import org.codi.lct.data.LCConfig;

/**
 * Discovers Data File & Custom Tests
 */
@Slf4j
public final class AutoCustomTestCaseProvider extends AutoTestCaseProviderBase {

    @Override
    public void generateTestCases(LCConfig classConfig, BiConsumer<String, LCTestCase> testCaseCollector) {
        List<LCTestCase> testCases = customTestCases(classConfig.getTestClass());
        int idx = 1;
        for (LCTestCase testCase : testCases) {
            testCaseCollector.accept(generateDisplayName(testCases.size() > 1, idx++), testCase);
        }
    }

    private static String generateDisplayName(boolean many, int idx) {
        String name = "Custom Test Case";
        if (many) {
            name += " #" + idx;
        }
        return name;
    }

    private static List<LCTestCase> customTestCases(Class<?> testClass) {
        List<Method> customs = ReflectionHelper.findTestCaseGeneratorMethods(testClass);
        if (customs.isEmpty()) {
            return Collections.emptyList();
        }
        return customs.stream()
            .map(ReflectionHelper::invokeTestCaseGeneratorMethod)
            .flatMap(List::stream)
            .collect(Collectors.toList());
    }
}
