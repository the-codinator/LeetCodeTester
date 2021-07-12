package org.codi.lct.impl;

import lombok.experimental.UtilityClass;
import org.codi.lct.core.LCException;
import org.codi.lct.junit.LCTester;

@UtilityClass
public class ValidationHelper {

    @SuppressWarnings("unchecked")
    public Class<? extends LCTester> validate(Class<?> cls) {
        if (!LCTester.class.isAssignableFrom(cls)) {
            throw new LCException("Class under test does not extend org.codi.lct.junit.LCTester");
        }
        return (Class<? extends LCTester>) cls;
    }
}
