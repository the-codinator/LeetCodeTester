package org.codi.lct.ext.checker;

public interface LCCheckerChain {

    /**
     * Recursive check equality using all available {@link LCChecker}s
     *
     * @param expected expected output
     * @param actual actual output
     * @return whether values are equal
     */
    boolean doCheck(Object expected, Object actual);
}
