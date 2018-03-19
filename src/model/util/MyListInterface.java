package model.util;
import exceptions.InvalidPositionException;

import java.util.List;
import java.util.stream.Stream;

public interface MyListInterface<T>
{
    void add(T elem);
    void add(int index, T elem);
    T get(int index) throws InvalidPositionException;
    int size();

    void clear();

    String toString();

    Stream<T> stream();

    List<T> getContent();

    void setContent(List<T> list);
}
