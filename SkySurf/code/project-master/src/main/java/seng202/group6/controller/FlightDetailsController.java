package seng202.group6.controller;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;
import seng202.group6.model.Flight;
import seng202.group6.model.FlightPoint;

/**
 * Controller for the the flight fxml file used for the GUI. Holds window for the table that shows
 * flight points of a flight.
 */
public class FlightDetailsController {
    @FXML
    private TableView<FlightPoint> flightPointTable;
    @FXML
    private TableColumn<FlightPoint, String> typeCol;
    @FXML
    private TableColumn<FlightPoint, String> idCol;
    @FXML
    private TableColumn<FlightPoint, Double> altCol;
    @FXML
    private TableColumn<FlightPoint, Double> latCol;
    @FXML
    private TableColumn<FlightPoint, Double> lonCol;
    @FXML
    private Button closeButton;


    /**
     * Closes the flight extra details window.
     */
    @FXML
    @SuppressWarnings("unused")
    private void close() {
        Stage st = (Stage) closeButton.getScene().getWindow();
        st.hide();
    }


    /**
     * Populates the the table for the flight extra details window with the flight points of the
     * given flight.
     *
     * @param flight the flight to display details of
     */
    public void setData(Flight flight) {
        typeCol.setCellValueFactory(new PropertyValueFactory<>("type"));
        idCol.setCellValueFactory(new PropertyValueFactory<>("id"));
        altCol.setCellValueFactory(new PropertyValueFactory<>("altitude"));
        latCol.setCellValueFactory(new PropertyValueFactory<>("latitude"));
        lonCol.setCellValueFactory(new PropertyValueFactory<>("longitude"));
        ObservableList<FlightPoint> table = FXCollections.observableArrayList(flight.getFlightPoints());
        flightPointTable.setItems(table);
    }
}
