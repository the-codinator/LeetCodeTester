package org.codi.lct.impl.testcase;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.function.BiConsumer;
import java.util.stream.Stream;
import lombok.NonNull;
import lombok.Value;
import org.codi.lct.core.tester.LCTestCase;
import org.codi.lct.impl.data.LCConfig;
import org.codi.lct.impl.helper.ConfigHelper;
import org.junit.jupiter.api.extension.Extension;
import org.junit.jupiter.api.extension.ExtensionContext;
import org.junit.jupiter.api.extension.TestTemplateInvocationContext;
import org.junit.jupiter.api.extension.TestTemplateInvocationContextProvider;

/**
 * Discovers Data File & Custom Tests
 */
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

        @NonNull String displayName;
        @NonNull LCTestCase testCase;

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
