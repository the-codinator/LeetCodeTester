package org.codi.lct.impl.adapter.ser;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.ser.std.StdSerializer;
import java.io.IOException;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;
import org.codi.lct.core.LCException;
import org.codi.lct.ds.TreeNode;
import org.codi.lct.impl.helper.ConfigHelper;

public class TreeNodeSerializer extends StdSerializer<TreeNode> {

    private static final int threshold = ConfigHelper.BASE_CONFIG.getCustomSerializationThreshold() > 0
        ? ConfigHelper.BASE_CONFIG.getCustomSerializationThreshold() : Integer.MAX_VALUE;

    protected TreeNodeSerializer() {
        super(TreeNode.class);
    }

    @Override
    public void serialize(TreeNode value, JsonGenerator gen, SerializerProvider provider) throws IOException {
        provider.defaultSerializeValue(serializeTreeNodeToIntegerList(value), gen);
    }

    private List<Integer> serializeTreeNodeToIntegerList(TreeNode head) {
        List<Integer> list = new ArrayList<>();
        // Level order traversal
        Queue<TreeNode> q = new LinkedList<>();
        q.add(head);
        while (!q.isEmpty()) {
            TreeNode node = q.remove();
            if (node == null) {
                list.add(null);
            } else {
                list.add(node.val);
                if (list.size() > threshold) {
                    throw new LCException("More than " + threshold + " items in list... possible cycle ??");
                }
                q.add(node.left);
                q.add(node.right);
            }
        }
        // Remove all trailing `null`s
        while (!list.isEmpty() && list.get(list.size() - 1) == null) {
            list.remove(list.size() - 1);
        }
        return list;
    }
}
