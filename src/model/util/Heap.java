package model.util;

import exceptions.InvalidPositionException;

import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class Heap extends MyDictionary<Integer,Integer> implements HeapInterface
{
    private static Integer currentID = 0;
    private static final Lock mutex_currentID = new ReentrantLock(true);

    public Integer newID()
    {
        mutex_currentID.lock();
        currentID++;
        mutex_currentID.unlock();
        return currentID;
    }

    @Override
    public Integer get(Integer key) throws InvalidPositionException
    {
        if(key==0)
            throw new InvalidPositionException("Requested invalid address (NULL) from heap. - ADT Dictionary");
        if(!dictionary.containsKey(key))
            throw new InvalidPositionException("ADT Dictionary.");
        return dictionary.get(key);
    }

    @Override
    public void clear()
    {
        mutex_currentID.lock();
        currentID=0;
        mutex_currentID.unlock();
        dictionary.clear();
    }
}
