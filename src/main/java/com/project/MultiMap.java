package com.project;

import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import java.util.stream.Stream;

//Multi Mapa jeden klucz wiele wartości możesz dodać metody typu isEmpty itd itp ale nie wiem czy sie przydadza
public class MultiMap<K,V> {
    private final Map<K,ArrayList<V>> map = new HashMap<>();
    public void put(K key, V value)
    {
        if (map.get(key) == null) {
            map.put(key, new ArrayList<V>());
        }
        map.get(key).add(value);
    }
    public ArrayList<V> get(K key) {
        return map.get(key);
    }
    public boolean remove(K key, V value)
    {
        if (map.get(key) != null) { // key exists
            map.get(key).remove(value);
            if (map.get(key).isEmpty()){ //ArrayList empty
                map.remove(key);
            }
            return true;
        }

        return false;
    }
    /**
     * Returns a Collection view of ArrayLists of the values present in this multimap.
     * Useful for iterating over every animal.
     * @return Collection view of ArrayLists of the values present in this multimap.
     */
    public Collection<V> values() {
        Collection<V> result = map.values().stream()
                .flatMap(Collection::stream)
                .collect(Collectors.toList());
        return result;
    }
    /**
     * Returns 2 values highest for a given key, highest defined by comperator function
     * Przydatne do znalezienia zwierząt które beda sie rozmnażać na dnaym bloku
     * @param key - key to check
     * @return 2 values for given key which are highest
     */ //szczerze nie wiem czy nie prościej zienić ArrayList na TreeSet ale wtedy mogą być problemy z iteracją po wszystkich animalach idk
    public void get2Highest(K key){
    }
}
