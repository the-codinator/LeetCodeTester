package org.codi.lct.data;

import java.lang.reflect.Method;
import java.util.List;
import lombok.Builder;
import lombok.Value;
import lombok.experimental.FieldNameConstants;

/**
 * Contextual data object for configuration settings
 */
@Value
@Builder(toBuilder = true)
@FieldNameConstants
public class LCConfig {

    Class<?> testClass;
    Method solutionMethod;
    boolean trackExecutionTime;
    boolean allowMissingExpectedValues; // TODO: impl
    int executionTimeLimit;
    int customSerializationThreshold;
    boolean warnOnEmptyOrMissingTestFiles;
    List<String> inputFiles;

    public static class LCConfigBuilder {
        // So that Javadoc doesn't cry...
    }
}
