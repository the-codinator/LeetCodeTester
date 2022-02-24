package org.codi.lct.ds;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import java.util.Iterator;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import org.codi.lct.impl.adapter.deser.ListNodeDeserializer;
import org.codi.lct.impl.adapter.ser.ListNodeSerializer;

@NoArgsConstructor
@AllArgsConstructor
@JsonSerialize(using = ListNodeSerializer.class)
@JsonDeserialize(using = ListNodeDeserializer.class)
@EqualsAndHashCode
public class ListNode {

    public int val;
    public ListNode next;

    public ListNode(int val) {
        this.val = val;
    }

    @AllArgsConstructor(access = AccessLevel.PRIVATE)
    public static class ListNodeIterator implements Iterator<Integer> {

        private ListNode node;

        @Override
        public boolean hasNext() {
            return node != null;
        }

        @Override
        public Integer next() {
            int val = node.val;
            node = node.next;
            return val;
        }

        public static Iterable<Integer> iterable(ListNode node) {
            // ListNode does not implement java.util.Iterable to conform with LeetCode's implementation
            return () -> new ListNodeIterator(node);
        }
    }
}
