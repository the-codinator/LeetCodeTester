package org.codi.lct.ds.extra;

import lombok.Getter;
import lombok.NonNull;

/**
 * A simple Trie data structure.
 *
 * Assumes all strings are english lower case words (base 26 children array).
 * Stores a unique ID (0-indexed) with each word to allow reverse mapping from the added index.
 * Duplicate words are counted.
 */
@Getter
public class TrieNode {

    /**
     * Child nodes in the Trie. Raw access is given for performance reasons. Avoid modifying this directly to avoid
     * state corruption.
     */
    private final TrieNode[] children;

    /**
     * Cached copy of the word
     */
    private String word;

    /**
     * Indicates the character key which this trie is mapped to in the parent
     */
    private char character = 0;

    /**
     * Number of times a word was added to the Trie
     */
    private int count;

    /**
     * Unique ID (0-indexed) for every word added to the Trie
     */
    private int id = -1;

    /**
     * Number of words in the current (sub) trie
     */
    private int size;

    /**
     * Create a default Trie with 26 spots for its children (maps to lower case english alphabets
     */
    public TrieNode() {
        this(26);
    }

    /**
     * Creates a Trie with specified number of children. When providing a custom number of children, you may also want
     * to override the implementation of {@link #resolveIndex(char)}
     *
     * @param numberOfChildren number of children
     */
    public TrieNode(int numberOfChildren) {
        children = new TrieNode[numberOfChildren];
    }

    /**
     * Check if the Trie has any words in it
     *
     * @return {@code true} if no words are present in the Trie, {@code false} otherwise
     */
    public boolean isEmpty() {
        return size == 0;
    }

    /**
     * Check if a word exists at the current TrieNode
     *
     * @return whether word is present
     */
    public boolean isWordPresent() {
        return word != null;
    }

    /**
     * Override this to have different number of children and indexing logic.
     * Default implementation assumes lower case english alphabets, hence the children sub-array has size 26.
     * Overriding this should appropriately manage the size the children array via {@link #TrieNode(int)}
     *
     * Example: You could consider having the implementation {@code Character.toLowerCase(ch) - 'a'} to deal with
     * ignoring case in words.
     *
     * @param ch character from the word
     * @return child index mapping to {@param ch}
     */
    public int resolveIndex(char ch) {
        return ch - 'a';
    }

    /**
     * Get a child of this TrieNode, which could potentially be null
     *
     * @param ch child character (to be resolved to index)
     * @return the child ({@code null} if absent)
     */
    public TrieNode getChild(char ch) {
        return children[resolveIndex(ch)];
    }

    /**
     * Add a word to this Trie. Always invoke this on the root of the Trie only. Invoking this on a child Trie will
     * may corrupt the size & id properties, and all further operations will have undefined behavior.
     *
     * @param word word to add
     */
    public void addWord(@NonNull CharSequence word) {
        TrieNode node = this;
        for (int i = 0; i < word.length(); i++) {
            char ch = word.charAt(i);
            int x = resolveIndex(ch);
            if (node.children[x] == null) {
                TrieNode newNode = new TrieNode(children.length);
                newNode.character = ch;
                node.children[x] = newNode;
            }
            node = node.children[x];
            node.size++;
        }
        if (node.word == null) {
            node.word = word.toString();
            node.id = size;
        }
        size++;
        node.count++;
    }

    /**
     * Get the TrieNode with the given prefix
     *
     * @param prefix prefix to query the Trie with
     * @return TrieNode at prefix (possibly null if absent)
     */
    public TrieNode findPrefix(@NonNull CharSequence prefix) {
        TrieNode node = this;
        for (int i = 0; i < prefix.length() && node != null; i++) {
            node = node.children[resolveIndex(prefix.charAt(i))];
        }
        return node;
    }

    public String toString() {
        StringBuilder sb = new StringBuilder();
        toString(sb);
        return sb.toString();
    }

    private void toString(StringBuilder sb) {
        sb.append('[').append(isWordPresent() ? '✓' : '✗');
        for (TrieNode child : children) {
            if (child != null) {
                child.toString(sb.append(',').append(child.character).append('='));
            }
        }
        sb.append(']');
    }
}
