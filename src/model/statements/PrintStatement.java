package model.statements;
import exceptions.DivideByZero;
import exceptions.InvalidPositionException;
import model.ProgramState;
import model.expressions.Expression;

public class PrintStatement implements StatementInterface
{
    private Expression exp;

    public PrintStatement(Expression exp)
    {
        this.exp=exp;
    }

    public String toString()
    {
        return "print("+exp.toString()+")";
    }

    public ProgramState execute(ProgramState state)  throws InvalidPositionException, DivideByZero
    {
        state.getOutput().add(exp.evaluate(state.getSymbolTable(),state.getHeap()));
        return null;
    }
}
