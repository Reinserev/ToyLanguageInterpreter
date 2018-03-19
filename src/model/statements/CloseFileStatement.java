package model.statements;

import exceptions.DivideByZero;
import exceptions.FatalError;
import exceptions.InvalidPositionException;
import model.ProgramState;
import model.expressions.Expression;

import java.io.BufferedReader;
import java.io.IOException;

public class CloseFileStatement implements StatementInterface
{
    private Expression exp_file_id;

    public CloseFileStatement(Expression exp_file_id)
    {
        this.exp_file_id=exp_file_id;
    }

    public String toString()
    {
        return "close("+exp_file_id.toString()+")";
    }

    public ProgramState execute(ProgramState state) throws InvalidPositionException, DivideByZero, FatalError
    {
        BufferedReader reader;
        Integer val = exp_file_id.evaluate(state.getSymbolTable(),state.getHeap());
        try
        {
            reader = state.getFileTable().get(val).getRight();
        }
        catch (InvalidPositionException i)
        {
            throw new FatalError("No entry for "+val.toString()+" exists: "+i.getMessage());
        }
        try
        {
            reader.close();
        }
        catch (IOException e)
        {
            throw new FatalError("Error closing file: "+e.getMessage());
        }
        state.getFileTable().remove(val);
        return null;
    }
}
