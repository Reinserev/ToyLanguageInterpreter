package model.util;
import exceptions.EmptyContainerException;

import java.util.Stack;

public interface MyStackInterface<T>
{
    Stack<T> getContent();

    boolean empty();

    T pop() throws EmptyContainerException;
    void push(T v);

    void clear();

    String toString();
}
