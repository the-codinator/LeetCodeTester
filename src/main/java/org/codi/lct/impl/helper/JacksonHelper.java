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
import java.util.Collections;
import java.util.List;
import lombok.Getter;
import lombok.experimental.UtilityClass;
import org.codi.lct.core.LCException;

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

    public List<Object> readAllValues(String input) {
        try {
            return input == null ? Collections.emptyList()
                : readAllValues(objectMapper.readValues(objectMapper.createParser(input), Object.class));
        } catch (IOException e) {
            throw new LCException("Error reading values from input string", e);
        }
    }

    public List<Object> readAllValues(InputStream input) {
        try {
            return readAllValues(objectMapper.readValues(objectMapper.createParser(input), Object.class));
        } catch (IOException e) {
            throw new LCException("Error reading values from input stream", e);
        }
    }

    public List<Object> readAllValues(MappingIterator<Object> iterator) {
        try {
            List<Object> values = new ArrayList<>();
            iterator.forEachRemaining(values::add);
            return values;
        } catch (Exception e) {
            throw new LCException("Error reading values from input stream", e);
        }
    }

    public Object resolveValue(Object rawValue, Class<?> targetClass, Type targetType) {
        if (ReflectionHelper.hasGenericTypeParameters(targetClass)) {
            return objectMapper.convertValue(rawValue, TypeFactory.defaultInstance().constructType(targetType));
        } else {
            return objectMapper.convertValue(rawValue, targetClass);
        }
    }
}
