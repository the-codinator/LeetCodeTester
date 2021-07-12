package org.codi.lct.impl;

import lombok.Getter;
import org.codi.lct.core.LCExecutor;
import org.codi.lct.core.LCTestCase;

public class LCExecutorImpl implements LCExecutor {

    @Getter
    private boolean executedAtLeastOnce;

    @Override
    public void executeTestCase(LCTestCase testCase) {
        executedAtLeastOnce = true;
        // TODO
    }
}
