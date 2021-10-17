package org.codi.lct.ds.extra;

import java.util.Arrays;
import java.util.NoSuchElementException;

/**
 * Implement a bounded circular int array
 *
 * Generic version of this is {@link java.util.ArrayDeque}, except that you can't get by index
 */
public class CircularArray {

    private final int[] arr;
    private int offset;
    private int size;

    /**
     * Create new circular array of size {@param capacity}
     */
    public CircularArray(int capacity) {
        this.arr = new int[capacity];
        offset = 0;
        size = 0;
    }

    /**
     * Get item at i-th circular index
     *
     * @return element at circular index {@param i}
     * @throws ArrayIndexOutOfBoundsException on bad index
     */
    public int get(int i) {
        rangeCheck(i);
        return arr[index(i)];
    }

    /**
     * Set element at circular index {@param i} to value {@param x}
     *
     * @return array size
     * @throws ArrayIndexOutOfBoundsException on bad index
     */
    public int set(int i, int x) {
        rangeCheck(i);
        arr[index(i)] = x;
        return size;
    }

    /**
     * Set element at circular index {@param i} to value {@param x}
     * Set all unassigned indexes (from index=size to index=i) as 0
     *
     * @return array size
     * @throws ArrayIndexOutOfBoundsException on index outside capacity
     */
    public int setForcibly(int i, int x) {
        if (i < size) {
            return set(i, x);
        } else if (i >= arr.length) {
            throw new ArrayIndexOutOfBoundsException(
                "Invalid array index: " + i + " for Circular Array of capacity: " + arr.length);
        } else {
            while (size < i) {
                add(0);
            }
            return add(x);
        }
    }

    /**
     * Append {@param x} to the circular array
     * Similar to {@link java.util.Queue#offer} or {@link java.util.Stack#push}
     *
     * @return size
     * @throws ArrayIndexOutOfBoundsException if circular array is at capacity
     */
    public int add(int x) {
        if (size >= arr.length) {
            throw new ArrayIndexOutOfBoundsException("Circular Array is at capacity: " + arr.length);
        }
        arr[index(size)] = x;
        return ++size;
    }

    /**
     * Remove the first element
     *
     * @return first element
     * @throws NoSuchElementException if circular array is empty
     */
    public int removeFirst() {
        if (size == 0) {
            throw new NoSuchElementException("Circular Array is empty");
        }
        int element = arr[offset];
        if (++offset == arr.length) {
            offset = 0;
        }
        size--;
        return element;
    }

    /**
     * Remove the last element
     *
     * @return last element
     * @throws NoSuchElementException if circular array is empty
     */
    public int removeLast() {
        if (size == 0) {
            throw new NoSuchElementException("Circular Array is empty");
        }
        return arr[index(size--)];
    }

    /**
     * Get current size of the circular array
     *
     * @return current size
     */
    public int size() {
        return size;
    }

    /**
     * Get max size capacity of the circular array
     *
     * @return capacity
     */
    public int capacity() {
        return arr.length;
    }

    /**
     * Delete all elements in the circular array
     */
    public void clear() {
        offset = size = 0;
    }

    /**
     * Is empty check
     *
     * @return {@code true} if circular array has no elements. {@code false} otherwise
     */
    public boolean isEmpty() {
        return size == 0;
    }

    /**
     * Copy to standard indexing array
     *
     * @return a regular int array (shallow copy) containing all the elements at the matching index
     */
    public int[] toArray() {
        int[] copy = new int[size];
        int len = Math.min(size, arr.length - offset);
        System.arraycopy(arr, offset, copy, 0, len);
        System.arraycopy(arr, 0, copy, len, size - len);
        return copy;
    }

    private int index(int i) {
        i += offset;
        if (i >= arr.length) {
            i -= arr.length;
        }
        return i;
    }

    private void rangeCheck(int i) {
        if (i < 0 || i >= size) {
            throw new ArrayIndexOutOfBoundsException(
                "Invalid array index: " + i + " for Circular Array of size: " + size);
        }
    }

    /**
     * Circular array equivalent of{@link Arrays#toString}
     *
     * @return String representation of the circular array
     */
    @Override
    public String toString() {
        return Arrays.toString(toArray());
    }
}
