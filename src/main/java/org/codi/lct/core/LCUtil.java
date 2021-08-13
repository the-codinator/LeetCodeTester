package org.codi.lct.core;

import lombok.experimental.UtilityClass;
import org.codi.lct.ds.TreeNode;
import org.codi.lct.impl.helper.JacksonHelper;

/**
 * A bunch of utility functions that can be used for debugging purposes
 */
@UtilityClass
public class LCUtil {

    /**
     * Serialize / stringify an object to its json string representation using the internal ObjectMapper. Useful for
     * printing data for debugging.
     *
     * @param obj object to serialize
     * @return serialized string
     */
    public String serialize(Object obj) {
        return JacksonHelper.serialize(obj);
    }

    public <T> T deserialize(String str, Class<T> type) {
        return JacksonHelper.deserialize(str, type);
    }

    public <T> T convert(Object obj, Class<T> type) {
        return JacksonHelper.convert(obj, type);
    }

    /**
     * A utility to convert a tree to its visual representation.
     * Note: the tree is depicted left-to-right, rather than the traditional top-to-bottom (since this is easier to do)
     *
     * @param root root of the tree
     * @return visual tree representation
     */
    public String visualize(TreeNode root) {
        return visualizeTree(root, new StringBuilder(), true, new StringBuilder()).toString();
    }

    private StringBuilder visualizeTree(TreeNode node, StringBuilder prefix, boolean isTail, StringBuilder sb) {
        if (node == null) {
            return null;
        }
        // Recurse right
        visualizeTree(node.right, prefix.append(isTail ? "│   " : "    "), false, sb);
        prefix.setLength(prefix.length() - 4); // backtrack prefix
        // Print node
        sb.append(prefix).append(isTail ? "└── " : "┌── ").append(node.val).append('\n');
        // Recurse left
        visualizeTree(node.left, prefix.append(isTail ? "    " : "│   "), true, sb);
        prefix.setLength(prefix.length() - 4); // backtrack prefix
        return sb;
    }
}
