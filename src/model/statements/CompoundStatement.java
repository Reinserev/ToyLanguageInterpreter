package model.statements;
import model.ProgramState;

public class CompoundStatement implements StatementInterface
{
    private StatementInterface first, second;

    public CompoundStatement(StatementInterface f, StatementInterface s)
    {
        this.first=f;
        this.second=s;
    }

    public ProgramState execute(ProgramState state)
    {
        state.getExecutionStack().push(second);
        state.getExecutionStack().push(first);
        return null;
    }

    @Override
    public String toString()
    {
        return "("+first.toString()+';'+second.toString()+")";
    }
}
