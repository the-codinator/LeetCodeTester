package org.codi.lct.impl.junit;

import lombok.experimental.UtilityClass;
import org.codi.lct.impl.LCExecutorImpl;
import org.junit.jupiter.api.extension.ExtensionContext;

@UtilityClass
public class JunitHelper {

    public LCExecutorImpl getExecutor(ExtensionContext context) {
        return context.getStore(ExtensionContext.Namespace.GLOBAL).getOrComputeIfAbsent(LCExecutorImpl.class);
    }
}
