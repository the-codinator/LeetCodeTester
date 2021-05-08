package org.codi.lct.data;

import java.lang.reflect.Field;
import java.util.List;
import lombok.Builder;
import lombok.NonNull;
import lombok.Value;

@Value
@Builder
public class ExtensionData {

    @NonNull Class<?> classUnderTest;
    @NonNull SolutionData solutionData;
    @NonNull List<Field> inputFields;
    @NonNull Field expectedValueField;
}
