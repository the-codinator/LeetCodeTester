package org.codi.lct.impl.junit;

import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import java.util.stream.Stream;
import lombok.experimental.ExtensionMethod;
import lombok.extern.slf4j.Slf4j;
import org.codi.lct.core.LCTestCase;
import org.codi.lct.data.LCConfig;
import org.codi.lct.impl.FileHelper;
import org.codi.lct.impl.LCExecutorImpl;
import org.codi.lct.junit.LCExtension;
import org.junit.jupiter.api.extension.ExtensionContext;
import org.junit.jupiter.api.extension.TestTemplateInvocationContext;
import org.junit.jupiter.api.extension.TestTemplateInvocationContextProvider;

/**
 * Discovers Data File & Custom Tests
 */
@Slf4j
@ExtensionMethod(JunitHelper.class)
public class AutoTestCaseProvider implements TestTemplateInvocationContextProvider {

    private static final LCTestCase dummy = new LCTestCase(null, List.of());

    private final LCExtension lcExtension = new LCExtension();

    @Override
    public boolean supportsTestTemplate(ExtensionContext context) {
        return true;
    }

    @Override
    public Stream<TestTemplateInvocationContext> provideTestTemplateInvocationContexts(ExtensionContext context) {
        List<TestTemplateInvocationContext> result = new LinkedList<>();
        LCExecutorImpl executor = context.getExecutor();
        LCConfig config = executor.getConfig();
        if (config.isUseDefaultFile()) {
            int idx = 1;
            for (LCTestCase testCase : dataFileTestCases(FileHelper.defaultTestFileName(executor.getTestClass()),
                false)) {
                result.add(new AutoTestCaseContext(lcExtension, false, 0, idx++, testCase));
            }
        } else {
            int fileNumber = 1;
            for (String file : config.getInputFiles()) {
                int idx = 1;
                for (LCTestCase testCase : dataFileTestCases(file, true)) {
                    result.add(new AutoTestCaseContext(lcExtension, true, fileNumber, idx++, testCase));
                }
                fileNumber++;
            }
        }
        int idx = 1;
        for (LCTestCase testCase : customTestCases(executor.getTestClass())) {
            result.add(new AutoTestCaseContext(lcExtension, false, -1, idx++, testCase));
        }
        return result.stream();
    }

    private static List<LCTestCase> dataFileTestCases(String file, boolean warnOnNoTests) {
        // TODO: impl
        return List.of(dummy, dummy);
    }

    private static List<LCTestCase> customTestCases(Class<?> testClass) {
        // TODO: impl
        return Collections.emptyList();
    }
}
