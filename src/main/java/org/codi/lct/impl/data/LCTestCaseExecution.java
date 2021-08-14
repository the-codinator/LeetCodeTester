package org.codi.lct.impl.data;

import lombok.Builder;
import lombok.Value;
import org.codi.lct.core.tester.LCTestCase;

@Value
@Builder
public class LCTestCaseExecution {

    LCConfig config;
    LCTestCase testCase;
    Object testInstance;
    long start;
    long end;
    Object actual;
    Object transformed;
    boolean success;
}
