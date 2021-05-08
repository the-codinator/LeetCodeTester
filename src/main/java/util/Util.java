package util;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.databind.json.JsonMapper;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.SequenceInputStream;
import java.net.URL;
import java.util.Arrays;
import java.util.Enumeration;
import lombok.Getter;
import lombok.experimental.UtilityClass;
import org.codi.lct.core.LCError;

@UtilityClass
public class Util {

    /**
     * Shared Jackson {@link ObjectMapper} instance
     */
    @Getter
    private final ObjectMapper objectMapper = JsonMapper.builder()
        .configure(SerializationFeature.INDENT_OUTPUT, true)
        .build();

    /**
     * Non-null check
     */
    public <T> T requireNonNull(T value, String name) {
        if (value == null) {
            throw new LCError(name + " cannot not be null");
        }
        return value;
    }

    /**
     * Read contents of {@param fileName}
     */
    public String loadFileContents(String fileName) {
        if (fileName == null || fileName.isBlank()) {
            throw new LCError("File name null/empty");
        }
        Enumeration<URL> resources;
        try {
            resources = Thread.currentThread().getContextClassLoader().getResources(fileName);
        } catch (IOException e) {
            throw new LCError("Error reading resources for " + fileName, e);
        }
        if (!resources.hasMoreElements()) {
            throw new LCError("No resource found for " + fileName);
        }
        URL url = resources.nextElement();
        if (resources.hasMoreElements()) {
            throw new LCError("Multiple resources found for " + fileName);
        }
        InputStream inputStream;
        try {
            inputStream = url.openStream();
        } catch (IOException e) {
            throw new LCError("Error opening stream for resource " + url, e);
        }
        if (inputStream == null) {
            throw new LCError("Resource input stream is null for " + url);
        }
        try {
            return new String(inputStream.readAllBytes());
        } catch (IOException e) {
            throw new LCError("Error reading contents from resource stream for " + url, e);
        }
    }

    /**
     * Create InputStream from Strings
     */
    public InputStream createStream(String... lines) {
        return new SequenceInputStream(new IteratorEnumeration<>(
            Arrays.stream(lines).map(s -> new ByteArrayInputStream(s.getBytes())).iterator()));
    }
}
