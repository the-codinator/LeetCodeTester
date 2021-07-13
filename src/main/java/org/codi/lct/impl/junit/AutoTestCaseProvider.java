package org.codi.lct.impl.junit;

import java.util.stream.Stream;
import org.codi.lct.junit.LCExtension;
import org.junit.jupiter.api.extension.ExtensionContext;
import org.junit.jupiter.api.extension.TestTemplateInvocationContext;
import org.junit.jupiter.api.extension.TestTemplateInvocationContextProvider;

public class AutoTestCaseProvider implements TestTemplateInvocationContextProvider {

    private LCExtension lcExtension = new LCExtension();

    @Override
    public boolean supportsTestTemplate(ExtensionContext context) {
        return true;
    }

    @Override
    public Stream<TestTemplateInvocationContext> provideTestTemplateInvocationContexts(ExtensionContext context) {
        return null;
    }
}
