package org.codi.lct.impl;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import lombok.experimental.ExtensionMethod;
import lombok.extern.slf4j.Slf4j;
import org.codi.lct.core.LCException;
import org.codi.lct.core.tester.LCExecutor;
import org.codi.lct.core.tester.LCTester;
import org.codi.lct.impl.data.LCConfig;
import org.codi.lct.impl.data.LCTestCaseExecution;
import org.codi.lct.impl.helper.ConfigHelper;
import org.codi.lct.impl.helper.JunitHelper;
import org.codi.lct.impl.helper.ReflectionHelper;
import org.codi.lct.impl.helper.ValidationHelper;
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
public class LCExtensionImpl implements BeforeAllCallback, BeforeEachCallback, AfterEachCallback, AfterAllCallback,
    ParameterResolver {

    private final LCCheckerChainImpl checkers = new LCCheckerChainImpl();
    private final List<LCTestCaseExecution> results = new ArrayList<>();

    private LCConfig classConfig;
    private List<LCConfig> solutionMethodConfigs;
    private Method outputTransformationMethod;
    private boolean executedAtLeastOnce;

    // ***** Test Lifecycle ***** //

    @Override
    public void beforeAll(ExtensionContext context) {
        Class<?> testClass = context.getRequiredTestClass();
        log.info("Testing Class: " + testClass.getSimpleName());
        classConfig = ConfigHelper.BASE_CONFIG.withClass(testClass);
        List<Method> solutionMethods = ReflectionHelper.findSolutionMethods(testClass);
        outputTransformationMethod = ReflectionHelper.findOutputTransformerMethod(testClass, solutionMethods);
        solutionMethodConfigs = solutionMethods.stream()
            .map(method -> classConfig.withMethod(method))
            .collect(Collectors.toList());
        checkers.initDefaultCheckers();
    }

    @Override
    public void beforeEach(ExtensionContext context) {
        executedAtLeastOnce = true;
        // Prep new executor for each test case
        context.createExecutor(
            new LCExecutorImpl(context.getRequiredTestInstance(), solutionMethodConfigs, outputTransformationMethod,
                checkers));
    }

    @Override
    public void afterEach(ExtensionContext context) {
        results.addAll(context.getExecutor().getExecutions());
    }

    @Override
    public void afterAll(ExtensionContext context) {
        if (!executedAtLeastOnce) {
            throw new LCException("No Tests Executed!");
        }
        if (results.stream().allMatch(LCTestCaseExecution::isSuccess)) {
            log.info("All Test Cases Passed Successfully!");
        } else {
            log.error("There are test case failures...");
        }
    }

    // ***** Parameter Resolvers ***** //

    @Override
    public boolean supportsParameter(ParameterContext parameterContext, ExtensionContext extensionContext) {
        return LCExecutor.class.equals(parameterContext.getParameter().getType());
    }

    @Override
    public Object resolveParameter(ParameterContext parameterContext, ExtensionContext extensionContext) {
        return extensionContext.getExecutor().getProxy();
    }
}
