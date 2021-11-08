package org.codi.lct.ds.extra;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.ArrayList;
import java.util.List;
import org.junit.jupiter.api.Test;

public class TrieNodeTest {

    @Test
    public void testTrie() {
        TrieNode trie = new TrieNode();

        // Basic checks
        assertTrue(trie.isEmpty());
        trie.addWord("abc");
        assertFalse(trie.isEmpty());
        assertEquals(1, trie.getSize());

        // Adding duplicate word
        trie.addWord("abc");
        assertEquals(2, trie.getSize());
        assertEquals(0, trie.findPrefix("abc").getId());

        // More words
        List<String> words = List.of("abrakadabra", "ball", "abba");
        words.forEach(trie::addWord);
        trie.addWord("abc");
        assertEquals(6, trie.getSize());

        // Find prefix & existance test
        TrieNode node = trie.findPrefix("ab");
        assertNotNull(node);
        assertFalse(node.isWordPresent());
        assertEquals(5, node.getSize());
        assertNull(node.getChild('a'));
        assertNotNull(node.getChild('b'));
        assertNotNull(node.getChild('b').getChild('a'));
        assertTrue(node.getChild('b').getChild('a').isWordPresent());
        assertEquals(node.getChild('b').getChild('a'), trie.findPrefix("abba"));

        // Count
        assertEquals(0, trie.findPrefix("").getCount());
        assertEquals(0, trie.findPrefix("a").getCount());
        assertEquals(0, trie.findPrefix("ab").getCount());
        assertEquals(3, trie.findPrefix("abc").getCount());

        // Size
        assertEquals(6, trie.findPrefix("").getSize());
        assertEquals(5, trie.findPrefix("a").getSize());
        assertEquals(5, trie.findPrefix("ab").getSize());
        assertEquals(3, trie.findPrefix("abc").getSize());

        // Non-existent search
        assertNull(trie.findPrefix("abcd"));

        // ID check
        assertEquals(3, trie.findPrefix("ball").getId());
        words.forEach(word -> assertEquals(words.indexOf(word) + 2, trie.findPrefix(word).getId()));

        // Autocomplete use-case implementation & test
        List<String> wordsStartingWithAB = new ArrayList<>();
        autocomplete(trie.findPrefix("ab"), wordsStartingWithAB, 2);
        assertEquals(List.of("abba", "abc"), wordsStartingWithAB);
    }

    private void autocomplete(TrieNode node, List<String> collector, int limit) {
        if (node == null || collector.size() == limit) {
            return;
        }
        if (node.isWordPresent()) {
            collector.add(node.getWord());
        }
        TrieNode[] children = node.getChildren();
        for (TrieNode child : children) {
            if (child != null) {
                autocomplete(child, collector, limit);
            }
            if (collector.size() == limit) {
                return;
            }
        }
    }
}
