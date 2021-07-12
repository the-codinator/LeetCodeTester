package org.codi.lct.impl;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.databind.json.JsonMapper;
import java.util.Collection;
import java.util.List;
import java.util.Optional;
import java.util.Properties;
import lombok.Getter;
import lombok.experimental.UtilityClass;
import org.codi.lct.core.LCException;

@UtilityClass
public class Util {

    /**
     * Shared Jackson {@link ObjectMapper} instance
     */
    @Getter
    private static final ObjectMapper objectMapper = JsonMapper.builder()
        .configure(SerializationFeature.INDENT_OUTPUT, true)
        .build();

    public boolean getBooleanProperty(Properties props, String key, boolean fallback) {
        return Optional.ofNullable(props.getProperty(key)).map(Boolean::parseBoolean).orElse(fallback);
    }

    public int getIntegerProperty(Properties props, String key, int fallback) {
        return Optional.ofNullable(props.getProperty(key)).map(Integer::parseInt).orElse(fallback);
    }

    public <T> List<T> addIfPresent(List<T> list, T element) {
        if (element != null) {
            list.add(element);
        }
        return list;
    }

    public <T extends Collection<?>> T throwIfEmpty(T collection, String message) {
        if (collection == null || collection.isEmpty()) {
            throw new LCException(message);
        }
        return collection;
    }
}
