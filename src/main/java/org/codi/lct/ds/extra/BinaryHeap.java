package org.codi.lct.ds.extra;

import java.util.Arrays;
import java.util.NoSuchElementException;
import lombok.NonNull;

/**
 * Array based implementation of a binary min-heap.
 * <br/>
 * Additionally, here's a cool implementation for an
 * <a href="https://www.edureka.co/blog/binary-heap-in-java/">n-ary heap</a>
 */
public class BinaryHeap {

    private final int[] heap;
    private int size;

    /**
     * Create a min heap with specific capacity
     *
     * @param capacity heap capacity
     * @throws IllegalArgumentException on negative capacity
     */
    public BinaryHeap(int capacity) {
        if (capacity < 0) {
            throw new IllegalArgumentException("Heap with negative capacity " + capacity);
        }
        this.heap = new int[capacity];
        this.size = 0;
    }

    /**
     * Create a min heap from an existing array.
     * Applies heapify to ensure heap properties.
     * Creates a defensive copy of the existing array.
     *
     * @param array existing array
     * @throws NullPointerException if array is null
     */
    public BinaryHeap(@NonNull int[] array) {
        this(array, array.length);
    }

    /**
     * Create a min heap from an existing array.
     * Applies heapify to ensure heap properties.
     * The existing array is cloned and trimmed or padded with 0's to fit the capacity.
     *
     * @param array existing array
     * @param capacity heap capacity
     * @throws NullPointerException if array is null
     * @throws IllegalArgumentException if capacity is negative
     */
    public BinaryHeap(@NonNull int[] array, int capacity) {
        if (capacity < 0) {
            throw new IllegalArgumentException("Heap with negative capacity " + capacity);
        }
        this.heap = Arrays.copyOf(array, capacity);
        this.size = Math.min(array.length, capacity);
        heapifyAll();
    }

    /**
     * Insert an element into the heap while maintaining heap properties
     *
     * @param val value to insert
     * @throws IndexOutOfBoundsException if heap is full
     */
    public void add(int val) {
        insert(val);
    }

    /**
     * Insert an element into the heap while maintaining heap properties
     *
     * @param val value to insert
     * @throws IndexOutOfBoundsException if heap is full
     */
    public void insert(int val) {
        if (isFull()) {
            throw new IndexOutOfBoundsException("Heap is full! Capacity = " + heap.length);
        }
        heap[size] = val;
        heapifyUp(size++);
    }

    /**
     * Get the smallest element in the heap
     *
     * @return smallest element
     * @throws NoSuchElementException if heap is empty
     */
    public int element() {
        return findMin();
    }

    /**
     * Get the smallest element in the heap
     *
     * @return smallest element
     * @throws NoSuchElementException if heap is empty
     */
    public int findMin() {
        if (isEmpty()) {
            throw new NoSuchElementException("Heap is empty!");
        }
        return heap[0];
    }

    /**
     * Remove the smallest element from the heap
     *
     * @return smallest element in heap
     * @throws NoSuchElementException if heap is empty
     */
    public int remove() {
        return extractMin();
    }

    /**
     * Remove the smallest element from the heap
     *
     * @return smallest element in heap
     * @throws NoSuchElementException if heap is empty
     */
    public int extractMin() {
        if (size == 0) {
            throw new NoSuchElementException("Heap is empty!");
        }
        return delete(0);
    }

    /**
     * Delete element at the given heap index
     *
     * @param x heap index of element to remove
     * @throws IllegalArgumentException if heap index lies outside valid range [0, size)
     */
    public int delete(int x) {
        if (x < 0) {
            throw new IllegalArgumentException("Heap index cannot be negative");
        }
        if (x >= size) {
            throw new IllegalArgumentException("Element at index " + x + " does not exist in heap of size " + size);
        }
        int val = heap[x];
        heap[x] = heap[--size];
        heapifyDown(x);
        return val;
    }

    /**
     * Update the value of a key at given heap index
     *
     * @param x heap index
     * @param val updated value
     * @throws IllegalArgumentException if heap index lies outside valid range [0, size)
     */
    public void update(int x, int val) {
        if (x < 0) {
            throw new IllegalArgumentException("Heap index cannot be negative");
        }
        if (x >= size) {
            throw new IllegalArgumentException("Element at index " + x + " does not exist in heap of size " + size);
        }
        if (val < heap[x]) {
            decreaseKey(x, val);
        } else if (val > heap[x]) {
            increaseKey(x, val);
        }
    }

