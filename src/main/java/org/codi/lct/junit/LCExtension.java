package org.codi.lct.junit;

import lombok.experimental.ExtensionMethod;
import lombok.extern.slf4j.Slf4j;
import org.codi.lct.core.LCException;
import org.codi.lct.core.LCExecutor;
import org.codi.lct.data.LCConfig;
import org.codi.lct.impl.ConfigHelper;
import org.codi.lct.impl.LCExecutorImpl;
import org.codi.lct.impl.ValidationHelper;
import org.junit.jupiter.api.extension.AfterAllCallback;
import org.junit.jupiter.api.extension.AfterEachCallback;
import org.junit.jupiter.api.extension.BeforeAllCallback;
import org.junit.jupiter.api.extension.BeforeEachCallback;
import org.junit.jupiter.api.extension.ExtensionContext;
import org.junit.jupiter.api.extension.ExtensionContext.Namespace;
import org.junit.jupiter.api.extension.InvocationInterceptor;
import org.junit.jupiter.api.extension.ParameterContext;
import org.junit.jupiter.api.extension.ParameterResolutionException;
import org.junit.jupiter.api.extension.ParameterResolver;

/**
 * Extension that runs LC tests. This class handles the test lifecycle and does most of the heavy lifting.
 *
 * It is recommended to sub-class {@link LCTester} instead of using this extension directly, unless you want build a
 * custom extension
 */
@Slf4j
@ExtensionMethod({ConfigHelper.class, ValidationHelper.class})
public class LCExtension implements BeforeAllCallback, InvocationInterceptor, BeforeEachCallback, AfterEachCallback,
    AfterAllCallback, ParameterResolver {

    private static final String STORE_START_TS = "startTs";
    private static final String STORE_CONFIG = "config";

    private Class<? extends LCTester> testClass;
    private LCConfig classConfig;
    private LCExecutorImpl testExecutor;

    @Override
    public void beforeAll(ExtensionContext context) {
        testClass = context.getRequiredTestClass().validate();
        classConfig = ConfigHelper.BASE_CONFIG.withClass(testClass);
        testExecutor = new LCExecutorImpl();
    }

    @Override
    public void beforeEach(ExtensionContext context) {
        _beforeEach(context);
    }

    @Override
    public void interceptDynamicTest(Invocation<Void> invocation, ExtensionContext extensionContext) throws Throwable {
        _beforeEach(extensionContext);
        invocation.proceed();
        _afterEach(extensionContext);
    }

    private void _beforeEach(ExtensionContext context) {
        ExtensionContext.Store store = context.getStore(Namespace.GLOBAL);
        LCConfig methodConfig = classConfig.withMethod(context.getRequiredTestMethod());
        // TODO: we're getting an error on getRequireTestMethod... its present before TestFactory, but not for DynamicTest
        store.put(STORE_CONFIG, methodConfig);
        store.put(STORE_START_TS, System.currentTimeMillis());
    }

    @Override
    public void afterEach(ExtensionContext context) {
        _afterEach(context);
    }

    private void _afterEach(ExtensionContext context) {
        long end = System.currentTimeMillis();
        ExtensionContext.Store store = context.getStore(Namespace.GLOBAL);
        // TODO: is this store unique per test case ?? of do we need to provide a namespace using context.uniqueId()
        long start = store.get(STORE_START_TS, Long.class);
        LCConfig methodConfig = store.get(STORE_CONFIG, LCConfig.class);
        // TODO: timing
    }

    @Override
    public void afterAll(ExtensionContext context) {
        if (!testExecutor.isExecutedAtLeastOnce()) {
            throw new LCException("No tests executed!");
        }
    }

    @Override
    public boolean supportsParameter(ParameterContext parameterContext, ExtensionContext extensionContext)
        throws ParameterResolutionException {
        return LCExecutor.class.isAssignableFrom(parameterContext.getParameter().getType());
    }

    @Override
    public Object resolveParameter(ParameterContext parameterContext, ExtensionContext extensionContext)
        throws ParameterResolutionException {
        return testExecutor;
    }
}
