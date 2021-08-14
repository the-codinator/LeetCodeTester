package org.codi.lct.ds;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
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
}
