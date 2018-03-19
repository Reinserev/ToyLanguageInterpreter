package model.expressions;
import model.util.HeapInterface;
import model.util.MyDictionaryInterface;

public class ConstantExpression extends Expression
{
    private int number;

    public ConstantExpression(int nr)
    {
        this.number=nr;
    }

    @Override
    public int evaluate(MyDictionaryInterface<String,Integer> symbolTable, HeapInterface heap)
    {
        return number;
    }

    @Override
    public String toString()
    {
        return Integer.toString(number);
    }
}
