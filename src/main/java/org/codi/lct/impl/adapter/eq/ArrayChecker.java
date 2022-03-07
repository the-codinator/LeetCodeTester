package org.codi.lct.impl.adapter.eq;

import java.lang.reflect.Array;
import java.util.Objects;
import org.codi.lct.ext.checker.LCChecker;
import org.codi.lct.ext.checker.LCCheckerChain;

public class ArrayChecker implements LCChecker {

    @Override
    public boolean canCheck(Object expected, Object actual) {
        return expected.getClass().isArray() || actual.getClass().isArray();
    }

    @Override
    public boolean check(LCCheckerChain chain, Object expected, Object actual) {
        Class<?> expectedCls = expected.getClass().getComponentType();
        Class<?> actualCls = actual.getClass().getComponentType();
        if (expectedCls == null || actualCls == null) {
            // One is not an array
            return false;
        }
        int len = Array.getLength(expected);
        if (len != Array.getLength(actual)) {
            // mis-matched length
            return false;
        }
        if (!expectedCls.isAssignableFrom(actualCls)) {
            // Mis-matched types
            return false;
        }
        if (expectedCls.isPrimitive()) {
            // Primitive array
            return Objects.deepEquals(expected, actual);
        }
        for (int i = 0; i < len; i++) {
            if (!chain.doCheck(Array.get(expected, i), Array.get(actual, i))) {
                // corresponding array elements don't match
                return false;
            }
        }
        return true;
    }
}
