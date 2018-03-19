package view;

import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import model.util.Pair;
import javafx.beans.property.ReadOnlyObjectWrapper;

public class TableViewController
{
    @FXML
    protected TableView<Pair<String,String>> theTable;
    @FXML
    protected TableColumn<Pair<String,String>,String> firstColumn;
    @FXML
    protected TableColumn<Pair<String,String>,String> secondColumn;

    @FXML
    protected Label currentItem;

    public TableViewController()
    {

    }

    @FXML
    protected void initialize()
    {
        firstColumn.setCellValueFactory(p -> new ReadOnlyObjectWrapper<>(p.getValue().getLeft()));
        secondColumn.setCellValueFactory(p -> new ReadOnlyObjectWrapper<>(p.getValue().getRight()));

        showCurrentSelection(null);
        theTable.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue)->showCurrentSelection(newValue));
    }

    public void setNames(String s1, String s2)
    {
        firstColumn.setText(s1);
        secondColumn.setText(s2);
    }

    public void setContent(ObservableList<Pair<String,String>> list)
    {
        theTable.setItems(list);
    }

    private void showCurrentSelection(Pair<String,String> p)
    {
        if(p != null)
            currentItem.setText(p.getLeft()+" "+p.getRight());
        else
            currentItem.setText("No item selected.");
    }
}
