package org.codi.lct.impl.adapter.deser;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.deser.std.StdDeserializer;
import java.io.IOException;
import java.util.ArrayDeque;
import java.util.Queue;
import org.codi.lct.ds.TreeNode;

public class TreeNodeDeserializer extends StdDeserializer<TreeNode> {

    protected TreeNodeDeserializer() {
        super(TreeNode.class);
    }

    @Override
    public TreeNode deserialize(JsonParser p, DeserializationContext ctxt) throws IOException {
        return deserializeIntArrayToListNode(p.getCodec().readValue(p, Integer[].class));
    }

    private TreeNode deserializeIntArrayToListNode(Integer[] arr) {
        if (arr == null || arr.length == 0 || arr[0] == null) {
            return null;
        }
        Queue<TreeNode> q = new ArrayDeque<>((arr.length + 1) / 2); // Max level size
        TreeNode root = new TreeNode(arr[0]);
        q.add(root);
        int ptr = 0;
        while (ptr < arr.length && !q.isEmpty()) {
            TreeNode parent = q.remove();
            if (++ptr < arr.length && arr[ptr] != null) {
                q.add(parent.left = new TreeNode(arr[ptr]));
            }
            if (++ptr < arr.length && arr[ptr] != null) {
                q.add(parent.right = new TreeNode(arr[ptr]));
            }
        }
        // Remaining elements in q are assumed to have no children & extra elements in arr are ignored
        return root;
    }
}
