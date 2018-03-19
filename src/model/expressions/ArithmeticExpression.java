package model.expressions;

import exceptions.DivideByZero;
import exceptions.InvalidPositionException;
import model.util.HeapInterface;
import model.util.MyDictionaryInterface;

public class ArithmeticExpression extends Expression
{
    public ArithmeticExpression(char op, Expression e1, Expression e2)
    {
        this.e1=e1;
        this.e2=e2;
        this.op=op;
    }

    private Expression e1, e2;
    private char op;

    @Override
    public int evaluate(MyDictionaryInterface<String,Integer> symbolTable, HeapInterface heap) throws InvalidPositionException, DivideByZero
    {
        switch (op)
        {
            case '+':
                return e1.evaluate(symbolTable,heap)+e2.evaluate(symbolTable,heap);
            case '-':
                return e1.evaluate(symbolTable,heap)-e2.evaluate(symbolTable,heap);
            case '*':
                return e1.evaluate(symbolTable,heap)*e2.evaluate(symbolTable,heap);
            case '/':
                if(e2.evaluate(symbolTable,heap)==0)
                    throw new DivideByZero();
                return e1.evaluate(symbolTable,heap)/e2.evaluate(symbolTable,heap);
            default:
                throw new InvalidPositionException("Invalid operator used in arithmetic operation!!");
        }
    }

    @Override
    public String toString()
    {
        switch (op)
        {
            case '+':
                return "("+e1.toString()+"+"+e2.toString()+")";
            case '-':
                return "("+e1.toString()+"-"+e2.toString()+")";
            case '*':
                return "("+e1.toString()+"*"+e2.toString()+")";
            case '/':
                return "("+e1.toString()+"/"+e2.toString()+")";
            default:
                return "Invalid arithmetic";
        }
    }
}
