package org.codi.lct.impl;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.function.Predicate;
import java.util.stream.Collectors;
import lombok.Getter;
import lombok.NonNull;
import lombok.experimental.ExtensionMethod;
import lombok.extern.slf4j.Slf4j;
import org.codi.lct.annotation.LCOutputTransformation;
import org.codi.lct.annotation.LCSolution;
import org.codi.lct.core.LCException;
import org.codi.lct.core.tester.LCExecutor;
import org.codi.lct.core.tester.LCTestCase;
import org.codi.lct.impl.data.LCConfig;
import org.codi.lct.impl.data.LCTestCaseExecution;
import org.codi.lct.impl.helper.JacksonHelper;
import org.codi.lct.impl.helper.ReflectionHelper;

@Slf4j
@ExtensionMethod(JacksonHelper.class)
public final class LCExecutorImpl implements LCExecutor {

    private final Object instance;
    private final List<LCConfig> configs;
    private final Method transformer;
    private final LCCheckerChainImpl checkers;
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

    public LCExecutorImpl(Object instance, List<LCConfig> configs, Method transformer, LCCheckerChainImpl checkers) {
        this.instance = instance;
        this.configs = configs;
        this.transformer = transformer;
        this.checkers = checkers;
        this.executions = new ArrayList<>(configs.size());
    }

    @Override
    public void executeTestCase(@NonNull LCTestCase testCase) {
        this.testCase = testCase;
        configs.forEach(config -> executions.add(executeTestCaseInternal(config)));
        if (configs.size() == 1) {
            LCTestCaseExecution execution = executions.get(0);
            if (!execution.isSuccess()) {
                throw new LCException(
                    "[Test Case Failed] " + generateTestCaseFailureMessage(execution, transformer != null));
            }
        } else {
            String errors = executions.stream()
                .filter(Predicate.not(LCTestCaseExecution::isSuccess))
                .map(execution -> "[" + execution.getConfig().getSolutionMethod().getName() + "] "
                    + generateTestCaseFailureMessage(execution, transformer != null))
                .collect(Collectors.joining("\n"));
            if (!errors.isEmpty()) {
                throw new LCException("Test Case Failed!\n" + errors);
            }
        }
    }

    private LCTestCaseExecution executeTestCaseInternal(LCConfig config) {
        // Resolve parameters
        Object[] resolvedInputs = resolveParameterValues(config.getSolutionMethod(), testCase.getInputs());
        Object resolvedExpectedValue = resolveReturnValue(
            transformer == null ? config.getSolutionMethod() : transformer, testCase.getExpected());
        LCTestCase resolvedTestCase = LCTestCase.builder()
            .inputs(Arrays.asList(resolvedInputs))
            .expected(resolvedExpectedValue)
            .build();
        // Run test case
        long start = System.nanoTime();
        Object actualValue;
        try {
            actualValue = ReflectionHelper.invokeMethod(instance, config.getSolutionMethod(), resolvedInputs,
                LCSolution.class);
        } catch (LCException e) {
            throw e;
        } catch (Throwable e) {
            throw new LCException("Exception inside Solution method: " + config.getSolutionMethod(), e);
        }
        long end = System.nanoTime();
        Object transformedValue =
            transformer == null ? null : applyTransformation(transformer, actualValue, resolvedInputs);
        boolean success = checkers.doCheck(resolvedExpectedValue, transformer == null ? actualValue : transformedValue);
        LCTestCaseExecution execution = LCTestCaseExecution.builder()
            .config(config)
            .testCase(resolvedTestCase)
            .testInstance(instance)
            .start(start)
            .actual(actualValue)
            .transformed(transformedValue)
            .end(end)
            .success(success)
            .build();
        log.debug("Test Case Execution: {}", execution);
        if (config.isTrackExecutionTime()) {
            log.info("[Execution Duration] {}.{}: {} ms", config.getTestClass().getSimpleName(),
                config.getSolutionMethod().getName(), (end - start + 500_000) / 1_000_000);
        }
        return execution;
    }

    private static String generateTestCaseFailureMessage(LCTestCaseExecution execution, boolean isTransformed) {
        String s = "Resolved Test Case - Input: " + execution.getTestCase().getInputs().serialize() + ", Expected: "
            + execution.getTestCase().getExpected().serialize() + ", Actual: ";
        if (isTransformed) {
            s += execution.getTransformed().serialize() + ", Pre-transform: ";
        }
        return s + execution.getActual().serialize();
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
            return rawValue.resolveValue(method.getParameterTypes()[idx], method.getGenericParameterTypes()[idx]);
        } catch (Exception e) {
            throw new LCException("Failed to resolve parameter value - method: " + method + ", param index: " + idx
                + ", conversion target type: " + method.getGenericParameterTypes()[idx].getTypeName() + " raw value: "
                + rawValue, e);
        }
    }

    private static Object resolveReturnValue(Method method, Object rawValue) {
        try {
            return rawValue.resolveValue(method.getReturnType(), method.getGenericReturnType());
        } catch (Exception e) {
            throw new LCException("Failed to resolve return value - method: " + method + ", conversion target type: "
                + method.getGenericReturnType().getTypeName() + " raw value: " + rawValue, e);
        }
    }

    private static Object applyTransformation(Method transformer, Object actual, Object[] inputs) {
        try {
            Object[] resolvedInputs = new Object[transformer.getParameterCount()];
            resolvedInputs[0] = resolveParameterValue(transformer, 0, actual);
            if (transformer.getParameterCount() > 1) {
                for (int i = 0; i < inputs.length; i++) {
                    resolvedInputs[i + 1] = resolveParameterValue(transformer, i + 1, inputs[i]);
                }
            }
            return ReflectionHelper.invokeMethod(null, transformer, resolvedInputs, LCOutputTransformation.class);
        } catch (LCException e) {
            throw e;
        } catch (Throwable e) {
            throw new LCException(
                "Error executing @" + LCOutputTransformation.class.getSimpleName() + " method on result "
                    + actual.serialize(), e);
        }
    }
}
