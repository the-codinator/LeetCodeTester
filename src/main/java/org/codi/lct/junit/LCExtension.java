package org.codi.lct.junit;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;
import lombok.Getter;
import lombok.experimental.ExtensionMethod;
import lombok.extern.slf4j.Slf4j;
import org.codi.lct.core.LCException;
import org.codi.lct.core.LCExecutor;
import org.codi.lct.core.LCTestCase;
import org.codi.lct.data.LCConfig;
import org.codi.lct.data.LCTestCaseResult;
import org.codi.lct.impl.ConfigHelper;
import org.codi.lct.impl.LCExecutorImpl;
import org.codi.lct.impl.ValidationHelper;
import org.codi.lct.impl.junit.JunitHelper;
import org.junit.jupiter.api.extension.AfterAllCallback;
import org.junit.jupiter.api.extension.AfterEachCallback;
import org.junit.jupiter.api.extension.BeforeAllCallback;
import org.junit.jupiter.api.extension.BeforeEachCallback;
import org.junit.jupiter.api.extension.ExtensionContext;
import org.junit.jupiter.api.extension.ParameterContext;
import org.junit.jupiter.api.extension.ParameterResolver;

/**
 * Extension that runs LC tests. This class handles the test lifecycle and does most of the heavy lifting.
 *
 * It is recommended to sub-class {@link LCTester} instead of using this extension directly, unless you want build a
 * custom extension
 */
@Slf4j
@ExtensionMethod({ConfigHelper.class, ValidationHelper.class, JunitHelper.class})
public class LCExtension implements BeforeAllCallback, BeforeEachCallback, AfterEachCallback, AfterAllCallback,
    ParameterResolver {

    private Class<?> testClass;
    private LCConfig classConfig;
    @Getter
    private List<LCTestCase> testCases;
    private List<LCTestCaseResult> results = new ArrayList<>();
    private Method solutionMethod;
    private boolean executedAtLeastOnce;

    @Override
    public void beforeAll(ExtensionContext context) {
        System.out.println("LIFECYCLE: Before All");
        testClass = context.getRequiredTestClass().validate();
        classConfig = ConfigHelper.BASE_CONFIG.withClass(testClass);
    }

    @Override
    public void beforeEach(ExtensionContext context) {
        executedAtLeastOnce = true;
        LCExecutorImpl executor = context.getExecutor();
        executor.setTestClass(testClass);
        executor.setConfig(classConfig.withMethod(solutionMethod));
        executor.setInstance(context.getRequiredTestInstance());
        executor.setSolutionMethod(solutionMethod);
        // TODO: prepare executor
    }

    @Override
    public void afterEach(ExtensionContext context) {
        // TODO: aggregate results
    }

    @Override
    public void afterAll(ExtensionContext context) {
        if (!executedAtLeastOnce) {
            throw new LCException("No tests executed!");
        }
    }

    @Override
    public boolean supportsParameter(ParameterContext parameterContext, ExtensionContext extensionContext) {
        return LCExecutor.class.equals(parameterContext.getParameter().getType());
    }

    @Override
    public Object resolveParameter(ParameterContext parameterContext, ExtensionContext extensionContext) {
        LCExecutorImpl executor = extensionContext.getExecutor();
        return (LCExecutor) executor::executeTestCase; // We do this so clients don't have direct access to the executor
    }
}
