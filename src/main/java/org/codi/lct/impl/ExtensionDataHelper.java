package org.codi.lct.impl;

import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Predicate;
import lombok.NonNull;
import lombok.experimental.UtilityClass;
import org.apache.commons.lang3.reflect.FieldUtils;
import org.codi.lct.core.LCError;
import org.codi.lct.data.ExtensionData;
import org.codi.lct.data.SolutionData;
import org.codi.lct.junit.LCExpected;
import org.codi.lct.junit.LCInput;

/**
 * Caches the {@link SolutionData} for the class under test
 */
@UtilityClass
public class ExtensionDataHelper {

    private final Map<Class<?>, ExtensionData> cache = new HashMap<>();

    public ExtensionData get(@NonNull Class<?> clazz) {
        return cache.computeIfAbsent(clazz, ExtensionDataHelper::generate);
    }

    private ExtensionData generate(Class<?> clazz) {
        var inputFields = FieldUtils.getFieldsListWithAnnotation(clazz, LCInput.class);
        if (!inputFields.stream().map(Field::getType).allMatch(Predicate.isEqual(String.class))) {
            throw new LCError("@LCInput fields must be of string type");
        }
        var expectedFields = FieldUtils.getFieldsListWithAnnotation(clazz, LCExpected.class);
        if (expectedFields.size() != 1) {
            throw new LCError("Missing / too many @LCExpected fields");
        }
        var expectedValueField = expectedFields.get(0);
        if (expectedValueField.getType() != String.class) {
            throw new LCError("@LCExpected field must be of string type");
        }
        return ExtensionData.builder()
            .classUnderTest(clazz)
            .solutionData(SolutionDataHelper.get(clazz))
            .inputFields(inputFields)
            .expectedValueField(expectedValueField)
            .build();
    }
}
