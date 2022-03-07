package org.codi.lct.impl;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import lombok.NonNull;
import org.codi.lct.ext.checker.LCChecker;
import org.codi.lct.ext.checker.LCCheckerChain;
import org.codi.lct.impl.adapter.eq.ArrayChecker;
import org.codi.lct.impl.adapter.eq.NullChecker;
import org.codi.lct.impl.helper.ReflectionHelper;

public class LCCheckerChainImpl implements LCCheckerChain {

    private final Set<Class<? extends LCChecker>> registered = new HashSet<>();
    private final List<LCChecker> chain = new ArrayList<>();

    void initDefaultCheckers() {
        registerChecker(NullChecker.class); // get nulls out of the way first
        registerChecker(ArrayChecker.class);
    }

    @Override
    public boolean doCheck(Object expected, Object actual) {
        for (LCChecker checker : chain) {
            // try every registered checker
            if (checker.canCheck(expected, actual)) {
                return checker.check(this, expected, actual);
            }
        }
        // fallback check
        return expected.equals(actual);
    }

    public void registerChecker(@NonNull Class<? extends LCChecker> checkerClass) {
        /*
         TODO: provide this hook to clients to somehow register their own checkers
         Note: This hook should be at the global level somehow (maybe an annotation on the data-structure itself ?)
           so that clients can register their checkers just once, and not each time they write a test class...
           the annotation could probable be picked up in the executor's resolveReturnType method
         */
        if (!registered.add(checkerClass)) {
            // checker class already registered
            return;
        }
        chain.add(ReflectionHelper.instantiateDefault(checkerClass));
    }
}
