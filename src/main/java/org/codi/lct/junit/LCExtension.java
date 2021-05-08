package org.codi.lct.junit;

import org.codi.lct.impl.ExtensionDataHelper;
import org.junit.jupiter.api.extension.AfterTestExecutionCallback;
import org.junit.jupiter.api.extension.BeforeAllCallback;
import org.junit.jupiter.api.extension.Extension;
import org.junit.jupiter.api.extension.ExtensionContext;

/**
 * JUnit 5 extension to wrap on the class for executing multiple test scenarios.
 * Each test scenario should define inputs and expected results using {@link LCInput} and {@link LCExpected}.
 */
public class LCExtension implements Extension, BeforeAllCallback, AfterTestExecutionCallback {

    /**
     * Generate and cache test class information
     */
    @Override
    public void beforeAll(ExtensionContext context) {
        ExtensionDataHelper.get(context.getRequiredTestClass());
    }

    @Override
    public void afterTestExecution(ExtensionContext context) throws Exception {
        // TODO: execution
    }
}
