package org.codi.lct.impl.helper;

import java.lang.reflect.GenericDeclaration;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.Arrays;
import java.util.List;
import java.util.function.Predicate;
import java.util.stream.Collectors;
import lombok.experimental.UtilityClass;
import org.apache.commons.lang3.reflect.MethodUtils;
import org.codi.lct.annotation.LCSolution;
import org.codi.lct.annotation.LCTestCaseGenerator;
import org.codi.lct.core.LCException;
import org.codi.lct.core.LCTestCase;

@UtilityClass
public class ReflectionHelper {

    public boolean isPublicMethod(Method method) {
        return Modifier.isPublic(method.getModifiers());
    }

    public boolean isStaticMethod(Method method) {
        return Modifier.isStatic(method.getModifiers());
    }

    public boolean hasGenericTypeParameters(GenericDeclaration declaration) {
        return declaration.getTypeParameters().length > 0;
    }

    public List<Method> findSolutionMethods(Class<?> testClass) {
        // LCSolution annotated methods
        List<Method> methods = MethodUtils.getMethodsListWithAnnotation(testClass, LCSolution.class);
        if (!methods.isEmpty()) {
            methods.forEach(ValidationHelper::validateSolutionMethod);
            ValidationHelper.validateMultipleSolutionMethods(methods);
            return methods;
        }
        // Single "public" & "non-static" method
        methods = findPublicNonStaticMethods(testClass);
        if (methods.size() == 1) {
            return methods;
        }
        // Not found
        throw new LCException(
            "No valid solution methods found, refer docs for class " + LCSolution.class.getSimpleName());
    }

    public List<Method> findPublicNonStaticMethods(Class<?> testClass) {
        return Arrays.stream(testClass.getDeclaredMethods())
            .filter(ReflectionHelper::isPublicMethod)
            .filter(Predicate.not(ReflectionHelper::isStaticMethod))
            .collect(Collectors.toList());
    }

    public List<Method> findTestCaseGeneratorMethods(Class<?> testClass) {
        return MethodUtils.getMethodsListWithAnnotation(testClass, LCTestCaseGenerator.class);
    }

    public boolean hasReturnValue(Method method) {
        return method.getReturnType() != Void.class;
    }

    @SuppressWarnings("unchecked")
    public List<LCTestCase> validateAndInvokeTestCaseGeneratorMethod(Method method) {
        ValidationHelper.validateCustomTestCaseMethod(method);
        try {
            return (List<LCTestCase>) method.invoke(null);
        } catch (IllegalAccessException | IllegalArgumentException e) {
            throw new LCException(
                "Could not invoke @" + LCTestCaseGenerator.class.getSimpleName() + " method: " + method
                    + " - try checking Java encapsulation related issues");
        } catch (InvocationTargetException e) {
            throw new LCException("Error inside @" + LCTestCaseGenerator.class.getSimpleName() + " method: " + method,
                e.getCause());
        }
    }

    public Object invokeSolutionMethod(Object instance, Method method, Object[] resolvedInputs) {
        try {
            return method.invoke(instance, resolvedInputs);
        } catch (IllegalAccessException | IllegalArgumentException e) {
            throw new LCException("Could not invoke @" + LCSolution.class.getSimpleName() + " method: " + method
                + " - try checking Java encapsulation related issues");
        } catch (InvocationTargetException e) {
            throw new LCException("Error inside @" + LCSolution.class.getSimpleName() + " method: " + method,
                e.getCause());
        }
    }
}
