package org.codi.lct.impl;

import java.util.Arrays;
import java.util.Objects;
import org.codi.lct.ds.ListNode;
import org.codi.lct.ds.TreeNode;
import org.codi.lct.impl.helper.JacksonHelper;

public class ResultChecker {

    public boolean checkAnswer(Object expected, Object actual, Object rawValueForLogging) {
        // TODO: impl advanced checker
        if (expected instanceof ListNode || expected instanceof TreeNode) {
            return Arrays.equals(JacksonHelper.convert(expected, int[].class),
                JacksonHelper.convert(actual, int[].class));
        }
        return Objects.equals(expected, actual);
    }
}
