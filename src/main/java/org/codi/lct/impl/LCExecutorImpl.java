package org.codi.lct.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;
import lombok.Getter;
import lombok.NonNull;
import org.codi.lct.core.LCExecutor;
import org.codi.lct.core.LCTestCase;
import org.codi.lct.data.LCConfig;
import org.codi.lct.data.LCTestCaseExecution;

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
        this.executions = new ArrayList<>();
    }

    @Override
    public void executeTestCase(@NonNull LCTestCase testCase) {
        this.testCase = testCase;
        // TODO: impl: execution (write code in ReflectionHelper, refer to Junit's Reflection util to invoke methods)
        // TODO: impl: checker
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
