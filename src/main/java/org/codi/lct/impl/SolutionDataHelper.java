package org.codi.lct.impl;

import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.type.TypeFactory;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Predicate;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import lombok.NonNull;
import lombok.experimental.UtilityClass;
import org.apache.commons.lang3.reflect.MethodUtils;
import org.codi.lct.annotation.LCSolution;
import org.codi.lct.core.LCError;
import org.codi.lct.data.SolutionData;

/**
 * Caches the {@link SolutionData} for the class under test
 */
@UtilityClass
public class SolutionDataHelper {

    private final Map<Class<?>, SolutionData> cache = new HashMap<>();

    public SolutionData get(@NonNull Class<?> clazz) {
        return cache.computeIfAbsent(clazz, SolutionDataHelper::generate);
    }

    private SolutionData generate(Class<?> clazz) {
        var solutionMethods = MethodUtils.getMethodsListWithAnnotation(clazz, LCSolution.class);
        if (solutionMethods.isEmpty()) {
            throw new LCError("No @Solution methods found in " + clazz.getSimpleName());
        }
        var parameterTypes = getParameterTypes(solutionMethods.get(0)).collect(Collectors.toUnmodifiableList());
        var returnType = TypeFactory.defaultInstance().constructType(solutionMethods.get(0).getGenericReturnType());

        // Validation for multiple @Solution
        if (solutionMethods.size() > 1) {
            var parameterTypeStrings = parameterTypes.stream().map(JavaType::toString).collect(Collectors.toList());
            if (!solutionMethods.subList(1, solutionMethods.size())
                .stream()
                .map(m -> getParameterTypes(m).map(JavaType::toString).collect(Collectors.toList()))
                .allMatch(Predicate.isEqual(parameterTypeStrings))) {
                throw new LCError("Solution methods differ in parameter types");
            }
            var returnTypeString = returnType.toString();
            if (!solutionMethods.subList(1, solutionMethods.size())
                .stream()
                .map(m -> m.getGenericReturnType().toString())
                .allMatch(Predicate.isEqual(returnTypeString))) {
                throw new LCError("Solution methods differ in return types");
            }
        }

        return SolutionData.builder()
            .classUnderTest(clazz)
            .solutionMethods(solutionMethods)
            .parameterTypes(parameterTypes)
            .returnType(returnType)
            .build();
    }

    private Stream<JavaType> getParameterTypes(Method m) {
        return Arrays.stream(m.getGenericParameterTypes()).map(TypeFactory.defaultInstance()::constructType);
    }
}
