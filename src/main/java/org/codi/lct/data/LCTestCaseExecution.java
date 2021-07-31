package org.codi.lct.data;

import lombok.Builder;
import lombok.Value;
import org.codi.lct.core.LCTestCase;

@Value
@Builder
public class LCTestCaseExecution {

    LCConfig config;
    LCTestCase testCase;
    Object testInstance;
    int start;
    int end;
    Object actual;
}
