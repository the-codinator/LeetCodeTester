package org.codi.lct.data;

import java.lang.reflect.Method;
import java.util.List;
import lombok.Builder;
import lombok.Data;

@Data
public class ExecutionData {

    private Method solution;
    private List<String> input;
    private String actual;
    private boolean complete;
    private long duration;
}
