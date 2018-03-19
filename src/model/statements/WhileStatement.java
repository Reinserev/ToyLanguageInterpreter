package model.statements;

import exceptions.DivideByZero;
import exceptions.InvalidPositionException;
import model.ProgramState;
import model.expressions.Expression;

public class WhileStatement implements StatementInterface
{
    private Expression exp;
    private StatementInterface stmt;

    public WhileStatement(Expression exp, StatementInterface stmt)
    {
        this.exp=exp;
        this.stmt=stmt;
    }

    public String toString()
    {
        return "while("+this.exp.toString()+")"+this.stmt.toString();
    }

    public ProgramState execute(ProgramState state) throws InvalidPositionException, DivideByZero
    {
        if(exp.evaluate(state.getSymbolTable(),state.getHeap())!=0)
        {
            state.getExecutionStack().push(this);
            state.getExecutionStack().push(stmt);
        }
        return null;

        // More complicated. May break concurrency.
        /*
        StatementInterface currentStatement = null;
        try
        {
            currentStatement = state.getExecutionStack().pop();
        }
        catch (EmptyContainerException e)
        {
            currentStatement= new SkipStatement();
        }

        if(exp.evaluate(state.getSymbolTable(),state.getHeap())!=0)
            state.getExecutionStack().push(new CompoundStatement(new CompoundStatement(stmt,this),currentStatement));
        else
            state.getExecutionStack().push(currentStatement);

        return null;
        */
    }
}
