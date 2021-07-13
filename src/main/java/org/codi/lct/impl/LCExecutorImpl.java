package org.codi.lct.impl;

import java.lang.reflect.Method;
import lombok.Getter;
import lombok.NonNull;
import lombok.Setter;
import org.codi.lct.core.LCExecutor;
import org.codi.lct.core.LCTestCase;
import org.codi.lct.data.LCConfig;

@Getter
@Setter
public class LCExecutorImpl implements LCExecutor {

    private Class<?> testClass;
    private LCConfig config;
    private Object instance;
    private Method solutionMethod;

    private long startTs;
    private long endTs;
    private LCTestCase testCase;
    private Object actual;

    @Override
    public void executeTestCase(@NonNull LCTestCase testCase) {
        this.testCase = testCase;
        // TODO: impl
    }
}
