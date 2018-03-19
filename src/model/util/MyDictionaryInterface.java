package model.util;
import exceptions.InvalidPositionException;

import java.util.Collection;
import java.util.Map;
import java.util.Set;

public interface MyDictionaryInterface<K,V>
{
    boolean containsKey(K key);
    boolean isDefined(K key); // containsKey clone
    V get(K key) throws InvalidPositionException;
    V lookup(K key) throws InvalidPositionException; // get clone
    boolean isEmpty();
    Set<K> keySet();
    void add(K key, V value); // put clone
    void put(K key, V value);
    void update(K key, V value) throws InvalidPositionException;
    void remove(K key) throws InvalidPositionException;
    int size();
    Collection<V> values();

    void clear();

    Map<K,V> getContent();
    void setContent(Map<K, V> content);

    MyDictionaryInterface<K,V> deepCopy();

    String toString();
}
