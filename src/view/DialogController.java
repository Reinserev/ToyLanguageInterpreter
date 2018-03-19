package view;

import java.util.ArrayList;

import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.ListView;
import javafx.stage.Stage;

public class DialogController
{
    public void setSelectionList(ArrayList<String> selectionList)
    {
        this.selectionList.setItems(FXCollections.observableArrayList(selectionList));
        this.selectionList.getSelectionModel().select(this.selectionList.getItems().size()-1);
    }

    @FXML
    protected ListView<String> selectionList;

    private Stage dialogStage;
    private AppCoord main_app;

    @FXML
    void initialize()
    {

    }

    public void setDialogStage(Stage dialogStage)
    {
        this.dialogStage=dialogStage;
    }

    public void setParent(AppCoord a)
    {
        this.main_app=a;
    }

    @FXML
    public void handleOK()
    {
       if(selectionList.getSelectionModel().getSelectedItem()==null)
       {
           Alert alert = new Alert(Alert.AlertType.WARNING);
           alert.initOwner(dialogStage);
           alert.setTitle("Eroare");
           alert.setHeaderText("Selecteaza un program!");

           alert.showAndWait();
       }
       else
       {
           main_app.setIndex(selectionList.getSelectionModel().getSelectedIndex());
           dialogStage.close();
       }
    }

    @FXML
    public void handleExit()
    {
        Platform.exit();
        System.exit(0);
    }
}
