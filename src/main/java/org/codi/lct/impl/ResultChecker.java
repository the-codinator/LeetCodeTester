package org.codi.lct.impl;

import java.util.Arrays;
import java.util.Objects;
import org.codi.lct.core.LCUtil;
import org.codi.lct.ds.ListNode;
import org.codi.lct.ds.TreeNode;

public class ResultChecker {

    public boolean checkAnswer(Object expected, Object actual) {
        // TODO: impl advanced checker
        if (expected instanceof ListNode || expected instanceof TreeNode) {
            return Arrays.equals(LCUtil.convert(expected, int[].class), LCUtil.convert(actual, int[].class));
        }
        return Objects.equals(expected, actual);
    }
}
