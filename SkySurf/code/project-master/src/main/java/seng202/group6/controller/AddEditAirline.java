package seng202.group6.controller;

import javafx.fxml.FXML;
import javafx.scene.control.CheckBox;
import javafx.scene.control.TextField;
import seng202.group6.model.Airline;

import static seng202.group6.utils.Verify.validateAirlineIATA;
import static seng202.group6.utils.Verify.validateAirlineICAO;
import static seng202.group6.utils.Verify.validateName;

/**
 * Controller for Airline Add/Edit window.
 */
public class AddEditAirline extends AddEditController<Airline> {
    @FXML
    private TextField fieldName;
    @FXML
    private TextField fieldAlias;
    @FXML
    private TextField fieldCallSign;
    @FXML
    private TextField fieldCountry;
    @FXML
    private CheckBox checkActive;
    @FXML
    protected TextField fieldIATA;
    @FXML
    protected TextField fieldICAO;


    @Override
    protected void populateFields() {
        fieldName.setText(recordUnderEdit.getName());
        fieldAlias.setText(recordUnderEdit.getAlias());
        fieldCountry.setText(recordUnderEdit.getCountry());
        fieldIATA.setText(recordUnderEdit.getIata());
        fieldICAO.setText(recordUnderEdit.getIcao());
        fieldCallSign.setText(recordUnderEdit.getCallSign());
        checkActive.setSelected(recordUnderEdit.getActive());
    }


    @Override
    protected void applyEdit() {
        recordUnderEdit.setName(fieldName.getText());
        recordUnderEdit.setAlias(fieldAlias.getText());
        recordUnderEdit.setCountry(fieldCountry.getText());
        recordUnderEdit.setIata(fieldIATA.getText());
        recordUnderEdit.setIcao(fieldICAO.getText());
        recordUnderEdit.setCallSign(fieldCallSign.getText());
        recordUnderEdit.setActive(checkActive.isSelected());
    }


    @Override
    protected void addRecord() {
        sourceList.add(
                new Airline(fieldName.getText(),
                        fieldAlias.getText(),
                        fieldIATA.getText(),
                        fieldICAO.getText(),
                        fieldCallSign.getText(),
                        fieldCountry.getText(),
                        checkActive.isSelected()));
    }


    @SuppressWarnings("ConstantConditions")
    @Override
    protected boolean validate() {
        return isValidName() && isValidAlias() && isValidCountry() && isValidIATA() && isValidICAO()
                && isValidCallSign();
    }


    @Override
    protected void initialize() {
        fieldName.textProperty().addListener((observable, oldValue, newValue) -> indicateName());
        indicateName();

        fieldAlias.textProperty().addListener((observable, oldValue, newValue) -> indicateAlias());
        indicateAlias();

        fieldCountry.textProperty().addListener((observable, oldValue, newValue) -> indicateCountry());
        indicateCountry();

        fieldIATA.textProperty().addListener((observable, oldValue, newValue) -> indicateIATA());
        indicateIATA();

        fieldICAO.textProperty().addListener((observable, oldValue, newValue) -> indicateICAO());
        indicateICAO();

        fieldCallSign.textProperty().addListener((observable, oldValue, newValue) -> indicateCallSign());
        indicateCallSign();

    }


    /**
     * Indicates whether or not the CallSign entry field contains valid data.
     */
    private void indicateCallSign() {
        fieldCallSign.pseudoClassStateChanged(errorClass, !isValidCallSign());
    }


    /**
     * Indicates whether or not the ICAO entry field contains valid data.
     */
    private void indicateICAO() {
        fieldICAO.pseudoClassStateChanged(errorClass, !isValidICAO());
    }


    /**
     * Indicates whether or not the IATA entry field contains valid data.
     */
    private void indicateIATA() {
        fieldIATA.pseudoClassStateChanged(errorClass, !isValidIATA());
    }


    /**
     * Indicates whether or not the Country entry field contains valid data.
     */
    private void indicateCountry() {
        fieldCountry.pseudoClassStateChanged(errorClass, !isValidCountry());
    }


    /**
     * Indicates whether or not the Alias entry field contains valid data.
     */
    private void indicateAlias() {
        fieldAlias.pseudoClassStateChanged(errorClass, !isValidAlias());
    }


    /**
     * Indicates whether or not the Name entry field contains valid data.
     */
    private void indicateName() {
        fieldName.pseudoClassStateChanged(errorClass, !isValidName());
    }


    /**
     * Checks that the Name being edited is valid.
     *
     * @return true if valid else false
     */
    private boolean isValidName() {
        return validateName(fieldName.getText());
    }


    /**
     * Checks that the Alias being edited is valid.
     *
     * @return true if valid else false
     */
    private boolean isValidAlias() {
        return validateName(fieldAlias.getText());
    }


    /**
     * Checks that the Country being edited is valid.
     *
     * @return true if valid else false
     */
    private boolean isValidCountry() {
        return validateName(fieldCountry.getText());
    }


    /**
     * Checks that the IATA being edited is valid.
     *
     * @return true if valid else false
     */
    private boolean isValidIATA() {
        return validateAirlineIATA(fieldIATA.getText());
    }


    /**
     * Checks that the ICAO being edited is valid.
     *
     * @return true if valid else false
     */
    private boolean isValidICAO() {
        return validateAirlineICAO(fieldICAO.getText());
    }


    /**
     * Checks that the CallSign being edited is valid.
     *
     * @return true if valid else false
     */
    private boolean isValidCallSign() {
        return validateName(fieldCallSign.getText());
    }
}
