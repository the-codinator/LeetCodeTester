package org.codi.lct.ds.extra;

import java.util.Arrays;
import java.util.TreeMap;

/**
 * A Full Binary Tree implementation backed by an array.
 * A "full" tree is one where every node has NONE (leaf nodes) or ALL children (internal nodes).
 * Also supports Complete Binary Trees.
 * A "complete" tree is one where the leaf nodes are present only on the final level.
 * This is useful to implement a heap or segment tree.
 *
 * Note: this implementation is only a wrapper / utility, and it does not perform boundary checks
 */
public class ArrayTree {

    private final int base;
    private final int[] tree;

    /**
     * Creates an empty ArrayTree
     * The backing array has a length of 1 less than twice the leaf node count (sufficient to build the binary tree upto
     * the root with "count" leaf nodes)
     * If the tree is complete, the backing array length is also rounded up to 1 less than the closest larger (or equal)
     * power of 2
     *
     * @param count number of leaf / terminal elements
     * @param complete if tree should be complete (padded with zeros)
     */
    public ArrayTree(int count, boolean complete) {
        base = (complete ? 1 << (int) Math.ceil(Math.log(count) / Math.log(2)) : count) - 1;
        tree = new int[2 * base + 1];
    }

    /**
     * Get number of internal nodes, which is also the index of the first leaf node
     *
     * @return internal node count
     */
    public int base() {
        return base;
    }

    /**
     * Get internal backing array of the tree for direct manipulations
     *
     * @return internal array
     */
    public int[] array() {
        return tree;
    }

    /**
     * Get value in node at tree-index {@param i}
     *
     * @return value for tree-index
     */
    public int get(int i) {
        return tree[i];
    }

    /**
     * Set value in node at tree-index {@param i}
     *
     * @param i tree index
     * @param v new value
     */
    public void set(int i, int v) {
        tree[i] = v;
    }

    /**
     * Get index of parent of node at tree-index {@param i}
     *
     * @param i tree-index
     * @return tree-index of parent
     */
    public static int parent(int i) {
        return (i - 1) / 2;
    }

    /**
     * Get index of left child of node at tree-index {@param i}
     *
     * @param i tree-index
     * @return tree-index of left child
     */
    public static int left(int i) {
        return 2 * i + 1;
    }

    /**
     * Get index of right child of node at tree-index {@param i}
     *
     * @param i tree-index
     * @return tree-index of right child
     */
    public static int right(int i) {
        return 2 * i + 2;
    }

    /**
     * Check whether node at tree-index {@param i} is its parent's left child
     *
     * @param i tree-index
     * @return true if "i" is its parent's left child
     */
    public static boolean isLeft(int i) {
        return i % 2 == 1;
    }

    /**
     * Check whether node at tree-index {@param i} is its parent's right child
     * Note: root node (i = 0) is considered right child (since edge case is not checked)
     *
     * @param i tree-index
     * @return true if "i" is its parent's right child
     */
    public static boolean isRight(int i) {
        return i % 2 == 0;
    }

    /**
     * Get node in tree immediately to left of node with tree-index {@param i} (aka right neighbor)
     * Similar to {@link TreeMap#higherKey} (we don't need to actually traverse the tree in array mode)
     * Performs wrap around since no edge case check is performed
     *
     * @param i tree-index
     * @return tree-index of right sibling
     */
    public static int next(int i) {
        return i + 1;
    }

    /**
     * Get node in tree immediately to left of node with tree-index {@param i} (aka left neighbor)
     * Similar to {@link TreeMap#lowerKey} (we don't need to actually traverse the tree in array mode)
     * Performs wrap around since no edge case check is performed
     *
     * @param i tree-index
     * @return tree-index of left sibling
     */
    public static int previous(int i) {
        return i - 1;
    }

    @Override
    public String toString() {
        return Arrays.toString(tree);
    }
}
