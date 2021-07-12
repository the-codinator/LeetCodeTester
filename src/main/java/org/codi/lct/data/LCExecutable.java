package org.codi.lct.data;

import lombok.NonNull;
import lombok.Value;
import org.codi.lct.core.LCExecutor;
import org.junit.jupiter.api.function.Executable;

@Value
public class LCExecutable implements Executable {

    LCExecutor executor;
    @NonNull org.codi.lct.core.LCTestCase testCase;

    @Override
    public void execute() {
        executor.executeTestCase(testCase);
    }
}
