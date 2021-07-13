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

    boolean trackExecutionTime; // TODO: impl, check if Junit provides ootb timing of tests, must include instantiation
    boolean crashOnFailure; // TODO: impl, check if Junit provides ootb setting
    boolean allowMissingExpectedValues; // TODO: impl
    int executionTimeLimit; // TODO: impl, JUnit does have something around timeout for tests
    boolean useDefaultFile;
    List<String> inputFiles;

    public static class LCConfigBuilder {
        // So that Javadoc doesn't cry...
    }
}
