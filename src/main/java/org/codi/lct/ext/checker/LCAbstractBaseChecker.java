package org.codi.lct.ext.checker;

/**
 * Boilerplate checker for values having a common ancestral type {@param <T>}
 */
public abstract class LCAbstractBaseChecker<T> implements LCChecker {

    private final Class<T> baseType;

    protected LCAbstractBaseChecker(Class<T> baseType) {
        this.baseType = baseType;
    }

    @Override
    public final boolean canCheck(Object expected, Object actual) {
        return baseType.isAssignableFrom(expected.getClass()) && baseType.isAssignableFrom(actual.getClass());
    }

    @Override
    @SuppressWarnings("unchecked")
    public final boolean check(LCCheckerChain chain, Object expected, Object actual) {
        return checkInternal(chain, (T) expected, (T) actual);
    }

    protected abstract boolean checkInternal(LCCheckerChain chain, T expected, T actual);
}
