package org.codi.lct.data;

import java.util.List;
import lombok.Data;

@Data
public class TestCase {

    private List<Object> input;
    private Object expected;
    private Object actual;
}
