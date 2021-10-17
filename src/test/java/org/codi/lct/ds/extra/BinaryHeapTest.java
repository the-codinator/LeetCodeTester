package org.codi.lct.ds.extra;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.List;
import java.util.PriorityQueue;
import java.util.Random;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import org.apache.commons.lang3.ArrayUtils;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;

public class BinaryHeapTest {

    private static final Random rng = new Random();

    public static List<int[]> testCaseProvider() {
        int[] noDup = {1, 2, 3, 4, 5, 6, 7, 8, 9, 0};
        int[] withDup = {-1, 1, -2, 2, -3, 3, 3, 0, 0, 0, -10, 2000, Integer.MIN_VALUE, Integer.MAX_VALUE, 1024, -999};
        return List.of(shuffle(noDup), shuffle(noDup), shuffle(noDup), shuffle(withDup), shuffle(withDup),
            shuffle(withDup));
    }

    private static int[] shuffle(int[] arr) {
        arr = ArrayUtils.clone(arr);
        ArrayUtils.shuffle(arr, rng);
        return arr;
    }

    @ParameterizedTest
    @MethodSource("testCaseProvider")
    public void testStandardProperties(int[] array) {
        PriorityQueue<Integer> pq;
        BinaryHeap heap;
        if (array.length == 0) {
            pq = new PriorityQueue<>();
            heap = new BinaryHeap(100);
        } else {
            pq = new PriorityQueue<>(IntStream.of(array).boxed().collect(Collectors.toList()));
            heap = new BinaryHeap(array);
        }
        while (pq.size() > 5) {
            assertEquals(pq.remove(), heap.remove());
        }
        assertEquals(pq.size(), heap.size());
        assertFalse(heap.isFull());
        assertFalse(heap.isEmpty());
        while (heap.size() < heap.capacity()) {
            int x = rng.nextInt(100) - 50; // Numbers from [-50,50]
            pq.add(x);
            heap.add(x);
        }
        assertEquals(pq.size(), heap.size());
        assertTrue(heap.isFull());
        assertFalse(heap.isEmpty());
        while (!heap.isEmpty()) {
            assertEquals(pq.remove(), heap.remove());
        }
        assertEquals(pq.size(), heap.size());
        assertFalse(heap.isFull());
        assertTrue(heap.isEmpty());
    }

    @Test
    public void testUpdateAndSort() {
        BinaryHeap heap = new BinaryHeap(100);
        for (int i = rng.nextInt(50) + 10; i >= 0; i--) {
            heap.add(rng.nextInt(600) - 50);
        }
        heap.update(3, Integer.MIN_VALUE);
        heap.update(3, Integer.MAX_VALUE);
        int prev = heap.remove();
        assertEquals(Integer.MIN_VALUE, prev);
        while (heap.size() > 1) {
            int curr = heap.element();
            assertNotEquals(Integer.MIN_VALUE, curr);
            assertNotEquals(Integer.MAX_VALUE, curr);
            assertEquals(curr, heap.remove());
            assertTrue(curr >= prev);
            prev = curr;
        }
        assertEquals(Integer.MAX_VALUE, heap.remove());
    }
}
