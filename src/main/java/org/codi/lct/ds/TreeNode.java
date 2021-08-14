package org.codi.lct.ds;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import org.codi.lct.impl.adapter.deser.TreeNodeDeserializer;
import org.codi.lct.impl.adapter.ser.TreeNodeSerializer;

@NoArgsConstructor
@AllArgsConstructor
@JsonSerialize(using = TreeNodeSerializer.class)
@JsonDeserialize(using = TreeNodeDeserializer.class)
@EqualsAndHashCode
public class TreeNode {

    public int val;
    public TreeNode left;
    public TreeNode right;

    public TreeNode(int val) {
        this.val = val;
    }
}
