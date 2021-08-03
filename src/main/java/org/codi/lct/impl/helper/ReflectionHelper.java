package org.codi.lct.impl.helper;

import java.lang.reflect.GenericDeclaration;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.function.Predicate;
import java.util.stream.Collectors;
import lombok.experimental.UtilityClass;
import org.apache.commons.lang3.reflect.MethodUtils;
import org.codi.lct.annotation.LCSolution;
import org.codi.lct.annotation.LCTestCaseGenerator;
import org.codi.lct.core.LCException;
import org.codi.lct.core.LCTestCase;
import org.codi.lct.impl.Constants;

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
        throw new LCException("Failed to identify solution methods, checkout @" + LCSolution.class.getSimpleName());
    }

    public List<Method> findPublicNonStaticMethods(Class<?> testClass) {
        return Arrays.stream(testClass.getDeclaredMethods())
            .filter(ReflectionHelper::isPublicMethod)
            .filter(Predicate.not(ReflectionHelper::isStaticMethod))
            .collect(Collectors.toList());
    }

    public List<Method> findTestCaseGeneratorMethods(Class<?> testClass) {
        List<Method> methods = MethodUtils.getMethodsListWithAnnotation(testClass, LCTestCaseGenerator.class);
        Optional<Method> autoMethod = Arrays.stream(testClass.getDeclaredMethods())
            .filter(ReflectionHelper::isPublicMethod)
            .filter(ReflectionHelper::isStaticMethod)
            .filter(m -> Constants.DEFAULT_TEST_CASE_GENERATOR_METHOD_NAME.equals(m.getName()))
            .filter(m -> m.getParameterCount() == 0)
            .findAny();
        if (autoMethod.isPresent()) {
            methods = new ArrayList<>(methods);
            methods.add(autoMethod.get());
        }
        return methods;
    }

    public boolean hasReturnValue(Method method) {
        return method.getReturnType() != Void.class;
    }

    @SuppressWarnings("unchecked")
    public List<LCTestCase> validateAndInvokeTestCaseGeneratorMethod(Method method) {
        ValidationHelper.validateCustomTestCaseMethod(method);
        try {
            Object result = method.invoke(null);
            return result instanceof LCTestCase ? Collections.singletonList((LCTestCase) result)
                : (List<LCTestCase>) result;
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
