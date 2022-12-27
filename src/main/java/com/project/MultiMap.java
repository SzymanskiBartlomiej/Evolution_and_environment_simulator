package com.project;

import java.util.*;
import java.util.stream.Collectors;

//Multi Mapa jeden klucz wiele wartości możesz dodać metody typu isEmpty itd itp ale nie wiem czy sie przydadza
import java.util.Comparator;
import java.util.TreeSet;

public class MultiMap<K, V> {
    private final Map<K, TreeSet<V>> map = new HashMap<>();
    private final Comparator<V> comparator;

    public MultiMap(Comparator<V> comparator) {
        this.comparator = comparator;
    }

    public void put(K key, V value)
    {
        if (map.get(key) == null) {
            map.put(key, new TreeSet<V>(this.comparator));
        }
        map.get(key).add(value);
    }

    public TreeSet<V> get(K key) {
        return map.get(key);
    }

    public boolean remove(K key, V value) {
        if (map.get(key) != null) { // key exists
            map.get(key).remove(value);
            if (map.get(key).isEmpty()) { //TreeSet empty
                map.remove(key);
            }
            return true;
        }

        return false;
    }

    public boolean update(K key, V value, K newKey) {
        if (remove(key, value)) {
            put(newKey, value);
            return true;
        }
        return false;
    }

    /**
     * Returns a Collection view of TreeSets of the values present in this multimap.
     * Useful for iterating over every animal.
     *
     * @return Collection view of ArrayLists of the values present in this multimap.
     */
    public Collection<V> values() {
        return map.values().stream()
                .flatMap(Collection::stream)
                .collect(Collectors.toList());
    }

    /**
     * Returns 2 values highest for a given key, highest defined by comperator function
     * Przydatne do znalezienia zwierząt które beda sie rozmnażać na dnaym bloku
     *
     * @param key - key to check
     * @return 2 values for given key which are highest
     */ //szczerze nie wiem czy nie prościej zienić ArrayList na TreeSet ale wtedy mogą być problemy z iteracją po wszystkich animalach idk
    public V getHighest(K key) {
        return get(key).last();
    }
    public Set<K> keySet() {
        return map.keySet();
    }
    public ArrayList<V> get2Highest(K key) {
        ArrayList<V> list = new ArrayList<V>(2);
        list.add(getHighest(key));
        list.add(get(key).lower(getHighest(key)));
        return list;
    }

    @Override
    public String toString() {
        String result = "";
        for(K key:keySet()){
            result += key.toString()+" ";
            result += map.get(key) + "\n";
        }
        return result;
    }
}
