package seng202.group6.controller;

import javafx.fxml.FXML;
import javafx.scene.control.TextField;
import seng202.group6.model.Airport;

import static seng202.group6.utils.Verify.validateAirportIATA;
import static seng202.group6.utils.Verify.validateAirportICAO;
import static seng202.group6.utils.Verify.validateDaylightSavingTime;
import static seng202.group6.utils.Verify.validateDecimal;
import static seng202.group6.utils.Verify.validateLatitude;
import static seng202.group6.utils.Verify.validateLongitude;
import static seng202.group6.utils.Verify.validateName;
import static seng202.group6.utils.Verify.validateTimezoneDB;

/**
 * Controller for Airport add/edit window.
 */
public class AddEditAirport extends AddEditController<Airport> {
    @FXML
    private TextField fieldName;
    @FXML
    private TextField fieldCity;
    @FXML
    private TextField fieldCountry;
    @FXML
    protected TextField fieldIATA;
    @FXML
    protected TextField fieldICAO;
    @FXML
    private TextField fieldLatitude;
    @FXML
    private TextField fieldLongitude;
    @FXML
    private TextField fieldAltitude;
    @FXML
    private TextField fieldTimezone;
    @FXML
    private TextField fieldDaylightSavingTime;
    @FXML
    private TextField fieldTimezoneDB;


    @Override
    protected void populateFields() {
        fieldName.setText(recordUnderEdit.getName());
        fieldCity.setText(recordUnderEdit.getCity());
        fieldCountry.setText(recordUnderEdit.getCountry());
        fieldIATA.setText(recordUnderEdit.getIata());
        fieldICAO.setText(recordUnderEdit.getIcao());
        fieldLatitude.setText(recordUnderEdit.getLatitude().toString());
        fieldLongitude.setText(recordUnderEdit.getLongitude().toString());
        fieldAltitude.setText(recordUnderEdit.getAltitude().toString());
        fieldTimezone.setText(recordUnderEdit.getTimeZone());
        fieldDaylightSavingTime.setText(recordUnderEdit.getDst());
        fieldTimezoneDB.setText(recordUnderEdit.getTimeZoneDatabase());
    }


    @Override
    protected void applyEdit() {
        recordUnderEdit.setName(fieldName.getText());
        recordUnderEdit.setCity(fieldName.getText());
        recordUnderEdit.setCountry(fieldCountry.getText());
        recordUnderEdit.setIata(fieldIATA.getText());
        recordUnderEdit.setIcao(fieldICAO.getText());
        recordUnderEdit.setLatitude(Double.parseDouble(fieldLatitude.getText()));
        recordUnderEdit.setLongitude(Double.parseDouble(fieldLongitude.getText()));
        recordUnderEdit.setAltitude(Double.parseDouble(fieldAltitude.getText()));
        recordUnderEdit.setTimeZone(fieldTimezone.getText());
        recordUnderEdit.setDst(fieldDaylightSavingTime.getText());
        recordUnderEdit.setTimeZoneDatabase(fieldTimezoneDB.getText());
    }


    @Override
    protected void addRecord() {
        sourceList.add(
                new Airport(fieldName.getText(),
                        fieldName.getText(),
                        fieldCountry.getText(),
                        fieldIATA.getText(),
                        fieldICAO.getText(),
                        Double.parseDouble(fieldLatitude.getText()),
                        Double.parseDouble(fieldLongitude.getText()),
                        Double.parseDouble(fieldAltitude.getText()),
                        fieldTimezone.getText(),
                        fieldDaylightSavingTime.getText(),
                        fieldTimezoneDB.getText()));
    }


    @SuppressWarnings("ConstantConditions")
    @Override
    protected boolean validate() {
        return isValidName() && isValidCity() && isValidCountry() && isValidIATA() && isValidICAO()
                && isValidLatitude() && isValidLongitude() && isValidAltitude() && isValidTimezone()
                && isValidDaylightSavingTime() && isValidTimezoneDB();
    }


    @Override
    protected void initialize() {
        fieldName.textProperty().addListener((observable, oldValue, newValue) -> indicateName());
        indicateName();

        fieldCity.textProperty().addListener((observable, oldValue, newValue) -> indicateCity());
        indicateCity();

        fieldCountry.textProperty().addListener((observable, oldValue, newValue) -> indicateCountry());
        indicateCountry();

        fieldIATA.textProperty().addListener((observable, oldValue, newValue) -> indicateIATA());
        indicateIATA();

        fieldICAO.textProperty().addListener((observable, oldValue, newValue) -> indicateICAO());
        indicateICAO();

        fieldLatitude.textProperty().addListener((observable, oldValue, newValue) -> indicateLatitude());
        indicateLatitude();

        fieldLongitude.textProperty().addListener((observable, oldValue, newValue) -> indicateLongitude());
        indicateLongitude();

        fieldAltitude.textProperty().addListener((observable, oldValue, newValue) -> indicateAltitude());
        indicateAltitude();

        fieldTimezone.textProperty().addListener((observable, oldValue, newValue) -> indicateTimezone());
        indicateTimezone();

        fieldDaylightSavingTime.textProperty().addListener((observable, oldValue, newValue) -> indicateDaylightSavingTime());
        indicateDaylightSavingTime();

        fieldTimezoneDB.textProperty().addListener((observable, oldValue, newValue) -> indicateTimezoneDB());
        indicateTimezoneDB();

    }


