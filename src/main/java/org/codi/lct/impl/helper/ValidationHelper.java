package org.codi.lct.impl.helper;

import java.lang.reflect.Method;
import java.lang.reflect.Type;
import java.util.List;
import lombok.experimental.ExtensionMethod;
import lombok.experimental.UtilityClass;
import org.codi.lct.annotation.LCOutputTransformation;
import org.codi.lct.annotation.LCSolution;
import org.codi.lct.annotation.LCTestCaseGenerator;
import org.codi.lct.core.LCException;
import org.codi.lct.core.tester.LCTestCase;

@UtilityClass
@ExtensionMethod(ReflectionHelper.class)
public class ValidationHelper {

    private static final List<LCTestCase> unused = null;
    private static final Type typeListLCTestCase;

    static {
        try {
            typeListLCTestCase = ValidationHelper.class.getDeclaredField("unused").getGenericType();
        } catch (NoSuchFieldException e) {
            throw new RuntimeException("Someone changed the name of the List<LCTestCase> field", e);
        }
    }

    public void validateSolutionMethod(Method method) {
        // Ensure public static
        if (!method.isPublicMethod() || method.isStaticMethod()) {
            throw new LCException(
                "@" + LCSolution.class.getSimpleName() + " method must be public & non-static: " + method);
        }
        // Ensure no generic type params
        if (method.hasGenericTypeParameters()) {
            throw new LCException(
                "@" + LCSolution.class.getSimpleName() + " method must not have generic type parameters: " + method);
        }
    }

    public void validateMultipleSolutionMethods(List<Method> methods) {
        if (methods.size() < 2) {
            return;
        }
        int count = -1;
        boolean ret = false;
        // Ensure arg count & presence / absence of return type
        for (Method method : methods) {
            if (count < 0) {
                count = method.getParameterCount();
                ret = method.hasReturnValue();
            } else if (method.getParameterCount() != count || method.hasReturnValue() != ret) {
                throw new LCException("Incompatible @" + LCSolution.class.getSimpleName()
                    + " methods - all method signatures must match");
            }
        }
    }

    public void validateCustomTestCaseMethod(Method method) {
        // Ensure public static
        if (!method.isPublicMethod() || !method.isStaticMethod()) {
            throw new LCException(
                "@" + LCTestCaseGenerator.class.getSimpleName() + " method must be public & static: " + method);
        }
        // Ensure no generic type params
        if (method.hasGenericTypeParameters()) {
            throw new LCException(
                "@" + LCTestCaseGenerator.class.getSimpleName() + " method must not have generic type parameters: "
                    + method);
        }
        // Ensure no args
        if (method.getParameterCount() != 0) {
            throw new LCException("@" + LCTestCaseGenerator.class.getSimpleName() + " method cannot accept parameters");
        }
        // Ensure return type LCTestCase or List<LCTestCase>
        if (!ReflectionHelper.isCompatibleType(method.getReturnType(), method.getGenericReturnType(), LCTestCase.class,
            LCTestCase.class) && !ReflectionHelper.isCompatibleType(method.getReturnType(),
            method.getGenericReturnType(), List.class, typeListLCTestCase)) {
            throw new LCException("@" + LCTestCaseGenerator.class.getSimpleName() + " method must return type '"
                + LCTestCase.class.getSimpleName() + "' or 'List<" + LCTestCase.class.getSimpleName() + ">'");
        }
    }

    public void validateOutputTransformationMethod(Method method, List<Method> solutions) {
        // Ensure public static
        if (!method.isPublicMethod() || !method.isStaticMethod()) {
            throw new LCException(
                "@" + LCOutputTransformation.class.getSimpleName() + " method must be public & static: " + method);
        }
        // Ensure no generic type params
        if (method.hasGenericTypeParameters()) {
            throw new LCException(
                "@" + LCOutputTransformation.class.getSimpleName() + " method must not have generic type parameters: "
                    + method);
        }
        for (Method solution : solutions) {
            // Parameter count
            if (method.getParameterCount() != 1 && method.getParameterCount() != solution.getParameterCount() + 1) {
                throw new LCException(
                    "Bad argument list for @" + LCOutputTransformation.class.getSimpleName() + " method: " + method
                        + ". Must have 1st argument matching return type of solution method and optionally all input"
                        + " arguments of the solution method: " + solution);
            }
            // Ensure first parameter match
            if (!ReflectionHelper.isCompatibleType(solution.getReturnType(), solution.getGenericReturnType(),
                method.getParameterTypes()[0], method.getGenericParameterTypes()[0])) {
                throw new LCException(
                    "First parameter of @" + LCOutputTransformation.class.getSimpleName() + "method: " + method
                        + " must match return type of @" + LCSolution.class.getSimpleName() + " method: " + solution);
            }
            if (method.getParameterCount() > 1) {
                // Ensure additional parameters match input
                for (int i = 1; i < method.getParameterCount(); i++) {
                    if (!ReflectionHelper.isCompatibleType(solution.getParameterTypes()[i - 1],
                        solution.getGenericParameterTypes()[i - 1], method.getParameterTypes()[i],
                        method.getGenericParameterTypes()[i])) {
                        throw new LCException(
                            "Additional parameters of @" + LCOutputTransformation.class.getSimpleName() + " method: "
                                + method + " must match argument list of solution method: " + solution);
                    }
                }
            }
        }
    }
}
