package org.codi.lct.impl.testcase;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.function.BiConsumer;
import java.util.stream.Collectors;
import lombok.experimental.ExtensionMethod;
import lombok.extern.slf4j.Slf4j;
import org.codi.lct.core.LCException;
import org.codi.lct.core.LCTestCase;
import org.codi.lct.data.LCConfig;
import org.codi.lct.impl.helper.ConfigHelper;
import org.codi.lct.impl.helper.FileHelper;
import org.codi.lct.impl.helper.JacksonHelper;
import org.codi.lct.impl.helper.JunitHelper;
import org.codi.lct.impl.helper.ReflectionHelper;

/**
 * Discovers Data File & Custom Tests
 */
@Slf4j
@ExtensionMethod({ConfigHelper.class, FileHelper.class, JacksonHelper.class, JunitHelper.class, ReflectionHelper.class})
public final class AutoDataFileTestCaseProvider extends AutoTestCaseProviderBase {

    @Override
    public void generateTestCases(LCConfig classConfig, BiConsumer<String, LCTestCase> testCaseCollector) {
        int inputSize = classConfig.getTestClass().findSolutionMethods().get(0).getParameterCount();
        int dataIdx = 1;
        for (String file : classConfig.getInputFiles()) {
            int testCaseIdx = 1;
            for (LCTestCase testCase : dataFileTestCases(file, classConfig, inputSize)) {
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

    private static List<LCTestCase> dataFileTestCases(String file, LCConfig config, int inputSize) {
        List<LCTestCase> testCases;
        if (file.isEmpty()) {
            testCases = dataFileTestCases(config.getTestClass().defaultTestFileNamePrimary(), inputSize);
            if (testCases == null) {
                // We don't check secondary if file exists but is empty
                testCases = dataFileTestCases(config.getTestClass().defaultTestFileNameSecondary(), inputSize);
            }
        } else {
            testCases = dataFileTestCases(file, inputSize);
        }
        if (testCases == null) {
            testCases = Collections.emptyList();
        }
        if (config.isWarnOnEmptyOrMissingTestFiles() && testCases.isEmpty()) {
            log.warn("Missing / empty test case file: " + file);
        }
        return testCases;
    }

    private static List<LCTestCase> dataFileTestCases(String file, int inputSize) {
        log.debug("Reading test cases from file: {}", file);
        try (InputStream stream = file.readResource()) {
            if (stream == null) {
                log.debug("File not found: " + file);
                return null;
            }
            List<Object> values = stream.readAllValues();
            return parseTestCases(values, inputSize);
        } catch (Exception e) {
            throw new LCException("Error reading test cases from file: " + file, e);
        }
    }

    private static List<LCTestCase> parseTestCases(List<Object> values, int inputSize) {
        List<LCTestCase.LCTestCaseBuilder> builders = new ArrayList<>();
        Iterator<Object> iterator = values.iterator();
        while (values.size() > builders.size() * (inputSize + 1)) {
            LCTestCase.LCTestCaseBuilder builder = LCTestCase.builder();
            builders.add(builder);
            for (int i = inputSize; i > 0; i--) {
                builder.input(iterator.next());
            }
        }
        if (values.size() - builders.size() * inputSize != builders.size()) {
            throw new LCException(
                "Data File Input Mismatch: ran out of values to read full test cases. May be you missed some outputs?");
        }
        List<LCTestCase> result = new ArrayList<>();
        for (LCTestCase.LCTestCaseBuilder builder : builders) {
            result.add(builder.expected(iterator.next()).build());
        }
        return result;
    }
}
