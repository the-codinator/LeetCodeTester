package org.codi.lct.impl;

import java.lang.reflect.Method;
import java.util.Arrays;
import lombok.experimental.UtilityClass;
import org.codi.lct.annotation.LCConfig;
import org.codi.lct.data.Config;
import org.codi.lct.data.Config.ConfigBuilder;
import org.codi.lct.junit.LCTester;

@UtilityClass
public class ConfigHelper {

    /**
     * Config instance used as base configurations matching annotation property default values
     */
    public static final Config BASE_INSTANCE;

    static {
        @LCConfig
        class Dummy {

        }

        LCConfig config = Dummy.class.getAnnotation(LCConfig.class);
        // Create base instance with default values (note: files is ignored)
        BASE_INSTANCE = Config.builder()
            .enabled(config.enabled())
            .timed(config.timed())
            .crash(config.crash())
            .tle(config.tle())
            .build();
    }

    public Config withClass(Config baseConfig, Class<? extends LCTester> cls) {
        return overlay(baseConfig.toBuilder().files(FileHelper.defaultTestFileName(cls)),
            cls.getAnnotation(LCConfig.class));
    }

    public Config withMethod(Config classConfig, Method method) {
        return overlay(classConfig.toBuilder(), method.getAnnotation(LCConfig.class));
    }

    private Config overlay(ConfigBuilder builder, LCConfig config) {
        if (config != null) {
            if (!config.enabled()) {
                builder.enabled(false);
            }
            if (config.timed()) {
                builder.timed(true);
            }
            if (config.files().length != 0) {
                builder.files(Arrays.asList(config.files()));
            }
        }
        return builder.build();
    }
}
