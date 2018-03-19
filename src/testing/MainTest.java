package testing;

import controller.Controller;
import exceptions.ControllerException;
import exceptions.FatalError;
import exceptions.InvalidPositionException;
import model.ProgramState;
import model.expressions.*;
import model.statements.*;
import model.util.*;
import org.junit.Before;
import org.junit.Test;
import repository.Repository;
import repository.RepositoryInterface;

public class MainTest
{
    // Use beforeclass to instantiate :)
    // Use clear method from repository to clear the stuff

    private MyDictionaryInterface<String,Integer> SymbolTable;
    private MyListInterface<Integer> OutputList;
    private FileTableInterface file_table;
    private HeapInterface heap;
    private ProgramState program;
    private RepositoryInterface repo;
    private Controller ctrl;

    @Before
    public  void setUp() throws FatalError
    {
        MyStackInterface<StatementInterface> executionStack = new MyStack<>();
        SymbolTable = new MyDictionary<>();
        OutputList = new MyList<>();
        file_table = new FileTable();
        heap = new Heap();
        program = new ProgramState(executionStack,SymbolTable,OutputList,file_table,heap);
        repo = new Repository(program,"testing.txt");
        ctrl = new Controller(repo);
    }

    @Test
    public void first() throws ControllerException, InvalidPositionException
    {
        program.getExecutionStack().push(new CompoundStatement(new AssignmentStatement("v",new ConstantExpression(2)),new PrintStatement(new VariableExpression("v"))));

        ctrl.allSteps();

        assert (SymbolTable.size()==1);
        assert (SymbolTable.get("v")==2);
        assert (OutputList.size()==1);
        assert (OutputList.get(0)==2);
    }

    @Test
    public void second() throws ControllerException, InvalidPositionException
    {
        program.getExecutionStack().push(new CompoundStatement(new AssignmentStatement("a",new ArithmeticExpression('+',new ConstantExpression(2),new ArithmeticExpression('*',new ConstantExpression(3),new ConstantExpression(5)))),new CompoundStatement(new AssignmentStatement("b",new ArithmeticExpression('+',new VariableExpression("a"),new ConstantExpression(1))),new PrintStatement(new VariableExpression("b")))));

        ctrl.allSteps();

        assert (SymbolTable.size()==2);
        assert (SymbolTable.get("a")==17);
        assert (SymbolTable.get("b")==18);
        assert (OutputList.size()==1);
        assert (OutputList.get(0)==18);
    }

    @Test
    public void third() throws ControllerException, InvalidPositionException
    {
        program.getExecutionStack().push(new CompoundStatement(new AssignmentStatement("a",new ArithmeticExpression('-',new ConstantExpression(2),new ConstantExpression(2))),new CompoundStatement(new IfStatement(new VariableExpression("a"),new AssignmentStatement("v",new ConstantExpression(2)),new AssignmentStatement("v",new ConstantExpression(3))),new PrintStatement(new VariableExpression("v")))));

        ctrl.allSteps();

        assert (SymbolTable.size()==2);
        assert (SymbolTable.get("a")==0);
        assert (SymbolTable.get("v")==3);
        assert (OutputList.size()==1);
        assert (OutputList.get(0)==3);
    }

    @Test
    public void fourth() throws ControllerException, InvalidPositionException
    {
        program.getFileTable().clear();

        program.getExecutionStack().push(new CompoundStatement(new OpenFileRead("var_f","test.in"),new CompoundStatement(new ReadFromFile(new VariableExpression("var_f"),"var_c"),new CompoundStatement(new PrintStatement(new VariableExpression("var_c")),new CompoundStatement(new IfStatement(new VariableExpression("var_c"),new CompoundStatement(new ReadFromFile(new VariableExpression("var_f"),"var_c"),new PrintStatement(new VariableExpression("var_c"))),new PrintStatement(new ConstantExpression(0))),new CloseFileStatement(new VariableExpression("var_f")))))));

        ctrl.allSteps();

        assert (SymbolTable.size()==2);
        assert (SymbolTable.get("var_f")==0);
        assert (SymbolTable.get("var_c")==50);
        assert (OutputList.size()==2);
        assert (OutputList.get(0)==15);
        assert (OutputList.get(1)==50);
        assert (file_table.size()==0);
    }

