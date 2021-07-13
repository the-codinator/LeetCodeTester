package org.codi.lct.impl.junit;

import java.util.List;
import lombok.Builder;
import lombok.Value;
import org.codi.lct.core.LCTestCase;
import org.codi.lct.junit.LCExtension;
import org.junit.jupiter.api.extension.Extension;
import org.junit.jupiter.api.extension.TestTemplateInvocationContext;

@Value
@Builder
public class AutoTestCaseContext implements TestTemplateInvocationContext {

    LCExtension lcExtension;
    boolean multipleDataFiles;
    int dataFileIndex;
    int testCaseIndex;
    LCTestCase testCase;

    @Override
    public String getDisplayName(int invocationIndex) {
        if (dataFileIndex < 0) {
            return String.format("[%d] Custom Test Case #%d", invocationIndex, testCaseIndex);
        } else if (multipleDataFiles) {
            return String.format("[%d] Data File Test Case #%d.%d", invocationIndex, dataFileIndex, testCaseIndex);
        } else {
            return String.format("[%d] Data File Test Case #%d", invocationIndex, dataFileIndex);
        }
    }

    @Override
    public List<Extension> getAdditionalExtensions() {
        return List.of(new LCTestCaseParameterResolver(testCase), lcExtension);
    }
}
