package me.duanbn.snailfish.util.collection;

import java.util.LinkedHashMap;

public class LRU<K, V> extends LinkedHashMap<K, V> {

    private int capacity;

    public LRU(int capacity) {
        super(capacity, 0.75f, true);
        this.capacity = capacity;
    }

    @Override
    protected boolean removeEldestEntry(java.util.Map.Entry<K, V> eldest) {
        return size() > capacity;
    }

}
