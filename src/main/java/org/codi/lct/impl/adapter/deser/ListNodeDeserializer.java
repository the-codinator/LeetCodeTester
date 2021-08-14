package org.codi.lct.impl.adapter.deser;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.deser.std.StdDeserializer;
import java.io.IOException;
import org.codi.lct.ds.ListNode;

public class ListNodeDeserializer extends StdDeserializer<ListNode> {

    protected ListNodeDeserializer() {
        super(ListNode.class);
    }

    @Override
    public ListNode deserialize(JsonParser p, DeserializationContext ctxt) throws IOException, JsonProcessingException {
        return deserializeIntArrayToListNode(p.getCodec().readValue(p, int[].class));
    }

    private ListNode deserializeIntArrayToListNode(int[] arr) {
        ListNode head = null;
        for (int i = arr.length - 1; i >= 0; i--) {
            head = new ListNode(arr[i], head);
        }
        return head;
    }
}
