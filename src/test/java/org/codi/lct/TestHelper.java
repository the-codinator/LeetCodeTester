package org.codi.lct;

import java.util.Random;
import java.util.stream.IntStream;
import lombok.experimental.UtilityClass;

@UtilityClass
public class TestHelper {

    public static final Random rng = new Random();

    public int[] randomIntArray(int len) {
        return IntStream.range(0, len).map($ -> rng.nextInt()).toArray();
    }
}
