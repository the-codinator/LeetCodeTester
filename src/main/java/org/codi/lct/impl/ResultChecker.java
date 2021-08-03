package org.codi.lct.impl;

import java.util.Objects;

public class ResultChecker {

    public boolean checkAnswer(Object expected, Object actual) {
        // TODO: impl advanced checker
        return Objects.equals(expected, actual);
    }
}
