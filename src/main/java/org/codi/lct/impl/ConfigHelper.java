package org.codi.lct.impl;

import java.lang.reflect.AnnotatedElement;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.Properties;
import lombok.experimental.ExtensionMethod;
import lombok.experimental.UtilityClass;
import lombok.extern.slf4j.Slf4j;
import org.codi.lct.annotation.settings.LCAllowMissingExpectedValues;
import org.codi.lct.annotation.settings.LCExecutionTimeLimit;
import org.codi.lct.annotation.settings.LCInputFiles;
import org.codi.lct.annotation.settings.LCTrackExecutionTime;
import org.codi.lct.data.LCConfig;
import org.codi.lct.data.LCConfig.LCConfigBuilder;

@UtilityClass
@ExtensionMethod(Util.class)
@Slf4j
public class ConfigHelper {

    /**
     * Config instance used as base configurations matching annotation property default values
     */
    public static final LCConfig BASE_CONFIG;

    static {
        // Load default properties
        Properties props = FileHelper.loadProperties(null, "lc-tester.properties");
        BASE_CONFIG = LCConfig.builder()
            .trackExecutionTime(props.getBooleanProperty(LCConfig.Fields.trackExecutionTime, false))
            .crashOnFailure(props.getBooleanProperty(LCConfig.Fields.crashOnFailure, false))
            .allowMissingExpectedValues(props.getBooleanProperty(LCConfig.Fields.allowMissingExpectedValues, false))
            .executionTimeLimit(props.getIntegerProperty(LCConfig.Fields.executionTimeLimit, 0))
            .build();
    }

    public LCConfig withClass(LCConfig baseConfig, Class<?> cls) {
        return overlay(baseConfig.toBuilder().inputFiles(FileHelper.defaultTestFileName(cls)), cls);
    }

    public LCConfig withMethod(LCConfig classConfig, Method method) {
        return overlay(classConfig.toBuilder(), method);
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
            builder.inputFiles(Arrays.asList(files));
        }
        return builder.build();
    }
}
