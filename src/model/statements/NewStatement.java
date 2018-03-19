package model.statements;

import exceptions.DivideByZero;
import exceptions.InvalidPositionException;
import model.ProgramState;
import model.expressions.Expression;

public class NewStatement implements StatementInterface {

    private String var;
    private Expression exp;

    public NewStatement(String var, Expression exp)
    {
        this.var=var;
        this.exp=exp;
    }

    public String toString()
    {
        return "new("+var+","+exp.toString()+")";
    }

    public ProgramState execute(ProgramState state) throws InvalidPositionException, DivideByZero
    {
        int v = exp.evaluate(state.getSymbolTable(),state.getHeap());
        int location = state.getHeap().newID();
        state.getHeap().add(location,v);
        if(state.getSymbolTable().isDefined(var))
            state.getSymbolTable().update(var,location);
        else
            state.getSymbolTable().add(var,location);
        return null;
    }
}
