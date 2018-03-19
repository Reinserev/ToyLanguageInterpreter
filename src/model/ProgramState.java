package model;

import exceptions.DivideByZero;
import exceptions.EmptyContainerException;
import exceptions.FatalError;
import exceptions.InvalidPositionException;
import model.statements.StatementInterface;
import model.util.*;

import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class ProgramState
{
    private MyStackInterface<StatementInterface> executionStack;
    private MyDictionaryInterface<String,Integer> symbolTable;
    private MyListInterface<Integer> output;
    private FileTableInterface fileTable;
    private HeapInterface heap;
    private Integer ID = 1;
    private static Integer currentID = 1;
    private static final Lock mutex_currentID = new ReentrantLock(true);

    public ProgramState(MyStackInterface<StatementInterface> exe_stack, MyDictionaryInterface<String,Integer> sym_tbl, MyListInterface<Integer> out, FileTableInterface FileTable, HeapInterface heap)
    {
        this.executionStack=exe_stack;
        this.symbolTable=sym_tbl;
        this.output=out;
        this.fileTable=FileTable;
        this.heap=heap;
    }

    public void clear()
    {
        executionStack.clear();
        symbolTable.clear();
        output.clear();
        fileTable.clear();
    }

    public boolean isNotCompleted()
    {
        return !this.executionStack.empty();
    }

    public MyStackInterface<StatementInterface> getExecutionStack() {
        return executionStack;
    }

    public MyDictionaryInterface<String, Integer> getSymbolTable() {
        return symbolTable;
    }

    public MyListInterface<Integer> getOutput() {
        return output;
    }

    public FileTableInterface getFileTable() {
        return fileTable;
    }

    public HeapInterface getHeap() {
        return heap;
    }

    public void setHeap(HeapInterface heap) {
        this.heap = heap;
    }

    public ProgramState oneStep() throws FatalError, EmptyContainerException
    {
        if(this.executionStack.empty())
            throw new EmptyContainerException("Cannot continue execution: program is empty.");
        try
        {
            return this.executionStack.pop().execute(this);
        }
        catch (InvalidPositionException i)
        {
            throw new FatalError(i.getMessage() + "\nAn attempt to use a variable that doesen't exist has been made.");
        }
        catch (DivideByZero d)
        {
            throw new FatalError("\nAn attempt to divide by zero has been made.");
        }
    }

    public Integer getID() {
        return ID;
    }

    public void setID(Integer ID) {
        this.ID = ID;
    }

    public Integer getNextID()
    {
        mutex_currentID.lock();
        currentID++;
        mutex_currentID.unlock();
        return currentID;
    }

    public void resetNextID()
    {
        mutex_currentID.lock();
        currentID = 1;
        mutex_currentID.unlock();
    }

    public String toString()
    {
        return "Thread ID:\n"+this.ID.toString()+"\n\nExecution Stack:\n"+this.executionStack.toString()+"\nSymbol table:\n"+this.symbolTable.toString()+"\nOutput list:\n"+this.output.toString()+"\nFile table:\n"+this.fileTable.toString()+"\nHeap:\n"+this.heap.toString();
    }
}
