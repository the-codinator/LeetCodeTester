package org.codi.lct.data;

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

    boolean trackExecutionTime;
    boolean crashOnFailure;
    boolean allowMissingExpectedValues;
    int executionTimeLimit;
    List<String> inputFiles;

    public static class LCConfigBuilder {
        // So that Javadoc doesn't cry...
    }
}
