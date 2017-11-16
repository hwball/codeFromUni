package seng202.group6.controller;

import java.util.ArrayList;
import java.util.Arrays;

import javafx.fxml.FXML;
import javafx.scene.control.CheckBox;
import javafx.scene.control.TextField;
import seng202.group6.model.Route;

import static seng202.group6.utils.Verify.validateAirlineIATA;
import static seng202.group6.utils.Verify.validateAirportIATA;
import static seng202.group6.utils.Verify.validateAirportICAO;
import static seng202.group6.utils.Verify.validateInteger;
import static seng202.group6.utils.Verify.validateRouteEquipmentList;

/**
 * Controller for Route add/edit window.
 */
public class AddEditRoute extends AddEditController<Route> {
    @FXML
    private TextField fieldAirline;
    @FXML
    private TextField fieldSource;
    @FXML
    private TextField fieldDestination;
    @FXML
    private CheckBox checkCodeShare;
    @FXML
    private TextField fieldStops;
    @FXML
    private TextField fieldEquipment;


    @Override
    protected void populateFields() {
        fieldAirline.setText(recordUnderEdit.getAirline());
        fieldSource.setText(recordUnderEdit.getSourceAirport());
        fieldDestination.setText(recordUnderEdit.getDestinationAirport());
        checkCodeShare.setSelected(recordUnderEdit.getCodeShare());
        fieldStops.setText(Integer.toString(recordUnderEdit.getStops()));
        fieldEquipment.setText(recordUnderEdit.getEquipmentAsString());
    }


    @Override
    protected void applyEdit() {
        recordUnderEdit.setAirline(fieldAirline.getText());
        recordUnderEdit.setSourceAirport(fieldSource.getText());
        recordUnderEdit.setDestinationAirport(fieldDestination.getText());
        recordUnderEdit.setCodeShare(checkCodeShare.isSelected());
        recordUnderEdit.setStops(Integer.parseInt(fieldStops.getText()));
        recordUnderEdit.setEquipment(getEquipmentAsList());
    }


    @Override
    protected void addRecord() {
        sourceList.add(
                new Route(fieldAirline.getText(),
                        fieldSource.getText(),
                        fieldDestination.getText(),
                        checkCodeShare.isSelected(),
                        Integer.parseInt(fieldStops.getText()),
                        getEquipmentAsList()));
    }


    /**
     * Splits the equipment list string on spaces.
     *
     * @return a list containing each word in the equipment list field
     */
    private ArrayList<String> getEquipmentAsList() {
        return new ArrayList<>(Arrays.asList(fieldEquipment.getText().split(" ")));
    }


    @Override
    protected boolean validate() {
        return isValidAirline() && isValidSource() && isValidDestination() && isValidStops()
                && isValidEquipment();
    }


    @Override
    protected void initialize() {
        fieldAirline.textProperty().addListener((observable, oldValue, newValue) -> indicateAirline());
        indicateAirline();

        fieldSource.textProperty().addListener((observable, oldValue, newValue) -> indicateSource());
        indicateSource();

        fieldDestination.textProperty().addListener((observable, oldValue, newValue) -> indicateDestination());
        indicateDestination();

        fieldStops.textProperty().addListener((observable, oldValue, newValue) -> indicateStops());
        indicateStops();

        fieldEquipment.textProperty().addListener((observable, oldValue, newValue) -> indicateEquipment());
        indicateEquipment();

    }


    /**
     * Indicates whether or not the Airline entry field contains valid data.
     */
    private void indicateAirline() {
        fieldAirline.pseudoClassStateChanged(errorClass, !isValidAirline());
    }


    /**
     * Indicates whether or not the Source entry field contains valid data.
     */
    private void indicateSource() {
        fieldSource.pseudoClassStateChanged(errorClass, !isValidSource());
    }


    /**
     * Indicates whether or not the Destination entry field contains valid data.
     */
    private void indicateDestination() {
        fieldDestination.pseudoClassStateChanged(errorClass, !isValidDestination());
    }


    /**
     * Indicates whether or not the Stops field contains valid data.
     */
    private void indicateStops() {
        fieldStops.pseudoClassStateChanged(errorClass, !isValidStops());
    }


    /**
     * Indicates whether or not the Equipment field contains valid data.
     */
    private void indicateEquipment() {
        fieldEquipment.pseudoClassStateChanged(errorClass, !isValidEquipment());
    }


    /**
     * Checks that the Airline being being edited is valid.
     *
     * @return true if valid else false
     */
    private boolean isValidAirline() {
        return validateAirlineIATA(fieldAirline.getText()) || validateAirportICAO(fieldAirline.getText());
    }


    /**
     * Checks that the Source  being edited is valid.
     *
     * @return true if valid else false
     */
    private boolean isValidSource() {
        return validateAirportIATA(fieldSource.getText()) || validateAirportICAO(fieldSource.getText());
    }


    /**
     * Checks that the IATA being edited is valid.
     *
     * @return true if valid else false
     */
    private boolean isValidDestination() {
        return validateAirportIATA(fieldDestination.getText()) || validateAirportICAO(fieldDestination.getText());
    }


    /**
     * Checks that the Stops being being edited is valid.
     *
     * @return true if valid else false
     */
    private boolean isValidStops() {
        return validateInteger(fieldStops.getText());
    }


    /**
     * Checks that the Equipment being edited is valid.
     *
     * @return true if valid else false
     */
    private boolean isValidEquipment() {
        return validateRouteEquipmentList(fieldEquipment.getText());
    }
}
