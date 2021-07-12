package org.codi.lct.data;

import java.util.List;
import lombok.Builder;
import lombok.Value;
import org.codi.lct.annotation.LCConfig;

/**
 * Contextual data object for {@link LCConfig}
 */
@Value
@Builder(toBuilder = true)
public class Config {

    boolean enabled;
    boolean timed;
    List<String> files;
    boolean crash;
    int tle;

    public static class ConfigBuilder {
        // So that Javadoc doesn't cry...
    }
}
