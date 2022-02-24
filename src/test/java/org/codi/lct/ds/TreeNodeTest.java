package org.codi.lct.ds;

import static org.junit.jupiter.api.Assertions.assertArrayEquals;
import static org.junit.jupiter.api.Assertions.assertEquals;

import org.codi.lct.core.LCUtil;
import org.junit.jupiter.api.Test;

public class TreeNodeTest {

    @Test
    public void testSerializeDeserialize() {
        /*
               1
              / \
             2   3
            /   / \
           6   7   4
                \
                 5
         */
        TreeNode $5 = new TreeNode(5);
        TreeNode $6 = new TreeNode(6);
        TreeNode $7 = new TreeNode(7, null, $5);
        TreeNode $4 = new TreeNode(4);
        TreeNode $2 = new TreeNode(2, $6, null);
        TreeNode $3 = new TreeNode(3, $7, $4);
        TreeNode $1 = new TreeNode(1, $2, $3);
        // Serialize
        String serialized = LCUtil.serialize($1);
        Integer[] expected = {1, 2, 3, 6, null, 7, 4, null, null, null, 5};
        assertArrayEquals(expected, LCUtil.deserialize(serialized, Integer[].class));
        // Deserialize
        assertEquals($1, LCUtil.deserialize(serialized, TreeNode.class));
    }
}
