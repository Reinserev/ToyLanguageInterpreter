package model.expressions;
import exceptions.InvalidPositionException;
import model.util.HeapInterface;
import model.util.MyDictionaryInterface;

public class VariableExpression extends Expression
{
    private String ID;

    public VariableExpression(String ID)
    {
        this.ID=ID;
    }

    @Override
    public int evaluate(MyDictionaryInterface<String,Integer> symbolTable, HeapInterface heap) throws InvalidPositionException
    {
        return symbolTable.lookup(ID);
    }

    @Override
    public String toString()
    {
        return ID;
    }
}
