package org.codi.lct.impl.helper;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.json.JsonReadFeature;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.MappingIterator;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.databind.json.JsonMapper;
import com.fasterxml.jackson.databind.type.TypeFactory;
import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;
import lombok.Getter;
import lombok.experimental.UtilityClass;

@UtilityClass
public class JacksonHelper {

    /**
     * Shared Jackson {@link ObjectMapper} instance
     */
    @Getter
    private static final ObjectMapper objectMapper = JsonMapper.builder()
        .enable(JsonParser.Feature.AUTO_CLOSE_SOURCE)
        .enable(JsonReadFeature.ALLOW_JAVA_COMMENTS)
        .enable(JsonReadFeature.ALLOW_YAML_COMMENTS)
        .enable(DeserializationFeature.FAIL_ON_TRAILING_TOKENS)
        .enable(SerializationFeature.INDENT_OUTPUT)
        .build();

    public List<Object> readAllValues(InputStream input) throws IOException {
        List<Object> values = new ArrayList<>();
        for (MappingIterator<Object> iterator = objectMapper.readValues(objectMapper.createParser(input), Object.class);
            iterator.hasNextValue(); ) {
            values.add(iterator.nextValue());
        }
        return values;
    }

    public Object resolveValue(Object rawValue, Class<?> targetClass, Type targetType) {
        if (ReflectionHelper.hasGenericTypeParameters(targetClass)) {
            return objectMapper.convertValue(rawValue, TypeFactory.defaultInstance().constructType(targetType));
        } else {
            return objectMapper.convertValue(rawValue, targetClass);
        }
    }
}
