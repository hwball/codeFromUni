package seng202.group6.controller;

import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.stage.Stage;
import seng202.group6.utils.Verify;

/**
 * The controller class for the settings window.
 */
public class SettingsController {

    @FXML
    private Button cancelButton;
    @FXML
    private TextField maxGraphInput;
    @FXML
    private TextField maxMapInput;

    private MainWindowController mainWindowController;


    /**
     * Closes the window.
     */
    public void close() {
        // code to quit window
        (cancelButton.getScene().getWindow()).hide();
    }


    /**
     * Sets the max number of items shown in the graph and maps.
     */
    public void setMax() {
        setGraphMax();
        setMapMax();
        close();
    }


    /**
     * Sets the max number of items shown in the graph.
     */
    private void setGraphMax() {
        String graphMax = maxGraphInput.getText();
        if (!graphMax.equals("")) {
            if (Verify.validateInteger(graphMax)) {
                try {
                    int max = Integer.parseInt(graphMax);
                    mainWindowController.setGraphLimit(max >= 0 ? max : 0);
                } catch (NumberFormatException e) { // number greater than max int
                    mainWindowController.setGraphLimit(Integer.MAX_VALUE);
                }
            } else {
                Alert graphInputAlert = new Alert(Alert.AlertType.ERROR);
                graphInputAlert.setTitle("Invalid entry for graphs");
                ((Stage)graphInputAlert.getDialogPane().getScene().getWindow()).getIcons()
                        .add(new Image(getClass().getClassLoader().getResourceAsStream("icon.png")));
                graphInputAlert.setHeaderText(null);
                graphInputAlert.setContentText("Max number of graph items must be an integer");
            }
        }
    }


    /**
     * Sets the max number of items shown in the map.
     */
    private void setMapMax() {
        String mapMax = maxMapInput.getText();
        if (!mapMax.equals("")) {
            if (Verify.validateInteger(mapMax)) {
                try {
                    int max = Integer.parseInt(mapMax);
                    mainWindowController.setMapLimit(max >= 0 ? max : 0);
                } catch (NumberFormatException e) { // number greater than max int
                    mainWindowController.setMapLimit(Integer.MAX_VALUE);
                }
            } else {
                Alert emptyFieldAlert = new Alert(Alert.AlertType.ERROR);
                emptyFieldAlert.setTitle("Invalid entry for maps");
                ((Stage)emptyFieldAlert.getDialogPane().getScene().getWindow()).getIcons()
                        .add(new Image(getClass().getClassLoader().getResourceAsStream("icon.png")));
                emptyFieldAlert.setHeaderText(null);
                emptyFieldAlert.setContentText("Max number of map items must be an integer");
            }
        }
    }


    /**
     * Sets the main controller reference. Should be called once after fxml
     * loaded and before stage shown.
     *
     * @param mainWindowController the reference to the main window controller the class uses
     */
    public void setUp(MainWindowController mainWindowController) {
        this.mainWindowController = mainWindowController;
        maxGraphInput.setText(mainWindowController.getGraphLimit().toString());
        maxMapInput.setText(mainWindowController.getMapLimit().toString());
    }
}
