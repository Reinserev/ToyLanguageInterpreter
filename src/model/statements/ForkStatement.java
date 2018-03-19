package model.statements;

import model.ProgramState;
import model.util.MyDictionaryInterface;
import model.util.MyStack;
import model.util.MyStackInterface;

public class ForkStatement implements StatementInterface
{
    private StatementInterface stmt;

    public ForkStatement(StatementInterface stmt)
    {
        this.stmt = stmt;
    }

    public String toString()
    {
        return "fork("+stmt.toString()+")";
    }

    public ProgramState execute(ProgramState state)
    {
        MyStackInterface<StatementInterface> stack = new MyStack<>();
        stack.push(stmt);
        MyDictionaryInterface<String,Integer> copy_table = state.getSymbolTable().deepCopy();
        ProgramState prg = new ProgramState(stack,copy_table,state.getOutput(),state.getFileTable(),state.getHeap());
        prg.setID(state.getNextID());
        return prg;
    }
}
