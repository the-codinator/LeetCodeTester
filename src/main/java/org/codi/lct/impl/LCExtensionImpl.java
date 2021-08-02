package org.codi.lct.impl;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import lombok.experimental.ExtensionMethod;
import lombok.extern.slf4j.Slf4j;
import org.codi.lct.core.LCException;
import org.codi.lct.core.LCExecutor;
import org.codi.lct.core.LCTester;
import org.codi.lct.data.LCConfig;
import org.codi.lct.data.LCTestCaseExecution;
import org.junit.jupiter.api.extension.AfterAllCallback;
import org.junit.jupiter.api.extension.AfterEachCallback;
import org.junit.jupiter.api.extension.BeforeAllCallback;
import org.junit.jupiter.api.extension.BeforeEachCallback;
import org.junit.jupiter.api.extension.ExtensionContext;
import org.junit.jupiter.api.extension.InvocationInterceptor;
import org.junit.jupiter.api.extension.ParameterContext;
import org.junit.jupiter.api.extension.ParameterResolver;
import org.junit.jupiter.api.extension.ReflectiveInvocationContext;

/**
 * Extension that runs LC tests. This class handles the test lifecycle and does most of the heavy lifting.
 *
 * It is recommended to sub-class {@link LCTester} instead of using this extension directly, unless you want build a
 * custom extension
 */
@Slf4j
@ExtensionMethod({ConfigHelper.class, ValidationHelper.class, JunitHelper.class})
public class LCExtensionImpl implements BeforeAllCallback, BeforeEachCallback, InvocationInterceptor, AfterEachCallback,
    AfterAllCallback, ParameterResolver {

    private LCConfig classConfig;
    private List<LCConfig> solutionMethodConfigs;
    private List<LCTestCaseExecution> results = new ArrayList<>();
    private boolean executedAtLeastOnce;

    // ***** Test Lifecycle ***** //

    @Override
    public void beforeAll(ExtensionContext context) {
        Class<?> testClass = context.getRequiredTestClass();
        log.info("Testing class: " + testClass.getSimpleName());
        classConfig = ConfigHelper.BASE_CONFIG.withClass(testClass);
        solutionMethodConfigs = ReflectionHelper.findSolutionMethods(testClass)
            .stream()
            .map(method -> classConfig.withMethod(method))
            .collect(Collectors.toList());
    }

    @Override
    public void beforeEach(ExtensionContext context) {
        executedAtLeastOnce = true;
        // Prep new executor for each test case
        context.createExecutor(new LCExecutorImpl(solutionMethodConfigs, context.getRequiredTestInstance()));
    }

    @Override
    public void interceptTestTemplateMethod(Invocation<Void> invocation,
        ReflectiveInvocationContext<Method> invocationContext, ExtensionContext extensionContext) throws Throwable {
        if (!invocationContext.getExecutable().isLCTestMethod()) {
            // Ignore if it's some other @TestTemplate
            invocation.proceed();
            return;
        }
        Object result;
        try {
            result = invocation.proceed();
        } catch (Throwable e) {
            throw new LCException("Error in test method execution", e);
        }
        extensionContext.getExecutor().checkTestResult(result);
    }

    @Override
    public void afterEach(ExtensionContext context) {
        results.addAll(context.getExecutor().getExecutions());
    }

    @Override
    public void afterAll(ExtensionContext context) {
        if (!executedAtLeastOnce) {
            throw new LCException("No tests executed!");
        }
        // TODO: summary & aggregations
    }

    // ***** Parameter Resolvers ***** //

    @Override
    public boolean supportsParameter(ParameterContext parameterContext, ExtensionContext extensionContext) {
        return LCExecutor.class.equals(parameterContext.getParameter().getType());
    }

    @Override
    public Object resolveParameter(ParameterContext parameterContext, ExtensionContext extensionContext) {
        return extensionContext.getExecutor().wrapped();
    }
}
