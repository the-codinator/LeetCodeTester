package org.codi.lct.impl;

import java.util.Collections;
import java.util.List;
import java.util.function.BiConsumer;
import lombok.experimental.ExtensionMethod;
import lombok.extern.slf4j.Slf4j;
import org.codi.lct.core.LCTestCase;
import org.codi.lct.data.LCConfig;

/**
 * Discovers Data File & Custom Tests
 */
@Slf4j
@ExtensionMethod({JunitHelper.class, ConfigHelper.class, FileHelper.class})
public final class AutoDataFileTestCaseProvider extends AutoTestCaseProviderBase {

    private static final LCTestCase dummy = new LCTestCase(null, List.of());

    @Override
    public void generateTestCases(LCConfig classConfig, BiConsumer<String, LCTestCase> testCaseCollector) {
        int dataIdx = 1;
        for (String file : classConfig.getInputFiles()) {
            int testCaseIdx = 1;
            for (LCTestCase testCase : dataFileTestCases(file, classConfig)) {
                testCaseCollector.accept(
                    generateDisplayName(classConfig.getInputFiles().size() > 1, dataIdx++, testCaseIdx++), testCase);
            }
        }
    }

    private static String generateDisplayName(boolean many, int dataIdx, int testCaseIdx) {
        StringBuilder sb = new StringBuilder("Data File Test Case #");
        if (many) {
            sb.append(dataIdx).append('.');
        }
        return sb.append(testCaseIdx).toString();
    }

    private static List<LCTestCase> dataFileTestCases(String file, LCConfig config) {
        List<LCTestCase> testCases;
        if (file.isEmpty()) {
            testCases = dataFileTestCases(config.getTestClass().defaultTestFileNamePrimary());
            if (testCases == null) {
                // We don't check secondary if file exists but is empty
                testCases = dataFileTestCases(config.getTestClass().defaultTestFileNameSecondary());
            }
        } else {
            testCases = dataFileTestCases(file);
        }
        if (testCases == null) {
            testCases = Collections.emptyList();
        }
        if (config.isWarnOnEmptyOrMissingTestFiles() && testCases.isEmpty()) {
            log.warn("Missing / empty test case file: " + file);
        }
        return testCases;
    }

    private static List<LCTestCase> dataFileTestCases(String file) {
        // TODO: impl
        return List.of(dummy, dummy);
    }
}