    @Test
    public void fifth()
    {
        program.getFileTable().clear();

        program.getExecutionStack().push(new CompoundStatement(new OpenFileRead("var_f","test.in"),new CompoundStatement(new ReadFromFile(new ArithmeticExpression('+',new VariableExpression("var_f"),new ConstantExpression(2)),"var_c"),new CompoundStatement(new PrintStatement(new VariableExpression("var_c")),new CompoundStatement(new IfStatement(new VariableExpression("var_c"),new CompoundStatement(new ReadFromFile(new VariableExpression("var_f"),"var_c"),new PrintStatement(new VariableExpression("var_c"))),new PrintStatement(new ConstantExpression(0))),new CloseFileStatement(new VariableExpression("var_f")))))));

        try
        {
            ctrl.allSteps();
            assert (false);
        }
        catch (ControllerException c)
        {
            assert (c.getMessage().contains("No entry for file descriptor"));
        }
    }

    @Test
    public void sixth() throws ControllerException, InvalidPositionException
    {
        program.getHeap().clear();

        program.getExecutionStack().push(new CompoundStatement(new AssignmentStatement("v",new ConstantExpression(10)),new CompoundStatement(new NewStatement("v",new ConstantExpression(20)),new CompoundStatement(new NewStatement("a",new ConstantExpression(22)),new PrintStatement(new VariableExpression("v"))))));

        ctrl.allSteps();

        assert (SymbolTable.size()==2);
        assert (SymbolTable.get("v")==1);
        assert (SymbolTable.get("a")==2);
        assert (OutputList.size()==1);
        assert (OutputList.get(0)==1);
        assert (heap.size()==2);
        assert (heap.get(1)==20);
        assert (heap.get(2)==22);
    }

    @Test
    public void seventh() throws ControllerException, InvalidPositionException
    {
        program.getHeap().clear();

        program.getExecutionStack().push(new CompoundStatement(new AssignmentStatement("v",new ConstantExpression(10)),new CompoundStatement(new NewStatement("v",new ConstantExpression(20)),new CompoundStatement(new NewStatement("a",new ConstantExpression(22)),new CompoundStatement(new PrintStatement(new ArithmeticExpression('+',new ConstantExpression(100),new ReadFromHeap("v"))),new PrintStatement(new ArithmeticExpression('+',new ConstantExpression(100),new ReadFromHeap("a"))))))));

        ctrl.allSteps();

        assert (SymbolTable.size()==2);
        assert (SymbolTable.get("v")==1);
        assert (SymbolTable.get("a")==2);
        assert (OutputList.size()==2);
        assert (OutputList.get(0)==120);
        assert (OutputList.get(1)==122);
        assert (heap.size()==2);
        assert (heap.get(1)==20);
        assert (heap.get(2)==22);
    }

    @Test
    public void eighth() throws ControllerException, InvalidPositionException
    {
        program.getHeap().clear();

        program.getExecutionStack().push(new CompoundStatement(new AssignmentStatement("v",new ConstantExpression(10)),new CompoundStatement(new NewStatement("v",new ConstantExpression(20)),new CompoundStatement(new NewStatement("a",new ConstantExpression(22)),new CompoundStatement(new WriteToHeap("a",new ConstantExpression(30)),new CompoundStatement(new PrintStatement(new VariableExpression("a")),new PrintStatement(new ReadFromHeap("a"))))))));

        ctrl.allSteps();

        assert (SymbolTable.size()==2);
        assert (SymbolTable.get("v")==1);
        assert (SymbolTable.get("a")==2);
        assert (OutputList.size()==2);
        assert (OutputList.get(0)==2);
        assert (OutputList.get(1)==30);
        assert (heap.size()==2);
        assert (heap.get(1)==20);
        assert (heap.get(2)==30);
    }

