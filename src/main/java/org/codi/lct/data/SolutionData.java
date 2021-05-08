package org.codi.lct.data;

import com.fasterxml.jackson.databind.JavaType;
import java.lang.reflect.Method;
import java.util.List;
import lombok.Builder;
import lombok.NonNull;
import lombok.Value;

@Value
@Builder
public class SolutionData {

    @NonNull Class<?> classUnderTest;
    @NonNull List<Method> solutionMethods;
    @NonNull List<JavaType> parameterTypes;
    @NonNull JavaType returnType;
}
