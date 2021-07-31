package org.codi.lct.impl;

import java.util.Objects;
import org.codi.lct.data.LCTestCaseExecution;

public class ResultChecker {

    public boolean checkAnswer(LCTestCaseExecution execution) {
        return Objects.equals(execution.getTestCase().getExpected(), execution.getActual());
    }
}