    /**
     * Indicates whether or not the TimezoneDB field contains valid data.
     */
    private void indicateTimezoneDB() {
        fieldTimezoneDB.pseudoClassStateChanged(errorClass, !isValidTimezoneDB());
    }


    /**
     * Indicates whether or not the DST field contains valid data.
     */
    private void indicateDaylightSavingTime() {
        fieldDaylightSavingTime.pseudoClassStateChanged(errorClass, !isValidDaylightSavingTime());
    }


    /**
     * Indicates whether or not the Timezone entry field contains valid data.
     */
    private void indicateTimezone() {
        fieldTimezone.pseudoClassStateChanged(errorClass, !isValidTimezone());
    }


    /**
     * Indicates whether or not the Longitude entry field contains valid data.
     */
    private void indicateLongitude() {
        fieldLongitude.pseudoClassStateChanged(errorClass, !isValidLongitude());
    }


    /**
     * Indicates whether or not the Latitude entry fiend contains valid data.
     */
    private void indicateLatitude() {
        fieldLatitude.pseudoClassStateChanged(errorClass, !isValidLatitude());
    }


    /**
     * Indicates whether or not the ICAO field contains valid data.
     */
    private void indicateICAO() {
        fieldICAO.pseudoClassStateChanged(errorClass, !isValidICAO());
    }


    /**
     * Indicates whether or not the IATA field contains valid data.
     */
    private void indicateIATA() {
        fieldIATA.pseudoClassStateChanged(errorClass, !isValidIATA());
    }


    /**
     * Indicates whether or not the Country field contains valid data.
     */
    private void indicateCountry() {
        fieldCountry.pseudoClassStateChanged(errorClass, !isValidCountry());
    }


    /**
     * Indicates whether or not the City entry field contains valid data.
     */
    private void indicateCity() {
        fieldCity.pseudoClassStateChanged(errorClass, !isValidCity());
    }


    /**
     * Indicates whether or not the Name entry field contains valid data.
     */
    private void indicateName() {
        fieldName.pseudoClassStateChanged(errorClass, !isValidName());
    }


    /**
     * Indicates whether or not the Altitude entry field contains valid data.
     */
    private void indicateAltitude() {
        fieldAltitude.pseudoClassStateChanged(errorClass, !isValidAltitude());
    }


    /**
     * Checks that that the Name being edited is valid
     *
     * @return True if valid else false
     */
    private boolean isValidName() {
        return validateName(fieldName.getText());
    }


    /**
     * Checks that the City being edited is valid
     *
     * @return True if valid else false
     */
    private boolean isValidCity() {
        return validateName(fieldCity.getText());
    }


    /**
     * Checks that the Country being edited is valid
     *
     * @return True if valid else false
     */
    private boolean isValidCountry() {
        return validateName(fieldCountry.getText());
    }


    /**
     * Checks that the Airport being edited is valid.
     *
     * @return True if valid else false
     */
    private boolean isValidIATA() {
        return validateAirportIATA(fieldIATA.getText());
    }


    /**
     * Checks that the ICAO being edited is valid.
     *
     * @return True if valid else false
     */
    private boolean isValidICAO() {
        return validateAirportICAO(fieldICAO.getText());
    }


    /**
     * Checks that the Latitude being edited is valid.
     *
     * @return True if valid else false
     */
    private boolean isValidLatitude() {
        return validateLatitude(fieldLatitude.getText());
    }


    /**
     * Checks that the Longitude being edited is valid.
     *
     * @return True if valid else false
     */
    private boolean isValidLongitude() {
        return validateLongitude(fieldLongitude.getText());
    }


    /**
     * Checks that the Altitude being edited is valid.ata
     *
     * @return True if valid else false
     */
    private boolean isValidAltitude() {
        return validateDecimal(fieldAltitude.getText());
    }


    /**
     * Checks that the Timezone being edited is valid.
     *
     * @return True if valid else false
     */
    private boolean isValidTimezone() {
        return validateDecimal(fieldTimezone.getText());
    }


    /**
     * Checks that the DST being edited is valid.
     *
     * @return True if valid else false
     */
    private boolean isValidDaylightSavingTime() {
        return validateDaylightSavingTime(fieldDaylightSavingTime.getText());
    }


    /**
     * Checks that the TimezoneDB being edited is valid.a
     *
     * @return True if valid else false
     */
    private boolean isValidTimezoneDB() {
        return validateTimezoneDB(fieldTimezoneDB.getText());
    }
}
