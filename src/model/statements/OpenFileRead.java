package model.statements;

import exceptions.FatalError;
import exceptions.InvalidPositionException;
import model.util.Pair;
import model.ProgramState;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

public class OpenFileRead implements StatementInterface
{
    private String variableFileID, filename;

    public OpenFileRead(String variableFileID, String filename)
    {
        this.variableFileID=variableFileID;
        this.filename=filename;
    }

    public String toString()
    {
        return "open("+filename+","+variableFileID+")";
    }

    public ProgramState execute(ProgramState state) throws InvalidPositionException, FatalError
    {
        BufferedReader fileReader;
        if (state.getFileTable().isFileOpened(filename))
            throw new FatalError("File "+filename+" has already been opened.");
        try
        {
            fileReader = new BufferedReader(new FileReader(filename));
        }
        catch (IOException i)
        {
            throw new FatalError("Trouble opening the file: "+filename);
        }
        Integer key = state.getFileTable().newID();
        state.getFileTable().add(key,new Pair<>(filename,fileReader));
        if(state.getSymbolTable().isDefined(variableFileID))
            state.getSymbolTable().update(variableFileID,key);
        else
            state.getSymbolTable().add(variableFileID,key);
        return null;
    }
}
