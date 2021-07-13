package org.codi.lct.impl;

import lombok.Getter;
import lombok.NonNull;
import org.codi.lct.core.LCExecutor;
import org.codi.lct.core.LCTestCase;

public class LCExecutorImpl implements LCExecutor {

    @Getter
    private boolean executedAtLeastOnce;
    private long startTs;

    @Override
    public void executeTestCase(@NonNull LCTestCase testCase) {
        executedAtLeastOnce = true;
        // TODO: impl
    }
}
