package org.codi.lct.impl.helper;

import java.util.Objects;
import lombok.experimental.UtilityClass;
import org.codi.lct.core.LCException;
import org.codi.lct.impl.LCExecutorImpl;
import org.junit.jupiter.api.extension.ExtensionContext;

@UtilityClass
public class JunitHelper {

    public void createExecutor(ExtensionContext context, LCExecutorImpl executor) {
        if (context.getStore(ExtensionContext.Namespace.GLOBAL).get(LCExecutorImpl.class) != null) {
            throw new LCException("[Internal] Executor already exists");
        }
        context.getStore(ExtensionContext.Namespace.GLOBAL).put(LCExecutorImpl.class, executor);
    }

    public LCExecutorImpl getExecutor(ExtensionContext context) {
        return Objects.requireNonNull(
            context.getStore(ExtensionContext.Namespace.GLOBAL).get(LCExecutorImpl.class, LCExecutorImpl.class),
            "[Internal] Executor is missing in context");
    }
}
