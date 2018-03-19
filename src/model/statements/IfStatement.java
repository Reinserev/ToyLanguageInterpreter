package model.statements;
import exceptions.DivideByZero;
import exceptions.InvalidPositionException;
import model.ProgramState;
import model.expressions.Expression;

public class IfStatement implements StatementInterface
{
    private Expression exp;
    private StatementInterface thenStatement, elseStatement;

    public IfStatement(Expression exp, StatementInterface thenStatement, StatementInterface elseStatement)
    {
        this.exp=exp;
        this.thenStatement=thenStatement;
        this.elseStatement=elseStatement;
    }

    public String toString()
    {
        return "IF("+exp.toString()+") THEN("+thenStatement.toString()+") ELSE("+elseStatement.toString()+")";
    }

    public ProgramState execute(ProgramState state) throws InvalidPositionException, DivideByZero
    {
        if(exp.evaluate(state.getSymbolTable(),state.getHeap())!=0)
            state.getExecutionStack().push(thenStatement);
        else
            state.getExecutionStack().push(elseStatement);
        return null;

        //More complicated way; may break concurrency.
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
            state.getExecutionStack().push(new CompoundStatement(thenStatement,currentStatement));
        else
            state.getExecutionStack().push(new CompoundStatement(elseStatement,currentStatement));

        return null;
        */
    }
}
