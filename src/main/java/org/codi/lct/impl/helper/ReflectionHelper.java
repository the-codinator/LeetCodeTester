package org.codi.lct.impl.helper;

import java.lang.annotation.Annotation;
import java.lang.reflect.GenericDeclaration;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.function.Predicate;
import java.util.stream.Collectors;
import lombok.experimental.UtilityClass;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.reflect.ConstructorUtils;
import org.apache.commons.lang3.reflect.MethodUtils;
import org.codi.lct.annotation.LCOutputTransformation;
import org.codi.lct.annotation.LCSolution;
import org.codi.lct.annotation.LCTestCaseGenerator;
import org.codi.lct.core.LCException;
import org.codi.lct.core.tester.LCTestCase;
import org.codi.lct.impl.checker.LCChecker;

@Slf4j
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
            .filter(m -> LCTestCaseGenerator.AUTO_DISCOVERY_METHOD_NAME.equals(m.getName()))
            .filter(m -> m.getParameterCount() == 0)
            .findAny();
        if (autoMethod.isPresent()) {
            methods = new ArrayList<>(methods);
            methods.add(autoMethod.get());
        }
        return methods;
    }

    public Method findOutputTransformerMethod(Class<?> testClass, List<Method> solutionMethods) {
        List<Method> methods = new ArrayList<>(
            MethodUtils.getMethodsListWithAnnotation(testClass, LCOutputTransformation.class));
        Arrays.stream(testClass.getDeclaredMethods())
            .filter(ReflectionHelper::isPublicMethod)
            .filter(ReflectionHelper::isStaticMethod)
            .filter(m -> LCOutputTransformation.AUTO_DISCOVERY_METHOD_NAME.equals(m.getName()))
            .findAny()
            .ifPresent(methods::add);
        if (methods.size() > 1) {
            throw new LCException("Found multiple @" + LCOutputTransformation.class + " methods: " + methods);
        }
        if (methods.isEmpty()) {
            return null;
        }
        ValidationHelper.validateOutputTransformationMethod(methods.get(0), solutionMethods);
        return methods.get(0);
    }

    public boolean hasReturnValue(Method method) {
        return method.getReturnType() != Void.class;
    }

    public boolean isCompatibleType(Class<?> sourceClass, Type sourceType, Class<?> targetClass, Type targetType) {
        if (!targetClass.isAssignableFrom(sourceClass)) {
            return false;
        }
        if (hasGenericTypeParameters(sourceClass) || hasGenericTypeParameters(targetClass)) {
            // Best effort (which didn't actually amount to much... :P)
            if (!(sourceType instanceof ParameterizedType && targetType instanceof ParameterizedType)) {
                return false;
            }
            Type[] sourceGenericTypes = ((ParameterizedType) sourceType).getActualTypeArguments();
            Type[] targetGenericTypes = ((ParameterizedType) targetType).getActualTypeArguments();
            if (sourceGenericTypes.length != targetGenericTypes.length) {
                return false;
            }
            for (int i = 0; i < sourceGenericTypes.length; i++) {
                Class<?> src;
                if (sourceGenericTypes[i] instanceof Class) {
                    src = (Class<?>) sourceGenericTypes[i];
                } else if (sourceGenericTypes[i] instanceof ParameterizedType) {
                    src = (Class<?>) ((ParameterizedType) sourceGenericTypes[i]).getRawType();
                } else {
                    return false;
                }
                Class<?> tgt;
                if (targetGenericTypes[i] instanceof Class) {
                    tgt = (Class<?>) targetGenericTypes[i];
                } else if (targetGenericTypes[i] instanceof ParameterizedType) {
                    tgt = (Class<?>) ((ParameterizedType) targetGenericTypes[i]).getRawType();
                } else {
                    return false;
                }
                if (!isCompatibleType(src, sourceGenericTypes[i], tgt, targetGenericTypes[i])) {
                    return false;
                }
            }
        }
        return true;
    }

    @SuppressWarnings("unchecked")
    public List<LCTestCase> validateAndInvokeTestCaseGeneratorMethod(Method method) {
        ValidationHelper.validateCustomTestCaseMethod(method);
        try {
            Object result = invokeMethod(null, method, new Object[0], LCTestCaseGenerator.class);
            return result instanceof LCTestCase ? Collections.singletonList((LCTestCase) result)
                : (List<LCTestCase>) result;
        } catch (LCException e) {
            throw e;
        } catch (Throwable e) {
            throw new LCException("Error inside @" + LCTestCaseGenerator.class.getSimpleName() + " method: " + method,
                e);
        }
    }

    public Object invokeMethod(Object instance, Method method, Object[] resolvedInputs,
        Class<? extends Annotation> annotation) throws Throwable {
        try {
            return method.invoke(instance, resolvedInputs);
        } catch (InvocationTargetException e) {
            throw e.getCause();
        } catch (Exception e) {
            throw new LCException("Could not invoke @" + annotation.getSimpleName() + " method: " + method, e);
        }
    }

    public <T> T instantiateDefault(Class<T> cls) {
        if (hasGenericTypeParameters(cls)) {
            throw new LCException(
                LCChecker.class.getSimpleName() + " implementation must not have generic type parameters: " + cls);
        }
        try {
            return ConstructorUtils.invokeConstructor(cls);
        } catch (Exception e) {
            throw new LCException("Failed to instantiate " + LCChecker.class.getSimpleName() + " implementation: " + cls
                + ". Do you have a working public default constructor ?", e);
        }
    }
}
