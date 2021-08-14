package org.codi.lct.impl.helper;

import java.lang.reflect.AnnotatedElement;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.Collections;
import java.util.Optional;
import java.util.Properties;
import lombok.experimental.UtilityClass;
import lombok.extern.slf4j.Slf4j;
import org.codi.lct.annotation.settings.LCAllowMissingExpectedValues;
import org.codi.lct.annotation.settings.LCExecutionTimeLimit;
import org.codi.lct.annotation.settings.LCInputFiles;
import org.codi.lct.annotation.settings.LCTrackExecutionTime;
import org.codi.lct.core.LCException;
import org.codi.lct.impl.data.LCConfig;
import org.codi.lct.impl.data.LCConfig.LCConfigBuilder;

@Slf4j
@UtilityClass
public class ConfigHelper {

    /**
     * Config instance used as base configurations matching annotation property default values
     */
    public static final LCConfig BASE_CONFIG;

    static {
        // Load default properties
        Properties props = FileHelper.loadProperties(null, "lc-tester.properties");
        BASE_CONFIG = LCConfig.builder()
            .trackExecutionTime(getBooleanProperty(props, LCConfig.Fields.trackExecutionTime, false))
            .allowMissingExpectedValues(getBooleanProperty(props, LCConfig.Fields.allowMissingExpectedValues, false))
            .executionTimeLimit(getIntegerProperty(props, LCConfig.Fields.executionTimeLimit, 0))
            .customSerializationThreshold(
                getIntegerProperty(props, LCConfig.Fields.customSerializationThreshold, 10000))
            .inputFiles(Collections.singletonList(""))
            .warnOnEmptyOrMissingTestFiles(false)
            .build();
    }

    public LCConfig withClass(LCConfig baseConfig, Class<?> cls) {
        return overlay(baseConfig.toBuilder().testClass(cls), cls);
    }

    public LCConfig withMethod(LCConfig classConfig, Method method) {
        if (classConfig.getTestClass() != method.getDeclaringClass()) {
            throw new LCException("[Internal] Method declaring class mismatch");
        }
        return overlay(classConfig.toBuilder().solutionMethod(method), method);
    }

    private LCConfig overlay(LCConfigBuilder builder, AnnotatedElement element) {
        if (element.isAnnotationPresent(LCTrackExecutionTime.class)) {
            builder.trackExecutionTime(element.getAnnotation(LCTrackExecutionTime.class).value());
        }
        if (element.isAnnotationPresent(LCAllowMissingExpectedValues.class)) {
            builder.allowMissingExpectedValues(element.getAnnotation(LCAllowMissingExpectedValues.class).value());
        }
        if (element.isAnnotationPresent(LCExecutionTimeLimit.class)) {
            builder.executionTimeLimit(element.getAnnotation(LCExecutionTimeLimit.class).value());
        }
        if (element.isAnnotationPresent(LCInputFiles.class)) {
            String[] files = element.getAnnotation(LCInputFiles.class).value();
            if (files.length == 0) {
                log.warn("Found @LCInputFiles with empty file list on member: " + element);
            }
            builder.warnOnEmptyOrMissingTestFiles(true).inputFiles(Arrays.asList(files));
        }
        return builder.build();
    }

    private boolean getBooleanProperty(Properties props, String key, boolean fallback) {
        return Optional.ofNullable(props.getProperty(key)).map(Boolean::parseBoolean).orElse(fallback);
    }

    private int getIntegerProperty(Properties props, String key, int fallback) {
        return Optional.ofNullable(props.getProperty(key)).map(Integer::parseInt).orElse(fallback);
    }
}
