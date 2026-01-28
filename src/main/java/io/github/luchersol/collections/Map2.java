package io.github.luchersol.collections;

import java.util.AbstractMap;
import java.util.AbstractSet;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

public class Map2<K, V> extends AbstractMap<K, V> {

    private final Map<K, V> inner;

    // ----------------- Inner -----------------

    public Map<K, V> toMap() {
        return inner;
    }

    public Map<K,V> toUnmodifiableMap() {
        return Collections.unmodifiableMap(inner);
    }

    // ----------------- Constructors -----------------

    public Map2() {
        this.inner = new HashMap<>();
    }

    public Map2(Map<? extends K, ? extends V> other) {
        this.inner = new HashMap<>(other);
    }
    
    public static <K,V> Map2<K,V> of(Map<? extends K, ? extends V> map) {
        return new Map2<>(map);
    }

    public static <K, V> Map2<K, V> empty() {
        return new Map2<>();
    }

    // ----------------- Basic Operators -----------------

    @Override
    public V put(K arg0, V arg1) {
        return inner.put(arg0, arg1);
    }

    @Override
    public V remove(Object key) {
        return inner.remove(key);
    }

    @Override
    public void clear() {
        inner.clear();
    }

    @Override
    public boolean containsKey(Object key) {
        return inner.containsKey(key);
    }

    @Override
    public V get(Object key) {
        return inner.get(key);
    }

    @Override
    public int size() {
        return inner.size();
    }

    // ----------------- Controlled EntrySet -----------------
    
    @Override
    public Set<Entry<K, V>> entrySet() {
        return new AbstractSet<>() {

            @Override
            public Iterator<Entry<K, V>> iterator() {
                Iterator<Entry<K, V>> it = inner.entrySet().iterator();

                return new Iterator<>() {
                    @Override
                    public boolean hasNext() {
                        return it.hasNext();
                    }

                    @Override
                    public Entry<K, V> next() {
                        Entry<K, V> e = it.next();
                        return new SimpleEntry<>(e);
                    }

                    @Override
                    public void remove() {
                        it.remove();
                    }
                };
            }

            @Override
            public int size() {
                return inner.size();
            }
        };
    }

    // ----------------- Default constructors -----------------

    public static <K, V> Map2<K, V> of(K key0, V val0) {
        Map2<K, V> result = empty();
        result.put(key0, val0);
        return result;
    }

    public static <K, V> Map2<K, V> of(K key0, V val0, K key1, V val1) {
        Map2<K, V> result = empty();
        result.put(key0, val0);
        result.put(key1, val1);
        return result;
    }

    public static <K, V> Map2<K, V> of(K key0, V val0, K key1, V val1, K key2, V val2) {
        Map2<K, V> result = empty();
        result.put(key0, val0);
        result.put(key1, val1);
        result.put(key2, val2);
        return result;
    }

    public static <K, V> Map2<K, V> of(K key0, V val0, K key1, V val1, K key2, V val2, K key3, V val3) {
        Map2<K, V> result = empty();
        result.put(key0, val0);
        result.put(key1, val1);
        result.put(key2, val2);
        result.put(key3, val3);
        return result;
    }

    public static <K, V> Map2<K, V> of(K key0, V val0, K key1, V val1, K key2, V val2, K key3, V val3, K key4, V val4) {
        Map2<K, V> result = empty();
        result.put(key0, val0);
        result.put(key1, val1);
        result.put(key2, val2);
        result.put(key3, val3);
        result.put(key4, val4);
        return result;
    }

    public static <K, V> Map2<K, V> of(K key0, V val0, K key1, V val1, K key2, V val2, K key3, V val3, K key4, V val4, K key5, V val5) {
        Map2<K, V> result = empty();
        result.put(key0, val0);
        result.put(key1, val1);
        result.put(key2, val2);
        result.put(key3, val3);
        result.put(key4, val4);
        result.put(key5, val5);
        return result;
    }

    public static <K, V> Map2<K, V> of(K key0, V val0, K key1, V val1, K key2, V val2, K key3, V val3, K key4, V val4, K key5, V val5, K key6, V val6) {
        Map2<K, V> result = empty();
        result.put(key0, val0);
        result.put(key1, val1);
        result.put(key2, val2);
        result.put(key3, val3);
        result.put(key4, val4);
        result.put(key5, val5);
        result.put(key6, val6);
        return result;
    }

    public static <K, V> Map2<K, V> of(K key0, V val0, K key1, V val1, K key2, V val2, K key3, V val3, K key4, V val4, K key5, V val5, K key6, V val6, K key7, V val7) {
        Map2<K, V> result = empty();
        result.put(key0, val0);
        result.put(key1, val1);
        result.put(key2, val2);
        result.put(key3, val3);
        result.put(key4, val4);
        result.put(key5, val5);
        result.put(key6, val6);
        result.put(key7, val7);
        return result;
    }

    public static <K, V> Map2<K, V> of(K key0, V val0, K key1, V val1, K key2, V val2, K key3, V val3, K key4, V val4, K key5, V val5, K key6, V val6, K key7, V val7, K key8, V val8) {
        Map2<K, V> result = empty();
        result.put(key0, val0);
        result.put(key1, val1);
        result.put(key2, val2);
        result.put(key3, val3);
        result.put(key4, val4);
        result.put(key5, val5);
        result.put(key6, val6);
        result.put(key7, val7);
        result.put(key8, val8);
        return result;
    }

    public static <K, V> Map2<K, V> of(K key0, V val0, K key1, V val1, K key2, V val2, K key3, V val3, K key4, V val4, K key5, V val5, K key6, V val6, K key7, V val7, K key8, V val8, K key9, V val9) {
        Map2<K, V> result = empty();
        result.put(key0, val0);
        result.put(key1, val1);
        result.put(key2, val2);
        result.put(key3, val3);
        result.put(key4, val4);
        result.put(key5, val5);
        result.put(key6, val6);
        result.put(key7, val7);
        result.put(key8, val8);
        result.put(key9, val9);
        return result;
    }


    @SafeVarargs
    public static <K,V> Map2<K,V> ofEntries(Entry<? extends K, ? extends V>... entries) {
        Map2<K,V> result = empty();
        for (Entry<? extends K,? extends V> entry : entries) 
            result.put(entry.getKey(), entry.getValue());
        return result;
    }

}
