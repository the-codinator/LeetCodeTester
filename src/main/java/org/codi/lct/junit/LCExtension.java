package org.codi.lct.junit;

import lombok.experimental.ExtensionMethod;
import org.codi.lct.data.LCConfig;
import org.codi.lct.impl.ConfigHelper;
import org.codi.lct.impl.ValidationHelper;
import org.junit.jupiter.api.extension.BeforeAllCallback;
import org.junit.jupiter.api.extension.ExtensionContext;

@ExtensionMethod({ConfigHelper.class, ValidationHelper.class})
public class LCExtension implements BeforeAllCallback {

    private Class<? extends LCTester> testClass;
    private LCConfig classConfig;
    private LCTester testInstance;

    @Override
    public void beforeAll(ExtensionContext context) throws Exception {
        testClass = context.getRequiredTestClass().validate();
        classConfig = ConfigHelper.BASE_CONFIG.withClass(testClass);
    }
}
