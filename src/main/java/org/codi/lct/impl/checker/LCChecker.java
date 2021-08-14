package org.codi.lct.impl.checker;

/**
 * Custom checker implementations should implement this interface. The custom checker class MUST have a default
 * constructor for instantiation. It is recommended to extend {@link LCAbstractBaseChecker} instead of implementing this
 * interface directly.
 */
public interface LCChecker {

    /**
     * Whether this checker can handle the checking of provided parameters.
     *
     * @param expected expected output
     * @param actual actual output
     * @return whether equality checking is supported
     */
    boolean canCheck(Object expected, Object actual);

    /**
     * Check whether the values are equal. The checker chain is provided for recursively checking the value.
     *
     * @param chain checker chain to allow recursive checking
     * @param expected expected output
     * @param actual actual output
     * @return whether inputs are equal
     */
    boolean check(LCCheckerChain chain, Object expected, Object actual);
}