    /**
     * Decrease the value of a key at given heap index
     *
     * @param x heap index
     * @param val smaller value
     * @throws IllegalArgumentException if heap index lies outside valid range {@code [0, size)}
     * @throws IllegalArgumentException {@param val} is not smaller than existing key
     */
    private void decreaseKey(int x, int val) {
        if (x < 0) {
            throw new IllegalArgumentException("Heap index cannot be negative");
        }
        if (x >= size) {
            throw new IllegalArgumentException("Element at index " + x + " does not exist in heap of size " + size);
        }
        if (heap[x] < val) {
            throw new IllegalArgumentException(
                "Element at index " + x + " with key " + heap[x] + " is not smaller than value " + val);
        }
        heap[x] = val;
        heapifyUp(x);
    }

    /**
     * Increase the value of a key at given heap index
     *
     * @param x heap index
     * @param val larger value
     * @throws IllegalArgumentException if heap index lies outside valid range {@code [0, size)}
     * @throws IllegalArgumentException {@param val} is not smaller than existing key
     */
    private void increaseKey(int x, int val) {
        if (x < 0) {
            throw new IllegalArgumentException("Heap index cannot be negative");
        }
        if (x >= size) {
            throw new IllegalArgumentException("Element at index " + x + " does not exist in heap of size " + size);
        }
        if (heap[x] > val) {
            throw new IllegalArgumentException(
                "Element at index " + x + " with key " + heap[x] + " is not larger than value " + val);
        }
        heap[x] = val;
        heapifyDown(x);
    }

    /**
     * Get the value at given heap index
     *
     * @param x heap index
     * @return key at heap index
     * @throws IllegalArgumentException if heap index lies outside valid range {@code [0, size)}
     */
    public int get(int x) {
        if (x < 0) {
            throw new IllegalArgumentException("Heap index cannot be negative");
        }
        if (x >= size) {
            throw new IllegalArgumentException("Element at index " + x + " does not exist in heap of size " + size);
        }
        return heap[x];
    }

    /**
     * Whether the heap has no elements
     *
     * @return {@code true} if size > 0
     */
    public boolean isEmpty() {
        return size == 0;
    }

    /**
     * Whether the heap is full, and no more elements can be added
     *
     * @return {@code true} if size == capacity
     */
    public boolean isFull() {
        return size == heap.length;
    }

    /**
     * Get the number of elements currently in the heap
     *
     * @return heap size
     */
    public int size() {
        return size;
    }

    /**
     * Get the maximum number of elements the heap can hold
     *
     * @return heap capacity
     */
    public int capacity() {
        return heap.length;
    }

    /* Internal Helper Functions */

    private int parent(int i) {
        return (i - 1) / 2;
    }

    private int left(int i) {
        return 2 * i + 1;
    }

    private int right(int i) {
        return 2 * i + 2;
    }

    /**
     * Restore heap properties starting from heap index {@param x} and propagating upwards
     */
    private void heapifyUp(int x) {
        int val = heap[x];
        int p;
        while (x > 0 && val < heap[p = parent(x)]) {
            heap[x] = heap[p];
            x = p;
        }
        heap[x] = val;
    }

    /**
     * Restore heap properties starting from heap index {@param x} and propagating downwards
     */
    private void heapifyDown(int x) {
        int val = heap[x];
        int minChild;
        while ((minChild = left(x)) < size) {
            int r;
            if ((r = right(x)) < size && heap[r] < heap[minChild]) {
                minChild = r;
            }
            if (val <= heap[minChild]) {
                break;
            }
            heap[x] = heap[minChild];
            x = minChild;
        }
        heap[x] = val;
    }

    /**
     * Establishes the heap properties in the entire tree, assuming nothing is known about the order of the elements.
     * The algorithm starts from every non-leaf node and establishes heap properties in the sub-tree. This algorithm is
     * known to be O(size).
     */
    private void heapifyAll() {
        if (size > 0) {
            for (int i = parent(size - 1); i >= 0; i--) {
                heapifyDown(i);
            }
        }
    }

    /**
     * View the internal heap array (upto size)
     *
     * @return clone of underlying array
     */
    public int[] heap() {
        return Arrays.copyOf(heap, size);
    }

    @Override
    public String toString() {
        return Arrays.toString(heap());
    }
}