    @Test
    public void ninth() throws ControllerException, InvalidPositionException
    {
        program.getHeap().clear();

        program.getExecutionStack().push(new CompoundStatement(new AssignmentStatement("v",new ConstantExpression(10)),new CompoundStatement(new NewStatement("v",new ConstantExpression(20)),new CompoundStatement(new NewStatement("a",new ConstantExpression(22)),new CompoundStatement(new WriteToHeap("a",new ConstantExpression(30)),new CompoundStatement(new PrintStatement(new VariableExpression("a")),new CompoundStatement(new PrintStatement(new ReadFromHeap("a")),new AssignmentStatement("a",new ConstantExpression(0)))))))));

        ctrl.allSteps();

        assert (SymbolTable.size()==2);
        assert (SymbolTable.get("v")==1);
        assert (SymbolTable.get("a")==0);
        assert (OutputList.size()==2);
        assert (OutputList.get(0)==2);
        assert (OutputList.get(1)==30);
        assert (heap.size()==1);
        assert (heap.get(1)==20);
    }

    @Test
    public void tenth() throws ControllerException, InvalidPositionException
    {
        program.getExecutionStack().push(new CompoundStatement(new PrintStatement(new ArithmeticExpression('+',new ConstantExpression(10),new BooleanExpression("<",new ConstantExpression(2),new ConstantExpression(6)))),new PrintStatement(new BooleanExpression("<",new ArithmeticExpression('+',new ConstantExpression(10),new ConstantExpression(2)),new ConstantExpression(6)))));

        ctrl.allSteps();

        assert (OutputList.size()==2);
        assert (OutputList.get(0)==11);
        assert (OutputList.get(1)==0);
    }

    @Test
    public void eleventh() throws ControllerException, InvalidPositionException
    {
        program.getExecutionStack().push((new CompoundStatement(new AssignmentStatement("v",new ConstantExpression(6)),new CompoundStatement(new WhileStatement(new ArithmeticExpression('-',new VariableExpression("v"),new ConstantExpression(4)),new CompoundStatement(new PrintStatement(new VariableExpression("v")),new AssignmentStatement("v",new ArithmeticExpression('-',new VariableExpression("v"),new ConstantExpression(1))))),new PrintStatement(new VariableExpression("v"))))));

        ctrl.allSteps();

        assert(SymbolTable.size()==1);
        assert(SymbolTable.get("v")==4);
        assert (OutputList.size()==3);
        assert (OutputList.get(0)==6);
        assert (OutputList.get(1)==5);
        assert (OutputList.get(2)==4);
    }

    @Test
    public void twelfth() throws ControllerException, InvalidPositionException
    {
        program.getHeap().clear();
        program.resetNextID();

        program.getExecutionStack().push(new CompoundStatement(new AssignmentStatement("v",new ConstantExpression(10)),new CompoundStatement(new NewStatement("a", new ConstantExpression(22)),new CompoundStatement(new ForkStatement(new CompoundStatement(new WriteToHeap("a",new ConstantExpression(30)),new CompoundStatement(new AssignmentStatement("v",new ConstantExpression(32)),new CompoundStatement(new PrintStatement(new VariableExpression("v")),new PrintStatement(new ReadFromHeap("a")))))),new CompoundStatement(new PrintStatement(new VariableExpression("v")),new PrintStatement(new ReadFromHeap("a")))))));

        ctrl.debug_all_steps();

        assert (repo.getProgramList().size()==2);

        assert (repo.getProgramList().get(0).getID()==1);
        assert (repo.getProgramList().get(0).getSymbolTable().size()==2);
        assert (repo.getProgramList().get(0).getSymbolTable().get("v")==10);
        assert (repo.getProgramList().get(0).getSymbolTable().get("a")==1);

        assert (repo.getProgramList().get(1).getID()==2);
        assert (repo.getProgramList().get(1).getSymbolTable().size()==2);
        assert (repo.getProgramList().get(1).getSymbolTable().get("v")==32);
        assert (repo.getProgramList().get(1).getSymbolTable().get("a")==1);

        assert (heap.size()==1);
        assert (heap.get(1)==30);

        assert (OutputList.size()==4);
        assert (OutputList.get(0)==10);
        assert (OutputList.get(1)==30);
        assert (OutputList.get(2)==32);
        assert (OutputList.get(3)==30);
    }

