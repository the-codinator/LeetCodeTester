/*
 * Copyright 2015-2020 the original author or authors.
 *
 * All rights reserved. This program and the accompanying materials are
 * made available under the terms of the Eclipse Public License v2.0 which
 * accompanies this distribution and is available at
 *
 * https://www.eclipse.org/legal/epl-v20.html
 */

package org.codi.lct.ds.extra;

import java.util.LinkedHashMap;
import java.util.Map;

/**
 * A simple size-bounded LRU cache.
 * This is not thread-safe.
 * For optimal use, the {@code capacity} should be 1 less than a power of 2 (since this internally relies on {@link
 * LinkedHashMap})
 */
public class LruCache<K, V> {

    private final Map<K, V> map;

    /**
     * Create a new LRU Cache with given {@param capacity}
     */
    public LruCache(int capacity) {
        this.map = new LinkedHashMap<>(capacity + 1, 1, true) {
            @Override
            protected boolean removeEldestEntry(Map.Entry<K, V> eldest) {
                return size() > capacity;
            }
        };
    }

    /**
     * Get value associated with {@param key} if present in cache. Updates recent access order for eviction.
     */
    public V get(K key) {
        return map.get(key);
    }

    /**
     * Associate {@param value} with {@param key} in cache. If key already existed, its mapping is updated, recency is
     * not changed, and previous value is returned. If key did not already exist, then the new pair is marked as least
     * recent (nothing is returned).
     */
    public V put(K key, V value) {
        return map.put(key, value);
    }

    /**
     * Remove {@param key} from map and return its associated {@param value} if present.
     */
    public V remove(K key) {
        return map.remove(key);
    }
}
