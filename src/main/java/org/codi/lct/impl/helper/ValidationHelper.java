package org.codi.lct.impl.helper;

import java.lang.reflect.Method;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.List;
import lombok.experimental.ExtensionMethod;
import lombok.experimental.UtilityClass;
import org.codi.lct.annotation.LCSolution;
import org.codi.lct.annotation.LCTestCaseGenerator;
import org.codi.lct.core.LCException;
import org.codi.lct.core.LCTestCase;

@UtilityClass
@ExtensionMethod(ReflectionHelper.class)
public class ValidationHelper {

    public void validateSolutionMethod(Method method) {
        if (!method.isPublicMethod() || method.isStaticMethod()) {
            throw new LCException(
                "@" + LCSolution.class.getSimpleName() + " method must be public & non-static: " + method);
        }
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
        if (!method.isPublicMethod() || !method.isStaticMethod()) {
            throw new LCException(
                "@" + LCTestCaseGenerator.class.getSimpleName() + " method must be public & static: " + method);
        }
        if (method.hasGenericTypeParameters()) {
            throw new LCException(
                "@" + LCTestCaseGenerator.class.getSimpleName() + " method must not have generic type parameters: "
                    + method);
        }
        if (method.getParameterCount() != 0) {
            throw new LCException("@" + LCTestCaseGenerator.class.getSimpleName() + " method cannot accept parameters");
        }
        if (!List.class.isAssignableFrom(method.getReturnType())) {
            throw new LCException("@" + LCTestCaseGenerator.class.getSimpleName() + " method must return List<"
                + LCTestCase.class.getSimpleName() + ">");
        }
        Type[] args = ((ParameterizedType) method.getGenericReturnType()).getActualTypeArguments();
        if (args == null || args.length != 1 || LCTestCase.class != args[0]) {
            throw new LCException("@" + LCTestCaseGenerator.class.getSimpleName() + " method must return List<"
                + LCTestCase.class.getSimpleName() + ">");
        }
    }
}
