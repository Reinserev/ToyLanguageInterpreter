package model.util;

import java.io.BufferedReader;
import java.io.IOException;
import java.util.Set;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class FileTable extends MyDictionary<Integer,Pair<String,BufferedReader>> implements FileTableInterface
{
    private static Integer currentID = -1;
    private static final Lock mutex_currentID = new ReentrantLock(true);

    public Integer newID()
    {
        mutex_currentID.lock();
        currentID++;
        mutex_currentID.unlock();
        return currentID;
    }

    public boolean isFileOpened(String filename)
    {
        for (Pair<String, BufferedReader> element : values())
            if (element.getLeft().equals(filename))
                return true;
        return false;
    }

    @Override
    public String toString()
    {
        StringBuilder output= new StringBuilder("");
        Set<Integer> elems=dictionary.keySet();
        for(Integer current_key : elems)
            output.append(current_key.toString()).append(" ==> ").append(dictionary.get(current_key).getLeft()).append("\n");
        if(output.toString().equals(""))
            return "*empty*\n";
        return output.toString();
    }

    @Override
    public void clear()
    {
        mutex_currentID.lock();
        currentID=-1;
        mutex_currentID.unlock();

        //sau faci un wrapper care face in interior try-catch: faci cu un optional (a la older seminars) si culegi rezultatele (?)
        dictionary.keySet().forEach(key -> {
            try {
                dictionary.get(key).getRight().close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        });

            //dictionary.keySet().stream().forEach();

//        Set<Integer> elems=dictionary.keySet();
//        for(Integer current_key : elems)
//        {
//           try
//           {
//               dictionary.get(current_key).getRight().close();
//           }
//           catch (IOException i)
//           {
//               ;
//           }
//        }

        dictionary.clear();
    }
}
