package model.expressions;

import exceptions.DivideByZero;
import exceptions.InvalidPositionException;
import model.util.HeapInterface;
import model.util.MyDictionaryInterface;

public class BooleanExpression extends Expression
{
    private Expression e1, e2;
    private String op;

    public BooleanExpression(String op, Expression e1, Expression e2)
    {
        this.e1=e1;
        this.e2=e2;
        this.op=op;
    }

    public int evaluate(MyDictionaryInterface<String,Integer> symbolTable, HeapInterface heap) throws InvalidPositionException, DivideByZero
    {
        switch (op)
        {
            case "<":
                return (e1.evaluate(symbolTable,heap)<e2.evaluate(symbolTable,heap)) ? 1 : 0;
            case "<=":
                return (e1.evaluate(symbolTable,heap)<=e2.evaluate(symbolTable,heap)) ? 1 : 0;
            case "==":
                return (e1.evaluate(symbolTable,heap)==e2.evaluate(symbolTable,heap)) ? 1 : 0;
            case "!=":
                return (e1.evaluate(symbolTable,heap)!=e2.evaluate(symbolTable,heap)) ? 1 : 0;
            case ">":
                return (e1.evaluate(symbolTable,heap)>e2.evaluate(symbolTable,heap)) ? 1 : 0;
            case ">=":
                return (e1.evaluate(symbolTable,heap)>=e2.evaluate(symbolTable,heap)) ? 1 : 0;
            default:
                throw new InvalidPositionException("Invalid operator used in arithmetic operation!!");
        }
    }

    public String toString()
    {
        switch (op)
        {
            case "<":
                return "("+e1.toString()+"<"+e2.toString()+")";
            case "<=":
                return "("+e1.toString()+"<="+e2.toString()+")";
            case "==":
                return "("+e1.toString()+"=="+e2.toString()+")";
            case "!=":
                return "("+e1.toString()+"!="+e2.toString()+")";
            case ">":
                return "("+e1.toString()+">"+e2.toString()+")";
            case ">=":
                return "("+e1.toString()+">="+e2.toString()+")";
            default:
                    return "Invalid boolean";
        }
    }
}
