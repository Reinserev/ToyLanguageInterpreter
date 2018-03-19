package repository;
import exceptions.FatalError;
import model.util.MyListInterface;
import model.ProgramState;
import model.util.MyList;

import java.io.*;
import java.util.List;

public class Repository implements RepositoryInterface
{
    private MyListInterface<ProgramState> states = new MyList<>();
    private String logFilePath;
    private PrintWriter logFile;

    public Repository(ProgramState program, String logFilePath) throws FatalError
    {
        this.logFilePath=logFilePath;
        this.add(program);
        try
        {
            logFile = new PrintWriter(new BufferedWriter(new FileWriter(logFilePath)));
        }
        catch (IOException i)
        {
            throw new FatalError("Error opening log file: "+i.getMessage());
        }
    }

    public void add(ProgramState prg)
    {
        states.add(prg);
    }

    public void logProgramState(ProgramState prg) throws FatalError
    {
        try
        {
            logFile = new PrintWriter(new BufferedWriter(new FileWriter(logFilePath, true)));
        }
        catch (IOException i)
        {
            throw new FatalError("Error opening log file: "+i.getMessage());
        }

        logFile.write(prg.toString());
        logFile.write("\n**********Next state**********\n\n");
        if(logFile!=null)
            logFile.close();
    }

    public void clearLogFile() throws FatalError
    {
        if(logFile!=null)
            logFile.close();
        try
        {
            logFile = new PrintWriter(new BufferedWriter(new FileWriter(logFilePath)));
            logFile.write("");
        }
        catch (IOException i)
        {
            throw new FatalError("Error clearing log file: "+i.getMessage());
        }
        if(logFile!=null)
            logFile.close();
    }

    public void logKittyException(String err) throws FatalError
    {
        try
        {
            logFile = new PrintWriter(new BufferedWriter(new FileWriter(logFilePath, true)));
        }
        catch (IOException i)
        {
            throw new FatalError("Error opening log file: "+i.getMessage());
        }
        logFile.write(err);
        if(logFile!=null)
            logFile.close();
    }

    public MyListInterface<ProgramState> getProgramList() {
        return states;
    }

    public void setProgramList(List<ProgramState> states) {
        this.states.setContent(states);
    }
}
