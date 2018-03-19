package model.util;
import exceptions.InvalidPositionException;

import java.util.*;

public class MyDictionary<K,V> implements MyDictionaryInterface<K,V>
{
    Map<K,V> dictionary = new LinkedHashMap<>();

    public void clear()
    {
        dictionary.clear();
    }

    public boolean containsKey(K key)
    {
        return dictionary.containsKey(key);
    }

    public boolean isDefined(K key) {return containsKey(key);}

    public V get(K key) throws InvalidPositionException
    {
        if(!dictionary.containsKey(key))
            throw new InvalidPositionException("ADT Dictionary.");
        return dictionary.get(key);
    }

    public V lookup(K key) throws InvalidPositionException {return get(key);}

    public boolean isEmpty()
    {
        return dictionary.isEmpty();
    }

    public Set<K> keySet()
    {
        return dictionary.keySet();
    }

    public void put(K key, V value)
    {
        dictionary.put(key,value);
    }

    public void add(K key, V value) { put(key,value); }

    public void update(K key, V value) throws InvalidPositionException {
        if(!dictionary.containsKey(key))
            throw new InvalidPositionException("ADT Dictionary.");
        put(key,value);}

    public void remove(K key) throws InvalidPositionException
    {
        if(!dictionary.containsKey(key))
            throw new InvalidPositionException("ADT Dictionary.");
        dictionary.remove(key);
    }

    public int size()
    {
        return dictionary.size();
    }

    public Collection<V> values()
    {
        return dictionary.values();
    }

    @Override
    public String toString()
    {
        StringBuilder output=new StringBuilder("");
        for(K current_key : dictionary.keySet())
            output.append(current_key.toString()).append(" ==> ").append(dictionary.get(current_key).toString()).append("\n");
        if(output.toString().equals(""))
            return "*empty*\n";
        return output.toString();
    }

    public Map<K,V> getContent()
    {
        return dictionary;
    }

    public void setContent(Map<K,V> content)
    {
        dictionary=content;
    }

    public MyDictionaryInterface<K,V> deepCopy()
    {
        MyDictionaryInterface<K,V> my_copy = new MyDictionary<>();
        for(K current_key : dictionary.keySet())
            my_copy.put(current_key,dictionary.get(current_key));
        return my_copy;
    }
}
