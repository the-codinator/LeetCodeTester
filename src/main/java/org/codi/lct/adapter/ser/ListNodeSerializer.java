package org.codi.lct.adapter.ser;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.ser.std.StdSerializer;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import org.codi.lct.core.LCException;
import org.codi.lct.ds.ListNode;
import org.codi.lct.impl.helper.ConfigHelper;

public class ListNodeSerializer extends StdSerializer<ListNode> {

    private static final int threshold = ConfigHelper.BASE_CONFIG.getCustomSerializationThreshold() > 0
        ? ConfigHelper.BASE_CONFIG.getCustomSerializationThreshold() : Integer.MAX_VALUE;

    protected ListNodeSerializer() {
        super(ListNode.class);
    }

    @Override
    public void serialize(ListNode value, JsonGenerator gen, SerializerProvider provider) throws IOException {
        provider.defaultSerializeValue(serializeListNodeToIntegerList(value), gen);
    }

    private List<Integer> serializeListNodeToIntegerList(ListNode head) {
        List<Integer> list = new ArrayList<>();
        while (head != null) {
            list.add(head.val);
            head = head.next;
            if (list.size() > threshold) {
                throw new LCException("More than " + threshold + " items in list... possible cycle ??");
            }
        }
        return list;
    }
}
