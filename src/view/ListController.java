package view;

import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;

public class ListController
{
    @FXML
    protected ListView<String> theList;

    @FXML
    protected Label theLabel;

    private AppCoord appCoord;

    private boolean flag = false;

    public void setFlag()
    {
        this.flag = true;
    }

    @FXML
    void initialize()
    {
        showCurrentSelection(null);
        theList.getSelectionModel().selectedItemProperty().addListener((observable,oldValue,newValue)->showCurrentSelection(newValue));
    }

    public void setContent(ObservableList<String> ol)
    {
        theList.setItems(ol);
        if(ol.size()!=0)
            theList.getSelectionModel().select(0);
    }

    public void setAppCoord(AppCoord appCoord)
    {
        this.appCoord = appCoord;
    }

    private void showCurrentSelection(String s)
    {
        if(flag && s!=null)
            appCoord.select_thread(Integer.parseInt(s));
        if(s != null)
            theLabel.setText(s);
        else
            theLabel.setText("No item selected.");
    }
}
