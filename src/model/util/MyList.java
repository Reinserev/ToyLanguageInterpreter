package model.util;

import exceptions.InvalidPositionException;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Stream;

public class MyList<T> implements MyListInterface<T>
{
    public List<T> getContent() {
        return list;
    }

    public void setContent(List<T> list) {
        this.list = list;
    }

    private List<T> list = new ArrayList<>();

    public void add(T elem)
    {
        list.add(elem);
    }

    public void add(int index, T elem)
    {
        list.add(index,elem);
    }

    public T get(int index) throws InvalidPositionException
    {
        if(index<0 || index >= list.size())
            throw new InvalidPositionException("ADT List.");
        return list.get(index);
    }

    public int size() {return list.size();}

    public String toString()
    {
        StringBuilder output = new StringBuilder("");
        for(T elem : list)
            output.append(elem.toString()).append("\n");
        if(output.toString().equals(""))
            return "*empty*\n";
        return output.toString();
    }

    public void clear()
    {
        list.clear();
    }

    public Stream<T> stream()
    {
        return this.list.stream();
    }
}
