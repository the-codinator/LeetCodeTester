package org.codi.lct.impl;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.function.BiConsumer;
import java.util.stream.Stream;
import lombok.Value;
import lombok.extern.slf4j.Slf4j;
import org.codi.lct.core.LCTestCase;
import org.codi.lct.data.LCConfig;
import org.junit.jupiter.api.extension.Extension;
import org.junit.jupiter.api.extension.ExtensionContext;
import org.junit.jupiter.api.extension.TestTemplateInvocationContext;
import org.junit.jupiter.api.extension.TestTemplateInvocationContextProvider;

/**
 * Discovers Data File & Custom Tests
 */
@Slf4j
public abstract class AutoTestCaseProviderBase implements TestTemplateInvocationContextProvider {

    @Override
    public final boolean supportsTestTemplate(ExtensionContext context) {
        return true;
    }

    @Override
    public final Stream<TestTemplateInvocationContext> provideTestTemplateInvocationContexts(ExtensionContext context) {
        List<TestTemplateInvocationContext> result = new ArrayList<>();
        generateTestCases(ConfigHelper.withClass(ConfigHelper.BASE_CONFIG, context.getRequiredTestClass()),
            (name, tc) -> result.add(new AutoTestCaseContext(name, tc)));
        return result.stream();
    }

    public abstract void generateTestCases(LCConfig classConfig, BiConsumer<String, LCTestCase> testCaseCollector);

    @Value
    private static class AutoTestCaseContext implements TestTemplateInvocationContext {

        String displayName;
        LCTestCase testCase;

        @Override
        public String getDisplayName(int invocationIndex) {
            return String.format("[%d] %s", invocationIndex, displayName);
        }

        @Override
        public List<Extension> getAdditionalExtensions() {
            return Collections.singletonList(new TestCaseParameterResolver(testCase));
        }
    }
}
