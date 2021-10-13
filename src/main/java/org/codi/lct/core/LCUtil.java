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

    /**
     * De-serialize / parse a json string to a POJO using the internal ObjectMapper.
     *
     * @param str object to serialize
     * @param type class of type to deserialize to
     * @param <T> type parameter
     * @return de-serialized object
     */
    public <T> T deserialize(String str, Class<T> type) {
        return JacksonHelper.deserialize(str, type);
    }

    /**
     * Transform an object from one type to another using the internal ObjectMapper. This uses json as the intermediate
     * transformation language / specification.
     *
     * @param obj object to convert
     * @param type class of type to convert to
     * @param <T> type parameter
     * @return converted object
     */
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

    private StringBuilder visualizeTree(TreeNode node, StringBuilder prefix, boolean isLeft, StringBuilder sb) {
        if (node == null) {
            return null;
        }
        // Recurse right
        visualizeTree(node.right, prefix.append(isLeft ? "│   " : "    "), false, sb);
        prefix.setLength(prefix.length() - 4); // backtrack prefix
        // Print node
        sb.append(prefix).append(isLeft ? "└── " : "┌── ").append(node.val).append('\n');
        // Recurse left
        visualizeTree(node.left, prefix.append(isLeft ? "    " : "│   "), true, sb);
        prefix.setLength(prefix.length() - 4); // backtrack prefix
        return sb;
    }
}
