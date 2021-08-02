package org.codi.lct.ds;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import org.codi.lct.adapter.deser.TreeNodeDeserializer;
import org.codi.lct.adapter.ser.TreeNodeSerializer;

@NoArgsConstructor
@AllArgsConstructor
@JsonSerialize(using = TreeNodeSerializer.class)
@JsonDeserialize(using = TreeNodeDeserializer.class)
public class TreeNode {

    public int val;
    public TreeNode left;
    public TreeNode right;

    public TreeNode(int val) {
        this.val = val;
    }
}
