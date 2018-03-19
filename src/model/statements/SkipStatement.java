package model.statements;

import model.ProgramState;

public class SkipStatement implements StatementInterface
{
    public String toString()
    {
        return "skip";
    }

    public ProgramState execute(ProgramState state)
    {
        return null;
    }
}
