package view;

import controller.Controller;
import exceptions.ControllerException;
import exceptions.FatalError;
import exceptions.InvalidPositionException;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.layout.BorderPane;
import javafx.scene.control.SplitPane;
import javafx.stage.Modality;
import javafx.stage.Stage;
import model.ProgramState;
import model.expressions.*;
import model.statements.*;
import model.util.*;
import repository.Repository;
import repository.RepositoryInterface;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;

public class AppCoord extends Application {

    private Stage primaryStage;
    private BorderPane rootLayout;
    private int programToRun;
    private RepositoryInterface runningRepository;
    private Controller runningController;
    private RootController rc;
    private ArrayList<Pair<String,String>> tableViewList = new ArrayList<>();
    private ArrayList<String> listViewList = new ArrayList<>();
    private ObservableList<Pair<String,String>> obsTableViewList;
    private ObservableList<String> obsListViewList;

    private ArrayList<ProgramState> ProgramStates = new ArrayList<>();
    private ArrayList<RepositoryInterface> Repositories = new ArrayList<>();
    private ArrayList<Controller> Controllers = new ArrayList<>();
    private int number_of_examples = 12;
    private TableViewController heapController;
    private ListController outController;
    private TableViewController fileController;
    private ListController listPrgController;
    private TableViewController symTableController;
    private ListController stackController;

