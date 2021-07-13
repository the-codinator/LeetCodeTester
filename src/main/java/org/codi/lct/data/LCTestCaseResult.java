package org.codi.lct.data;

import lombok.Value;
import org.codi.lct.core.LCTestCase;

@Value
public class LCTestCaseResult {

    LCTestCase testCase;
    Object actual;
}