    @Test
    public void thirteenth() throws ControllerException, InvalidPositionException
    {
        program.resetNextID();

        program.getExecutionStack().push(new CompoundStatement(new ForkStatement(new CompoundStatement(new ForkStatement(new SkipStatement()),new SkipStatement())),new ForkStatement(new SkipStatement())));

        ctrl.debug_all_steps();

        assert (repo.getProgramList().size()==4);

        assert (repo.getProgramList().get(0).getID()==1);
        assert (repo.getProgramList().get(1).getID()==3);
        assert (repo.getProgramList().get(2).getID()==2);
        assert (repo.getProgramList().get(3).getID()==4);
    }

    @Test
    public void fourteenth() throws ControllerException, InvalidPositionException
    {
        program.resetNextID();

        program.getExecutionStack().push(new CompoundStatement(new ForkStatement(new SkipStatement()),new ForkStatement(new SkipStatement())));

        ctrl.debug_all_steps();

        assert (repo.getProgramList().size()==3);

        assert (repo.getProgramList().get(0).getID()==1);
        assert (repo.getProgramList().get(1).getID()==2);
        assert (repo.getProgramList().get(2).getID()==3);
    }

    @Test
    public void fifteenth() throws ControllerException, InvalidPositionException
    {
        program.getHeap().clear();
        program.resetNextID();

        program.getExecutionStack().push(new CompoundStatement(new AssignmentStatement("v",new ConstantExpression(10)),new CompoundStatement(new NewStatement("a", new ConstantExpression(22)),new CompoundStatement(new ForkStatement(new CompoundStatement(new WriteToHeap("a",new ConstantExpression(30)),new CompoundStatement(new AssignmentStatement("v",new ConstantExpression(32)),new CompoundStatement(new PrintStatement(new VariableExpression("v")),new PrintStatement(new ReadFromHeap("a")))))),new CompoundStatement(new PrintStatement(new VariableExpression("v")),new PrintStatement(new ReadFromHeap("a")))))));

        ctrl.gui_debug_prepare();
        boolean result = ctrl.gui_debug_one_step();
        while(result)
            result=ctrl.gui_debug_one_step();

        assert (repo.getProgramList().size()==2);

        assert (repo.getProgramList().get(0).getID()==1);
        assert (repo.getProgramList().get(0).getSymbolTable().size()==2);
        assert (repo.getProgramList().get(0).getSymbolTable().get("v")==10);
        assert (repo.getProgramList().get(0).getSymbolTable().get("a")==1);

        assert (repo.getProgramList().get(1).getID()==2);
        assert (repo.getProgramList().get(1).getSymbolTable().size()==2);
        assert (repo.getProgramList().get(1).getSymbolTable().get("v")==32);
        assert (repo.getProgramList().get(1).getSymbolTable().get("a")==1);

        assert (heap.size()==1);
        assert (heap.get(1)==30);

        assert (OutputList.size()==4);
        assert (OutputList.get(0)==10);
        assert (OutputList.get(1)==30);
        assert (OutputList.get(2)==32);
        assert (OutputList.get(3)==30);
    }
}