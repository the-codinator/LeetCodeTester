package org.codi.lct.ds.extra;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;

public class UnionFindTest {

    @Test
    public void test() {
        UnionFind uf = new UnionFind(5);
        assertEquals(uf.find(4), 4);
        assertThrows(ArrayIndexOutOfBoundsException.class, () -> uf.find(10));
        assertEquals(5, uf.components());
        assertTrue(uf.union(1, 2));
        assertEquals(uf.find(1), uf.find(2));
        assertEquals(4, uf.components());
        assertFalse(uf.union(2, 1));
        assertEquals(4, uf.components());
        assertTrue(uf.union(3, 4));
        assertEquals(3, uf.components());
        assertTrue(uf.union(3, 2));
        assertEquals(2, uf.components());
        assertNotEquals(uf.find(0), uf.find(4));
        assertTrue(uf.union(0, 4));
        assertEquals(uf.find(0), uf.find(4));
        assertEquals(1, uf.components());
    }
}
