package model.expressions;

import exceptions.InvalidPositionException;
import model.util.HeapInterface;
import model.util.MyDictionaryInterface;

public class ReadFromHeap extends Expression
{
    private String var;

    public ReadFromHeap(String var)
    {
        this.var=var;
    }

    public String toString()
    {
        return "rH("+var+")";
    }

    public int evaluate(MyDictionaryInterface<String,Integer> symbolTable, HeapInterface heap) throws InvalidPositionException
    {
        if(!symbolTable.isDefined(var))
            throw new InvalidPositionException("Variable name does not exist.");
        int location = symbolTable.lookup(var);
        if(!heap.isDefined(location))
            throw new InvalidPositionException("Heap location does not exist.");
        return heap.lookup(location);
    }
}
