package org.codi.lct.impl;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.function.Consumer;
import lombok.Getter;
import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;
import org.codi.lct.core.LCException;
import org.codi.lct.core.LCExecutor;
import org.codi.lct.core.LCTestCase;
import org.codi.lct.data.LCConfig;
import org.codi.lct.data.LCTestCaseExecution;
import org.codi.lct.impl.helper.JacksonHelper;
import org.codi.lct.impl.helper.ReflectionHelper;

@Slf4j
public final class LCExecutorImpl implements LCExecutor {

    private static final ResultChecker checker = new ResultChecker();

    private final List<LCConfig> configs;
    private final Object instance;
    private LCTestCase testCase;

    @Getter
    private final List<LCTestCaseExecution> executions;

    public LCExecutorImpl(List<LCConfig> configs, Object instance) {
        this.configs = configs;
        this.instance = instance;
        this.executions = new ArrayList<>(configs.size());
    }

    @Override
    public void executeTestCase(@NonNull LCTestCase testCase) {
        this.testCase = testCase;
        configs.forEach(config -> executions.add(executeTestCaseInternal(config)));
    }

    private LCTestCaseExecution executeTestCaseInternal(LCConfig config) {
        // Resolve parameters
        Object[] params = resolveParameterValues(config.getSolutionMethod(), testCase.getInputs());
        Object returnValue = resolveReturnValue(config.getSolutionMethod(), testCase.getExpected());
        LCTestCase resolvedTestCase = LCTestCase.builder().inputs(Arrays.asList(params)).expected(returnValue).build();
        // Run test case
        long start = System.nanoTime();
        Object actual = ReflectionHelper.invokeSolutionMethod(instance, config.getSolutionMethod(), params);
        long end = System.nanoTime();
        boolean success = checkResult(returnValue, actual);
        LCTestCaseExecution execution = LCTestCaseExecution.builder()
            .config(config)
            .testCase(resolvedTestCase)
            .testInstance(instance)
            .start(start)
            .actual(actual)
            .end(end)
            .success(success)
            .build();
        log.debug("Test Case Execution: {}", execution);
        if (config.isTrackExecutionTime()) {
            log.info("[Execution Duration] {}: {} ms", config.getSolutionMethod().getName(),
                (end - start + 500_000) / 1_000_000);
        }
        return execution;
    }

    private static Object[] resolveParameterValues(Method method, List<Object> rawValues) {
        int n = rawValues.size();
        if (method.getParameterCount() != n) {
            throw new LCException("[Internal] Mismatched method parameter count during resolution");
        }
        Object[] resolvedParams = new Object[n];
        int idx = 0;
        for (Object rawValue : rawValues) {
            resolvedParams[idx] = resolveParameterValue(method, idx, rawValue);
            idx++;
        }
        return resolvedParams;
    }

    private static Object resolveParameterValue(Method method, int idx, Object rawValue) {
        try {
            return JacksonHelper.resolveValue(rawValue, method.getParameterTypes()[idx],
                method.getGenericParameterTypes()[idx]);
        } catch (IllegalArgumentException e) {
            throw new LCException("Failed to resolve parameter value - method: " + method + ", param index: " + idx
                + ", conversion target type: " + method.getGenericParameterTypes()[idx].getTypeName() + " raw value: "
                + rawValue, e);
        }
    }

    private static Object resolveReturnValue(Method method, Object rawValue) {
        try {
            return JacksonHelper.resolveValue(rawValue, method.getReturnType(), method.getGenericReturnType());
        } catch (IllegalArgumentException e) {
            throw new LCException("Failed to resolve return value - method: " + method + ", conversion target type: "
                + method.getGenericReturnType().getTypeName() + " raw value: " + rawValue, e);
        }
    }

    private boolean checkResult(Object expected, Object actual) {
        // TODO: impl advanced checker
        return Objects.equals(expected, actual);
    }

    public LCExecutor wrapped() {
        return new LCExecutorWrapper(this);
    }

    /**
     * We do this so clients have a harder time gaining direct access to the underlying executor instance
     */
    private static class LCExecutorWrapper implements LCExecutor {

        private final Consumer<LCTestCase> delegate;

        public LCExecutorWrapper(LCExecutorImpl executor) {
            this.delegate = executor::executeTestCase;
        }

        @Override
        public void executeTestCase(LCTestCase testCase) {
            delegate.accept(testCase);
        }
    }
}
