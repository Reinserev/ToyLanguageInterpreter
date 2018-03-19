package model.util;

import exceptions.EmptyContainerException;
import java.util.Stack;

public class MyStack<T> implements MyStackInterface<T>
{
    private Stack<T> stack = new Stack<>();

    public boolean empty()
    {
        return stack.empty();
    }

    public T pop() throws EmptyContainerException
    {
        if(stack.empty())
            throw new EmptyContainerException("ADT Stack.");
        return stack.pop();
    }

    public void push(T v)
    {
        stack.push(v);
    }

    public String toString()
    {
        StringBuilder output = new StringBuilder("");
        for (T current : stack)
            output.insert(0,current.toString()+"\n");
        if(output.toString().equals(""))
            return "*empty*\n";
        return output.toString();
    }

    public void clear()
    {
        stack.clear();
    }

    public Stack<T> getContent()
    {
        return stack;
    }
}
