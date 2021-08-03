package org.codi.lct.ds.extra;

import java.util.Arrays;

/**
 * A simple implementation of the bounded Array union-find algorithm
 */
public class UnionFind {

    private final int[] parent;

    public UnionFind(int size) {
        parent = new int[size];
        while (size-- > 0) {
            parent[size] = size;
        }
    }

    public void union(int a, int b) {
        parent[find(b)] = find(a);
    }

    public int find(int a) {
        return a != parent[a] ? parent[a] = find(parent[a]) : a;
    }

    public int components() {
        int count = 0;
        for (int i = 0; i < parent.length; i++) {
            if (i == parent[i]) {
                count++;
            }
        }
        return count;
    }

    @Override
    public String toString() {
        return Arrays.toString(parent);
    }
}
