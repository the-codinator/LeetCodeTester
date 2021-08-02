package org.codi.lct.example;

import org.codi.lct.core.LCTester;
import org.codi.lct.ds.ListNode;
import org.codi.lct.ds.TreeNode;

/**
 * Checkout {@link org.codi.lct.ds} for supported data structures
 */
public class Example04ComplexDataStructures extends LCTester {

    public ListNode inorder(TreeNode root) {
        ListNode preStart = new ListNode(0, null);
        inorder(root, preStart);
        return preStart.next;
    }

    private ListNode inorder(TreeNode root, ListNode tail) {
        if (root == null) {
            return tail;
        }
        tail = inorder(root.left, tail);
        tail = tail.next = new ListNode(root.val, null);
        return inorder(root.right, tail);
    }
}
