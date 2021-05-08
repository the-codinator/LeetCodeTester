package org.codi.lct.client;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JavaType;
import lombok.NonNull;
import lombok.experimental.UtilityClass;
import org.codi.lct.core.LCError;
import util.Util;

@UtilityClass
public class LCParser {

    public String parse(@NonNull String content, @NonNull JavaType type) {
        try {
            return Util.getObjectMapper().readValue(content, type);
        } catch (JsonProcessingException e) {
            throw new LCError("Error constructing '" + type.getGenericSignature() + "' from '" + content + "'");
        }
    }
}
