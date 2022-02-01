package org.codi.lct.ds.extra;

import java.util.Arrays;

/**
 * A simple implementation of the bounded Array union-find algorithm (aka disjoint set)
 */
public class UnionFind {

    private final int[] parent;
    private int components;

    public UnionFind(int size) {
        parent = new int[components = size];
        while (size-- > 0) {
            parent[size] = size;
        }
    }

    /**
     * Merge sets / components containing {@param a} and {@param b}
     *
     * @return Whether groups were actually merged
     */
    public boolean union(int a, int b) {
        if ((a = find(a)) == (b = find(b))) {
            return false;
        }
        parent[b] = a;
        components--; // We merged 2 sets into 1
        return true;
    }

    /**
     * Find the root/parent of set/component containing {@param a} while compressing path
     *
     * @return root parent of a
     */
    public int find(int a) {
        return a != parent[a] ? parent[a] = find(parent[a]) : a;
    }

    /**
     * Return the number of disjoint sets or connected components
     *
     * @return number of components
     */
    public int components() {
        return components;
        /* Legacy O(n) approach
        int count = 0;
        for (int i = 0; i < parent.length; i++) {
            if (i == parent[i]) {
                count++;
            }
        }
        return count;
        */
    }

    /**
     * Get total number of elements (irrespective of which group they are in)
     *
     * @return size of underlying array
     */
    public int numberOfElements() {
        return parent.length;
    }

    @Override
    public String toString() {
        return Arrays.toString(parent);
    }
}
