package org.codi.lct.example;

import java.util.Set;
import org.codi.lct.core.LCTestCase;
import org.codi.lct.core.LCTester;

/**
 * Custom verification of the result.
 *
 * This is extremely useful when there are multiple correct answers. The result must match a certain condition rather
 * than a specific result.
 */
public class Example06CustomVerification extends LCTester {

    public static LCTestCase lcTestCases() {
        return LCTestCase.builder().expected(3).input(Set.of("long", "1234", "xyz", "ttt", "cartoon")).build();
    }

    public static int lcTransform(String result, Set<String> strings) {
        if (!strings.contains(result)) {
            return -1;
        }
        return result.length();
    }

    public String getAnySmallestLengthString(Set<String> strings) {
        int min = Integer.MAX_VALUE;
        String ans = null;
        for (String s : strings) {
            if (s.length() < min) {
                min = s.length();
                ans = s;
            }
        }
        return ans;
    }
}
