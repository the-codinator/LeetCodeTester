package org.codi.lct.ds;

import static org.junit.jupiter.api.Assertions.assertArrayEquals;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import org.codi.lct.TestHelper;
import org.codi.lct.core.LCUtil;
import org.junit.jupiter.api.Test;

public class ListNodeTest {

    @Test
    public void testIterator() {
        // Setup random ListNode
        int len = TestHelper.rng.nextInt(100) + 10;
        int[] arr = TestHelper.randomIntArray(len);
        ListNode head = null;
        for (int i = len - 1; i >= 0; i--) {
            head = new ListNode(arr[i], head);
        }
        // Read ListNode
        List<Integer> list = new ArrayList<>();
        ListNode.ListNodeIterator.iterable(head).forEach(list::add);
        // Validate
        assertEquals(Arrays.toString(arr), list.toString());
    }

    @Test
    public void testSerialize() {
        // Setup random ListNode
        int len = TestHelper.rng.nextInt(100) + 10;
        int[] arr = TestHelper.randomIntArray(len);
        ListNode head = null;
        for (int i = len - 1; i >= 0; i--) {
            head = new ListNode(arr[i], head);
        }
        // Validate
        assertArrayEquals(arr, LCUtil.deserialize(LCUtil.serialize(head), int[].class));
    }

    @Test
    public void testDeserialize() {
        // Setup random ListNode
        int len = TestHelper.rng.nextInt(100) + 10;
        int[] arr = TestHelper.randomIntArray(len);
        ListNode head = LCUtil.deserialize(LCUtil.serialize(arr), ListNode.class);
        // Validate
        for (int x : arr) {
            assertNotNull(head);
            assertEquals(x, head.val);
            head = head.next;
        }
        assertNull(head);
    }
}
