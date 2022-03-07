package org.codi.lct.impl.adapter.eq;

import org.codi.lct.ext.checker.LCChecker;
import org.codi.lct.ext.checker.LCCheckerChain;

/**
 * Special checker with the highest priority to check nulls (we don't want to deal with NPEs anywhere)
 */
public final class NullChecker implements LCChecker {

    @Override
    public boolean canCheck(Object expected, Object actual) {
        return expected == null || actual == null;
    }

    @Override
    public boolean check(LCCheckerChain chain, Object expected, Object actual) {
        return expected == actual;
    }
}
