package org.codi.lct.junit;

import java.util.ArrayList;
import java.util.List;
import lombok.experimental.ExtensionMethod;
import lombok.extern.slf4j.Slf4j;
import org.codi.lct.core.LCException;
import org.codi.lct.core.LCExecutor;
import org.codi.lct.data.LCConfig;
import org.codi.lct.data.LCTestCaseResult;
import org.codi.lct.impl.ConfigHelper;
import org.codi.lct.impl.ValidationHelper;
import org.codi.lct.impl.junit.JunitHelper;
import org.junit.jupiter.api.extension.AfterAllCallback;
import org.junit.jupiter.api.extension.AfterEachCallback;
import org.junit.jupiter.api.extension.BeforeAllCallback;
import org.junit.jupiter.api.extension.BeforeEachCallback;
import org.junit.jupiter.api.extension.ExtensionContext;
import org.junit.jupiter.api.extension.ParameterContext;
import org.junit.jupiter.api.extension.ParameterResolver;

/**
 * Extension that runs LC tests. This class handles the test lifecycle and does most of the heavy lifting.
 *
 * It is recommended to sub-class {@link LCTester} instead of using this extension directly, unless you want build a
 * custom extension
 */
@Slf4j
@ExtensionMethod({ConfigHelper.class, ValidationHelper.class, JunitHelper.class})
public class LCExtension implements BeforeAllCallback, BeforeEachCallback, AfterEachCallback, AfterAllCallback,
    ParameterResolver {

    private Class<?> testClass;
    private LCConfig classConfig;
    private List<LCTestCaseResult> results = new ArrayList<>();

    @Override
    public void beforeAll(ExtensionContext context) {
        System.out.println("LIFECYCLE: Before All");
        testClass = context.getRequiredTestClass().validate();
        classConfig = ConfigHelper.BASE_CONFIG.withClass(testClass);
    }

    @Override
    public void beforeEach(ExtensionContext context) {
        System.out.println("LIFECYCLE: Before Each");
    }

    @Override
    public void afterEach(ExtensionContext context) {
        System.out.println("LIFECYCLE: After Each");
    }

    @Override
    public void afterAll(ExtensionContext context) {
        if (!context.getExecutor().isExecutedAtLeastOnce()) {
            throw new LCException("No tests executed!");
        }
    }

    @Override
    public boolean supportsParameter(ParameterContext parameterContext, ExtensionContext extensionContext) {
        return LCExecutor.class.equals(parameterContext.getParameter().getType());
    }

    @Override
    public Object resolveParameter(ParameterContext parameterContext, ExtensionContext extensionContext) {
        return extensionContext.getExecutor();
    }
}
