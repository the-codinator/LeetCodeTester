package org.codi.lct.impl;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;
import java.util.function.Predicate;
import java.util.stream.Collectors;
import lombok.Getter;
import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;
import org.codi.lct.core.LCException;
import org.codi.lct.core.LCExecutor;
import org.codi.lct.core.LCTestCase;
import org.codi.lct.core.LCUtil;
import org.codi.lct.data.LCConfig;
import org.codi.lct.data.LCTestCaseExecution;
import org.codi.lct.impl.helper.JacksonHelper;
import org.codi.lct.impl.helper.ReflectionHelper;

@Slf4j
public final class LCExecutorImpl implements LCExecutor {

    private static final ResultChecker checker = new ResultChecker();

    private final ExecutorService executor;
    private final List<LCConfig> configs;
    private final Object instance;
    private LCTestCase testCase;

    @Getter
    private final List<LCTestCaseExecution> executions;

    /**
     * We do this so clients have a harder time gaining direct access to the underlying executor instance.
     */
    @Getter
    private final LCExecutor proxy = (LCExecutor) Proxy.newProxyInstance(this.getClass().getClassLoader(),
        new Class[]{LCExecutor.class}, (proxy, method, methodArgs) -> {
            try {
                return method.invoke(LCExecutorImpl.this, methodArgs);
            } catch (InvocationTargetException e) {
                throw e.getCause();
            }
        });

    public LCExecutorImpl(ExecutorService executor, List<LCConfig> configs, Object instance) {
        this.executor = executor;
        this.configs = configs;
        this.instance = instance;
        this.executions = new ArrayList<>(configs.size());
    }

    @Override
    public void executeTestCase(@NonNull LCTestCase testCase) {
        this.testCase = testCase;
        configs.forEach(this::executeTestCaseOnRunner);
        if (configs.size() == 1) {
            LCTestCaseExecution execution = executions.get(0);
            if (!execution.isSuccess()) {
                throw new LCException("[Test Case Failed]\n" + generateTestCaseFailureMessage(execution),
                    execution.getException());
            }
        } else {
            String errors = executions.stream()
                .filter(Predicate.not(LCTestCaseExecution::isSuccess))
                .map(execution -> "[" + execution.getConfig().getSolutionMethod().getName() + "] "
                    + generateTestCaseFailureMessage(execution))
                .collect(Collectors.joining("\n"));
            if (!errors.isEmpty()) {
                throw new LCException("Test Case Failed!\n" + errors);
            }
        }
    }

    private static String generateTestCaseFailureMessage(LCTestCaseExecution execution) {
        String s = "Resolved Test Case - Input: " + LCUtil.serialize(execution.getTestCase().getInputs());
        if (execution.getException() == null) {
            s = s + ", Expected: " + LCUtil.serialize(execution.getTestCase().getExpected()) + ", Actual: "
                + LCUtil.serialize(execution.getActual());
        } else {
            log.info("Test case failed with exception", execution.getException());
            s = execution.getException().getMessage() + ", " + s;
        }
        return s;
    }

    private void executeTestCaseOnRunner(LCConfig config) {
        int tle = config.getExecutionTimeLimit();
        Future<LCTestCaseExecution> future = executor.submit(() -> executeTestCaseInternal(config, testCase, instance));
        try {
            if (tle > 0) {
                try {
                    executions.add(future.get(tle, TimeUnit.MILLISECONDS));
                } catch (TimeoutException e) {
                    future.cancel(true);
                    executions.add(LCTestCaseExecution.builder()
                        .config(config)
                        .testCase(testCase)
                        .testInstance(instance)
                        .start(-1)
                        .actual(null)
                        .end(-1)
                        .success(false)
                        .exception(new LCException(
                            "Time Limit Exceeded -- method: " + config.getSolutionMethod() + ", testCase: " + testCase))
                        .build());
                }
            } else {
                executions.add(future.get());
            }
        } catch (Exception e) {
            throw new LCException("[Internal] Unknown internal error", e);
        }
    }

    private static LCTestCaseExecution executeTestCaseInternal(LCConfig config, LCTestCase testCase, Object instance) {
        // Resolve parameters
        Object[] params = resolveParameterValues(config.getSolutionMethod(), testCase.getInputs());
        Object returnValue = resolveReturnValue(config.getSolutionMethod(), testCase.getExpected());
        LCTestCase resolvedTestCase = LCTestCase.builder().inputs(Arrays.asList(params)).expected(returnValue).build();
        // Run test case
        long start = System.nanoTime();
        Object actual = null;
        Throwable exception = null;
        try {
            actual = ReflectionHelper.invokeSolutionMethod(instance, config.getSolutionMethod(), params);
        } catch (LCException e) {
            throw e;
        } catch (Throwable e) {
            exception = new LCException(
                "Error inside solution method: " + e.getClass().getSimpleName() + " " + e.getMessage(), e);
        }
        long end = System.nanoTime();
        boolean success = exception == null && checker.checkAnswer(returnValue, actual);
        LCTestCaseExecution execution = LCTestCaseExecution.builder()
            .config(config)
            .testCase(resolvedTestCase)
            .testInstance(instance)
            .start(start)
            .actual(actual)
            .end(end)
            .success(success)
            .exception(exception)
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
}
