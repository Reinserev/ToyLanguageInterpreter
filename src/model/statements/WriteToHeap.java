package model.statements;

import exceptions.DivideByZero;
import exceptions.FatalError;
import exceptions.InvalidPositionException;
import model.ProgramState;
import model.expressions.Expression;

public class WriteToHeap implements StatementInterface
{
    private String var;
    private Expression exp;

    public WriteToHeap(String var, Expression exp)
    {
        this.var=var;
        this.exp=exp;
    }

    public String toString()
    {
        return "wH("+var+","+exp.toString()+")";
    }

    public ProgramState execute(ProgramState state) throws InvalidPositionException, DivideByZero, FatalError
    {
        int addr = state.getSymbolTable().lookup(var);
        int v=exp.evaluate(state.getSymbolTable(),state.getHeap());
        if(state.getHeap().isDefined(addr))
            state.getHeap().update(addr,v);
        else
            throw new InvalidPositionException("Heap address does not exist.");
        return null;
    }
}
