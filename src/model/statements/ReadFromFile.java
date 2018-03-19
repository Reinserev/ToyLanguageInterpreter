package model.statements;

import exceptions.DivideByZero;
import exceptions.FatalError;
import exceptions.InvalidPositionException;
import model.ProgramState;
import model.expressions.Expression;

import java.io.BufferedReader;
import java.io.IOException;

public class ReadFromFile implements StatementInterface
{
    private Expression exp_file_id;
    private String variable_name;

    public ReadFromFile(Expression exp_file_id, String variable_name)
    {
        this.exp_file_id=exp_file_id;
        this.variable_name=variable_name;
    }

    public String toString()
    {
        return "read("+variable_name+","+exp_file_id.toString()+")";
    }

    public ProgramState execute(ProgramState state) throws InvalidPositionException, FatalError
    {
        BufferedReader reader;
        Integer val;
        try
        {
            val = exp_file_id.evaluate(state.getSymbolTable(),state.getHeap());
        }
        catch (InvalidPositionException | DivideByZero i)
        {
            throw new FatalError(i.getMessage());
        }
        try
        {
            reader = state.getFileTable().get(val).getRight();
        }
        catch (InvalidPositionException i)
        {
            throw new FatalError("No entry for file descriptor #"+val.toString()+" exists: "+i.getMessage());
        }
        String line;
        try
        {
            line = reader.readLine();
        }
        catch (IOException i)
        {
            throw new FatalError("Error reading from file: "+i.getMessage());
        }
        Integer to_add = 0;
        if(line!=null)
            to_add=Integer.parseInt(line);
        if(state.getSymbolTable().isDefined(variable_name))
            state.getSymbolTable().update(variable_name,to_add);
        else
            state.getSymbolTable().add(variable_name,to_add);
        return null;
    }
}
