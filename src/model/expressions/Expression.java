package model.expressions;

import exceptions.DivideByZero;
import exceptions.InvalidPositionException;
import model.util.HeapInterface;
import model.util.MyDictionaryInterface;

public abstract class Expression
{
    public abstract int evaluate(MyDictionaryInterface<String,Integer> symbolTable, HeapInterface heap) throws InvalidPositionException, DivideByZero;
    public abstract String toString();
}
