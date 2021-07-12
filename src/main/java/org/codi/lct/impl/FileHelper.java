package org.codi.lct.impl;

import java.util.Collections;
import java.util.List;
import lombok.experimental.UtilityClass;
import org.codi.lct.junit.LCTester;

@UtilityClass
public class FileHelper {

    public List<String> defaultTestFileName(Class<? extends LCTester> cls) {
        // Replace all '$' and '.' in the classes fully qualified name with '/'
        return Collections.singletonList(String.format("lct/%s.txt", cls.getName().replaceAll("[.$]+", "/")));
    }
}
