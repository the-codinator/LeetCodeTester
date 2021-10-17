package org.codi.lct.core;

import java.util.Arrays;
import java.util.Collection;
import java.util.Map;
import lombok.experimental.UtilityClass;
import org.codi.lct.ds.ListNode;
import org.codi.lct.ds.TreeNode;
import org.codi.lct.ds.extra.ArrayTree;
import org.codi.lct.ds.extra.BinaryHeap;
import org.codi.lct.ds.extra.CircularArray;
import org.codi.lct.ds.extra.Pair;
import org.codi.lct.ds.extra.UnionFind;

@UtilityClass
public class LCPrinter {

    // Printers

    public void print(Object o) {
        System.out.println(o);
    }

    public void print(String s) {
        print((Object) s);
    }

    public void print(Collection<?> collection) {
        print(collection.toString());
    }

    public void print(Map<?, ?> map) {
        print(map.toString());
    }

    public void print(int[] arr) {
        print(Arrays.toString(arr));
    }

    public void print(char[] arr) {
        print(new String(arr));
    }

    public <T> void print(T[] arr) {
        print(Arrays.toString(arr));
    }

    public void print(ArrayTree tree) {
        print(tree.toString());
    }

    public void print(CircularArray arr) {
        print(arr.toString());
    }

    public void print(BinaryHeap heap) {
        print(heap.heap());
    }

    public void print(ListNode list) {
        print(LCUtil.serialize(list));
    }

    public void print(Pair<?, ?> pair) {
        print(pair.toString());
    }

    public void print(TreeNode tree) {
        print(LCUtil.visualize(tree));
    }

    public void print(UnionFind uf) {
        print(uf.toString());
    }
}
