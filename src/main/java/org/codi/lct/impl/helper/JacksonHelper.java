package org.codi.lct.impl.helper;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.MappingIterator;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.databind.json.JsonMapper;
import java.io.IOException;
import java.io.InputStream;
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
        .enable(SerializationFeature.INDENT_OUTPUT)
        .enable(JsonParser.Feature.AUTO_CLOSE_SOURCE)
        .enable(DeserializationFeature.FAIL_ON_TRAILING_TOKENS)
        .build();

    public List<Object> readAllValues(InputStream input) throws IOException {
        List<Object> values = new ArrayList<>();
        for (MappingIterator<Object> iterator = objectMapper.readValues(objectMapper.createParser(input), Object.class);
            iterator.hasNextValue(); ) {
            values.add(iterator.nextValue());
        }
        return values;
    }
}
