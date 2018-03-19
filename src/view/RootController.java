package view;

import exceptions.InvalidPositionException;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.control.TextField;

public class RootController {

    public void setTheLabel(Integer i) {
        this.theLabel.setText("Number of program states in repository: "+i.toString());
    }

    @FXML
    private TextField theLabel;

    private AppCoord appCoord;

    public void setAppCoord(AppCoord appCoord) {
        this.appCoord = appCoord;
    }

    @FXML
    void initialize()
    {

    }

    @FXML
    private void all_steps_pressed() throws InvalidPositionException
    {
        appCoord.run_all();
    }

    @FXML
    private void handlePress() throws InvalidPositionException
    {
        appCoord.run_once();
    }

    @FXML
    private void exit()
    {
        Platform.exit();
        System.exit(0);
    }
}