    public AppCoord()
    {
        try
        {
            ArrayList<MyStackInterface<StatementInterface>> executionStacks = new ArrayList<>();
            ArrayList<MyDictionaryInterface<String, Integer>> symbolTables = new ArrayList<>();
            ArrayList<MyListInterface<Integer>> outputLists = new ArrayList<>();
            ArrayList<FileTableInterface> fileTables = new ArrayList<>();
            ArrayList<HeapInterface> heaps = new ArrayList<>();

            for(int i = 1; i<= number_of_examples; i++)
                executionStacks.add(new MyStack<>());

            // First example:
            executionStacks.get(0).push(new CompoundStatement(new AssignmentStatement("v",new ConstantExpression(2)),new PrintStatement(new VariableExpression("v"))));

            //Second example
            executionStacks.get(1).push(new CompoundStatement(new AssignmentStatement("a",new ArithmeticExpression('+',new ConstantExpression(2),new ArithmeticExpression('*',new ConstantExpression(3),new ConstantExpression(5)))),new CompoundStatement(new AssignmentStatement("b",new ArithmeticExpression('+',new VariableExpression("a"),new ConstantExpression(1))),new PrintStatement(new VariableExpression("b")))));

            //Third example
            executionStacks.get(2).push(new CompoundStatement(new AssignmentStatement("a",new ArithmeticExpression('-',new ConstantExpression(2),new ConstantExpression(2))),new CompoundStatement(new IfStatement(new VariableExpression("a"),new AssignmentStatement("v",new ConstantExpression(2)),new AssignmentStatement("v",new ConstantExpression(3))),new PrintStatement(new VariableExpression("v")))));

            //Fourth example
            executionStacks.get(3).push(new CompoundStatement(new OpenFileRead("var_f","test.in"),new CompoundStatement(new ReadFromFile(new VariableExpression("var_f"),"var_c"),new CompoundStatement(new PrintStatement(new VariableExpression("var_c")),new CompoundStatement(new IfStatement(new VariableExpression("var_c"),new CompoundStatement(new ReadFromFile(new VariableExpression("var_f"),"var_c"),new PrintStatement(new VariableExpression("var_c"))),new PrintStatement(new ConstantExpression(0))),new CloseFileStatement(new VariableExpression("var_f")))))));

            //Fifth example
            executionStacks.get(4).push(new CompoundStatement(new OpenFileRead("var_f","test.in"),new CompoundStatement(new ReadFromFile(new ArithmeticExpression('+',new VariableExpression("var_f"),new ConstantExpression(2)),"var_c"),new CompoundStatement(new PrintStatement(new VariableExpression("var_c")),new CompoundStatement(new IfStatement(new VariableExpression("var_c"),new CompoundStatement(new ReadFromFile(new VariableExpression("var_f"),"var_c"),new PrintStatement(new VariableExpression("var_c"))),new PrintStatement(new ConstantExpression(0))),new CloseFileStatement(new VariableExpression("var_f")))))));

            //Sixth example
            executionStacks.get(5).push(new CompoundStatement(new AssignmentStatement("v",new ConstantExpression(2)),new CompoundStatement(new NewStatement("v",new ConstantExpression(20)),new CompoundStatement(new NewStatement("a",new ConstantExpression(22)),new PrintStatement(new VariableExpression("v"))))));

            //Seventh example
            executionStacks.get(6).push(new CompoundStatement(new AssignmentStatement("v",new ConstantExpression(2)),new CompoundStatement(new NewStatement("v",new ConstantExpression(20)),new CompoundStatement(new NewStatement("a",new ConstantExpression(22)),new CompoundStatement(new PrintStatement(new ArithmeticExpression('+',new ConstantExpression(100),new ReadFromHeap("v"))),new PrintStatement(new ArithmeticExpression('+',new ConstantExpression(100),new ReadFromHeap("a"))))))));

            //Eigth example
            executionStacks.get(7).push(new CompoundStatement(new AssignmentStatement("v",new ConstantExpression(10)),new CompoundStatement(new NewStatement("v",new ConstantExpression(20)),new CompoundStatement(new NewStatement("a",new ConstantExpression(22)),new CompoundStatement(new WriteToHeap("a",new ConstantExpression(30)),new CompoundStatement(new PrintStatement(new VariableExpression("a")),new PrintStatement(new ReadFromHeap("a"))))))));

            //Ninth example
            executionStacks.get(8).push(new CompoundStatement(new AssignmentStatement("v",new ConstantExpression(10)),new CompoundStatement(new NewStatement("v",new ConstantExpression(20)),new CompoundStatement(new NewStatement("a",new ConstantExpression(22)),new CompoundStatement(new WriteToHeap("a",new ConstantExpression(30)),new CompoundStatement(new PrintStatement(new VariableExpression("a")),new CompoundStatement(new PrintStatement(new ReadFromHeap("a")),new AssignmentStatement("a",new ConstantExpression(0)))))))));

            //Tenth example
            executionStacks.get(9).push(new CompoundStatement(new PrintStatement(new ArithmeticExpression('+',new ConstantExpression(10),new BooleanExpression("<",new ConstantExpression(2),new ConstantExpression(6)))),new PrintStatement(new BooleanExpression("<",new ArithmeticExpression('+',new ConstantExpression(10),new ConstantExpression(2)),new ConstantExpression(6)))));

            //11th example
            executionStacks.get(10).push((new CompoundStatement(new AssignmentStatement("v",new ConstantExpression(6)),new CompoundStatement(new WhileStatement(new ArithmeticExpression('-',new VariableExpression("v"),new ConstantExpression(4)),new CompoundStatement(new PrintStatement(new VariableExpression("v")),new AssignmentStatement("v",new ArithmeticExpression('-',new VariableExpression("v"),new ConstantExpression(1))))),new PrintStatement(new VariableExpression("v"))))));

            //12th example
            executionStacks.get(11).push(new CompoundStatement(new AssignmentStatement("v",new ConstantExpression(10)),new CompoundStatement(new NewStatement("a", new ConstantExpression(22)),new CompoundStatement(new ForkStatement(new CompoundStatement(new WriteToHeap("a",new ConstantExpression(30)),new CompoundStatement(new AssignmentStatement("v",new ConstantExpression(32)),new CompoundStatement(new PrintStatement(new VariableExpression("v")),new PrintStatement(new ReadFromHeap("a")))))),new CompoundStatement(new PrintStatement(new VariableExpression("v")),new PrintStatement(new ReadFromHeap("a")))))));

            for(int i=0;i<=number_of_examples-1;i++)
            {
                symbolTables.add(new MyDictionary<>());
                outputLists.add(new MyList<>());
                fileTables.add(new FileTable());
                heaps.add(new Heap());

                ProgramStates.add(new ProgramState(executionStacks.get(i), symbolTables.get(i), outputLists.get(i), fileTables.get(i), heaps.get(i)));
                Integer index = i+1;
                Repositories.add(new Repository(ProgramStates.get(i),"log"+index.toString()+".txt"));
                Controllers.add(new Controller(Repositories.get(i)));
            }
        }
        catch (FatalError f)
        {
            f.printStackTrace();
        }
    }

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage)
    {
        this.primaryStage=primaryStage;
        this.primaryStage.setTitle("App");
        primaryStage.setOnCloseRequest(p->{Platform.exit();System.exit(0);});

        ArrayList<String> l = new ArrayList<>();
        for(int i=0;i<=number_of_examples-1;i++)
            l.add(ProgramStates.get(i).getExecutionStack().toString());

        int val = showDialog(l);
        if(val==0)
            Platform.exit();
    }

    /**
     * Initializes the root layout.
     */
    private void initRootLayout()
    {
        try
        {
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(AppCoord.class.getResource("RootLayout.fxml"));
            rootLayout = loader.load();
            rc = loader.getController();
            rc.setAppCoord(this);

            Scene scene = new Scene(rootLayout);
            primaryStage.setScene(scene);
            primaryStage.show();
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }
    }

    /**
     * Shows the person overview inside the root layout.
     */
    @FXML
    private void showMainWindow() {
        runningRepository = Repositories.get(programToRun);
        runningController = Controllers.get(programToRun);

        try
        {
            FXMLLoader heapLoader = new FXMLLoader();
            heapLoader.setLocation(AppCoord.class.getResource("TableView.fxml"));
            BorderPane heapView = heapLoader.load();
            heapController = heapLoader.getController();
            heapController.setNames("Adress","Value");

            FXMLLoader outLoader = new FXMLLoader();
            outLoader.setLocation(getClass().getResource("SimpleList.fxml"));
            BorderPane outView =  outLoader.load();
            outController = outLoader.getController();
            outController.setAppCoord(this);

            // --

            FXMLLoader fileLoader = new FXMLLoader();
            fileLoader.setLocation(AppCoord.class.getResource("TableView.fxml"));
            BorderPane fileView = fileLoader.load();
            fileController = fileLoader.getController();
            fileController.setNames("Identifier","File Name");

            FXMLLoader listPrgLoader = new FXMLLoader();
            listPrgLoader.setLocation(getClass().getResource("SimpleList.fxml"));
            BorderPane listPrgView = listPrgLoader.load();
            listPrgController = listPrgLoader.getController();
            listPrgController.setAppCoord(this);
            listPrgController.setFlag();

            // --

            FXMLLoader symTableLoader = new FXMLLoader();
            symTableLoader.setLocation(AppCoord.class.getResource("TableView.fxml"));
            BorderPane symTableView = symTableLoader.load();
            symTableController = symTableLoader.getController();
            symTableController.setNames("Variable","Value");

            FXMLLoader stackLoader = new FXMLLoader();
            stackLoader.setLocation(getClass().getResource("SimpleList.fxml"));
            BorderPane stackView = stackLoader.load();
            stackController = stackLoader.getController();
            stackController.setAppCoord(this);

            // --

            SplitPane sp = (SplitPane) rootLayout.getCenter();
            sp.getItems().add(heapView);
            sp.getItems().add(outView);
            sp.getItems().add(fileView);
            sp.getItems().add(listPrgView);
            sp.getItems().add(symTableView);
            sp.getItems().add(stackView);

            sp.setDividerPositions(0.17f,0.34f,0.5f,0.67f,0.84f,1f);

            runningController.gui_prepare_one_step();
            update();
        }
        catch (IOException | InvalidPositionException e)
        {
            e.printStackTrace();
        }
    }

    private int showDialog(ArrayList<String> options) {
        try {
            // Load the fxml file and create a new stage for the popup dialog.
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(getClass().getResource("SelectionDialog.fxml"));
            BorderPane page = loader.load();

            // Create the dialog Stage.
            Stage dialogStage = new Stage();
            dialogStage.setOnHiding( event -> {initRootLayout();showMainWindow();} );

            dialogStage.setTitle("Choose option");
            dialogStage.initModality(Modality.WINDOW_MODAL);
            dialogStage.initOwner(primaryStage);
            Scene scene = new Scene(page);
            dialogStage.setScene(scene);

            // Set the person into the controller.
            DialogController controller = loader.getController();
            controller.setParent(this);
            controller.setDialogStage(dialogStage);
            controller.setSelectionList(options);

            // Show the dialog and wait until the user closes it
            dialogStage.showAndWait();

            return 1;
        } catch (IOException e) {
            e.printStackTrace();
            return 0;
        }
    }

    private void update() throws InvalidPositionException
    {
        rc.setTheLabel(runningRepository.getProgramList().size());

        tableViewList.clear();
        listViewList.clear();

        if(runningRepository.getProgramList().size()==0)
            return;

        HeapInterface heap = runningRepository.getProgramList().get(0).getHeap();
        if(heap !=null)
            for(Integer i : heap.keySet())
                tableViewList.add(new Pair<>(i.toString(), heap.get(i).toString()));
        obsTableViewList = FXCollections.observableArrayList(tableViewList);
        heapController.setContent(obsTableViewList);

        MyListInterface<Integer> output = runningRepository.getProgramList().get(0).getOutput();
        if(output !=null)
            for(Integer i : output.getContent())
                listViewList.add(i.toString());
        obsListViewList = FXCollections.observableArrayList(listViewList);
        outController.setContent(obsListViewList);

        tableViewList.clear();
        listViewList.clear();

        FileTableInterface fileTable = runningRepository.getProgramList().get(0).getFileTable();
        if(fileTable !=null)
            for(Integer id : fileTable.keySet())
                tableViewList.add(new Pair<>(id.toString(), fileTable.get(id).getLeft()));
        obsTableViewList = FXCollections.observableArrayList(tableViewList);
        fileController.setContent(obsTableViewList);

        MyListInterface<ProgramState> prgList = runningRepository.getProgramList();
        if(prgList !=null)
            for(ProgramState i : prgList.getContent())
                listViewList.add(i.getID().toString());
        obsListViewList = FXCollections.observableArrayList(listViewList);
        listPrgController.setContent(obsListViewList);
    }

    public void select_thread(Integer i)
    {
        try
        {
            tableViewList.clear();
            listViewList.clear();

            MyListInterface<ProgramState> prgList = runningRepository.getProgramList();
            for(ProgramState prg : prgList.getContent())
                if(prg.getID().equals(i))
                {
                    MyDictionaryInterface<String, Integer> symbolTable = prg.getSymbolTable();
                    if(symbolTable !=null)
                        for(String id : symbolTable.keySet())
                            tableViewList.add(new Pair<>(id, symbolTable.get(id).toString()));
                    obsTableViewList = FXCollections.observableArrayList(tableViewList);
                    symTableController.setContent(obsTableViewList);

                    MyStackInterface<StatementInterface> executionStack = prg.getExecutionStack();
                    if(executionStack !=null)
                        for(StatementInterface s : executionStack.getContent())
                            listViewList.add(s.toString());
                    Collections.reverse(listViewList);
                    obsListViewList = FXCollections.observableArrayList(listViewList);
                    stackController.setContent(obsListViewList);
                    return;
                }
        }
        catch (InvalidPositionException IE55)
        {
            IE55.printStackTrace();
        }
    }

    public void run_once() throws InvalidPositionException
    {
        try
        {
            if(!runningController.gui_one_step())
            {
                listPrgController.setContent(FXCollections.observableArrayList());

                Alert alert = new Alert(Alert.AlertType.WARNING);
                alert.initOwner(primaryStage);
                alert.setTitle("Mesaj");
                alert.setHeaderText("Executia programului s-a incheiat!");

                alert.showAndWait();
            }
        }
        catch (ControllerException c)
        {
            DisplayError(c);
        }
        update();
    }

    public void run_all() throws InvalidPositionException
    {
        try
        {
            boolean result = runningController.gui_debug_one_step();
            while(result)
                result=runningController.gui_debug_one_step();

            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.initOwner(primaryStage);
            alert.setTitle("Mesaj");
            alert.setHeaderText("Executia programului s-a incheiat!");
        }
        catch (ControllerException c)
        {
            DisplayError(c);
        }
        update();
    }

    private void DisplayError(ControllerException c) throws InvalidPositionException
    {
        update();

        runningController.logKittyException(c.getMessage());

        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.initOwner(primaryStage);
        alert.setTitle("Exception");
        alert.setHeaderText(c.getMessage());

        alert.showAndWait();

        Platform.exit();
        System.exit(0);
    }

    public void setIndex(int s)
    {
        this.programToRun = s;
    }
}