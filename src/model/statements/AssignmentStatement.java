package model.statements;
import exceptions.DivideByZero;
import exceptions.InvalidPositionException;
import model.ProgramState;
import model.expressions.Expression;

public class AssignmentStatement implements StatementInterface
{
    private String ID;
    private Expression exp;

    public AssignmentStatement(String ID, Expression exp)
    {
        this.ID=ID;
        this.exp=exp;
    }

    public String toString()
    {
        return ID+'='+exp.toString();
    }

    public ProgramState execute(ProgramState state) throws InvalidPositionException, DivideByZero
    {
        if (state.getSymbolTable().isDefined(ID))
            state.getSymbolTable().update(ID,exp.evaluate(state.getSymbolTable(),state.getHeap()));
        else
            state.getSymbolTable().add(ID,exp.evaluate(state.getSymbolTable(),state.getHeap()));
        return null;
    }
}
