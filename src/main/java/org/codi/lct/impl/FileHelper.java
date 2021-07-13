package org.codi.lct.impl;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;
import lombok.experimental.UtilityClass;
import lombok.extern.slf4j.Slf4j;
import org.codi.lct.core.LCException;

@UtilityClass
@Slf4j
public class FileHelper {

    public String defaultTestFileName(Class<?> cls) {
        // Replace all '$' and '.' in the classes fully qualified name with '/'
        return String.format("lct/%s.txt", cls.getName().replaceAll("[.$]+", "/"));
    }

    public InputStream readResource(String path) {
        return Thread.currentThread().getContextClassLoader().getResourceAsStream(path);
    }

    public Properties loadProperties(Properties properties, String file) {
        if (properties == null) {
            properties = new Properties();
        }
        InputStream stream = readResource(file);
        if (stream != null) {
            log.debug("Found properties resource: " + file);
            try {
                properties.load(stream);
            } catch (IOException e) {
                throw new LCException("Error reading properties resource: " + file, e);
            }
        }
        return properties;
    }
}
