package org.codi.lct.ext.checker;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;
import org.codi.lct.impl.helper.JacksonHelper;

/**
 * Boilerplate checker for complex data types {@param <T>} that can be transformed to simpler formats {@param <U>} for
 * recursive checking
 */
public abstract class LCAbstractTransformChecker<T, U> implements LCChecker {

    private final Class<?> targetType;
    private final Set<Class<?>> supportedTypes;

    protected LCAbstractTransformChecker(Class<?> targetType, Class<?>... classes) {
        this.targetType = targetType;
        this.supportedTypes = new HashSet<>(Arrays.asList(classes));
    }

    @Override
    public boolean canCheck(Object expected, Object actual) {
        return expected.getClass() == actual.getClass() && supportedTypes.contains(expected.getClass());
    }

    @Override
    public boolean check(LCCheckerChain chain, Object expected, Object actual) {
        return chain.doCheck(JacksonHelper.convert(expected, targetType), JacksonHelper.convert(actual, targetType));
    }
}
