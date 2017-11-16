package seng202.group6.controller;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonBar;
import javafx.scene.control.RadioButton;
import javafx.scene.control.TextField;
import javafx.scene.control.Toggle;
import javafx.scene.control.ToggleGroup;
import javafx.stage.Stage;
import seng202.group6.DataContainer;
import seng202.group6.model.AirlineList;
import seng202.group6.model.AirportList;
import seng202.group6.model.FlightList;
import seng202.group6.model.RouteList;
import seng202.group6.utils.RecordType;

/**
 * The controller class to handle the List Creation GUI.
 */
public class CreateListController {
    @FXML
    private ToggleGroup toggleGroupDataType;
    @FXML
    private RadioButton radioAirline;
    @FXML
    private RadioButton radioAirport;
    @FXML
    private RadioButton radioFlight;
    @FXML
    private RadioButton radioRoute;

    @FXML
    private TextField fieldName;

    @FXML
    private ButtonBar buttonBar;
    @FXML
    private Button buttonCreate;
    @FXML
    private Button buttonCancel;

    /**
     * The data container to add the list to.
     */
    private DataContainer dataContainer;


    /**
     * FX initialization method. Supplements view definition in .fxml file.
     */
    @SuppressWarnings("unused")
    @FXML
    private void initialize() {
        ButtonBar.setButtonData(buttonCreate, ButtonBar.ButtonData.OK_DONE);
        ButtonBar.setButtonData(buttonCancel, ButtonBar.ButtonData.CANCEL_CLOSE);
    }


    /**
     * Closes the create list window.
     */
    @FXML
    private void close() {
        ((Stage) buttonBar.getScene().getWindow()).close();
    }


    /**
     * Adds a new list of the selected type to the data container. Called by the confirm button.
     */
    @SuppressWarnings("unused")
    @FXML
    private void createList() {
        Toggle toggle = toggleGroupDataType.getSelectedToggle();
        String name = fieldName.getText();
        if (toggle != null) {
            if (toggle == radioAirline) {
                dataContainer.addAirlineList(new AirlineList(name));
            } else if (toggle == radioAirport) {
                dataContainer.addAirportList(new AirportList(name));
            } else if (toggle == radioFlight) {
                dataContainer.addFlightList(new FlightList(name));
            } else if (toggle == radioRoute) {
                dataContainer.addRouteList(new RouteList(name));
            }
        }
        close();
    }


    /**
     * Sets the data container to add the list to. Selects the given type.
     *
     * @param dataContainer the data container
     * @param type          the type of list to initiate to. Can be null.
     */
    public void setUp(DataContainer dataContainer, RecordType type) {
        // Default to new airline list
        radioAirline.setSelected(true);

        this.dataContainer = dataContainer;

        if (type != null) {
            switch (type) {
                case AIRLINE:
                    radioAirline.setSelected(true);
                    break;
                case AIRPORT:
                    radioAirport.setSelected(true);
                    break;
                case FLIGHT:
                    radioFlight.setSelected(true);
                    break;
                case ROUTE:
                    radioRoute.setSelected(true);
                    break;
            }
        }
    }
}
