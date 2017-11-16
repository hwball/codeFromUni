package seng202.group6.controller;

import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.Set;
import java.util.TreeSet;

import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.ComboBox;
import javafx.scene.control.RadioMenuItem;
import javafx.scene.control.TextField;
import javafx.scene.control.ToggleGroup;
import javafx.scene.image.Image;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import seng202.group6.model.Airport;
import seng202.group6.model.AirportList;
import seng202.group6.utils.DistanceCalculator;

/**
 * Controller for the distance window which displays the distance between two airports.
 */
public class DistanceController {
    @FXML
    private TextField sourceLonTextField;
    @FXML
    private TextField sourceLatTextField;
    @FXML
    private TextField destLonTextField;
    @FXML
    private TextField destLatTextField;
    @FXML
    private TextField sourceHeiTextField;
    @FXML
    private TextField destHeiTextField;

    @FXML
    private RadioMenuItem metRadioMenuItem;
    @FXML
    private RadioMenuItem impRadioMenuItem;

    @FXML
    private Text resultText;

    // Combo boxes mix Strings and Airports. Bad.
    @FXML
    private ComboBox sourceAirportCombo;
    @FXML
    private ComboBox destAirportCombo;

    private final ToggleGroup unitToggleGroup = new ToggleGroup();


    /**
     * Populates the airport selection combo boxes from the given airportList.
     *
     * @param airportList a list of airports to calculate distance between
     */
    public void setUp(AirportList airportList) {
        Set<Airport> sourceAirports = new TreeSet<>();
        Set<Airport> destinationAirports = new TreeSet<>();

        sourceAirportCombo.getItems().clear();
        destAirportCombo.getItems().clear();

        sourceAirportCombo.getItems().add("[Source Airport]");
        destAirportCombo.getItems().add("[Destination Airport]");

        if (airportList != null) {
            for (Airport airport : airportList.getRecords()) {
                sourceAirports.add(airport);
                destinationAirports.add(airport);
            }

            sourceAirportCombo.getItems().addAll(sourceAirports);
            destAirportCombo.getItems().addAll(destinationAirports);
        }
    }


    /**
     * Populates the text fields with information from the selected airport in the source combo
     * box.
     */
    public void sourceSelected() {
        if (!sourceAirportCombo.getValue().toString().startsWith("[")) {
            Airport source = (Airport) sourceAirportCombo.getValue();
            sourceLatTextField.setText(source.getLatitude().toString());
            sourceLonTextField.setText(source.getLongitude().toString());
            sourceHeiTextField.setText(source.getAltitude().toString());
            sourceHeiTextField.setDisable(true);
            sourceLonTextField.setDisable(true);
            sourceLatTextField.setDisable(true);
        } else {
            sourceHeiTextField.setDisable(false);
            sourceLonTextField.setDisable(false);
            sourceLatTextField.setDisable(false);
        }
    }


    /**
     * Populates the text fields with information from the selected airport in the destination combo
     * box.
     */
    public void destinationSelected() {
        if (!destAirportCombo.getValue().toString().startsWith("[")) {
            Airport destination = (Airport) destAirportCombo.getValue();
            destLatTextField.setText(destination.getLatitude().toString());
            destLonTextField.setText(destination.getLongitude().toString());
            destHeiTextField.setText(destination.getAltitude().toString());
            destHeiTextField.setDisable(true);
            destLonTextField.setDisable(true);
            destLatTextField.setDisable(true);
        } else {
            destHeiTextField.setDisable(false);
            destLonTextField.setDisable(false);
            destLatTextField.setDisable(false);
        }
    }


    /**
     * Closes the distance window.
     */
    @FXML
    @SuppressWarnings("unused")
    private void close() {
        Stage st = (Stage) destLatTextField.getScene().getWindow();
        st.hide();
    }


    /**
     * Calculates the distance, given the values in the text fields if text fields contain invalid
     * values error message is displayed.
     */
    public void calculate() {
        try {
            Double dLat = Double.parseDouble(destLatTextField.getText());
            Double sLat = Double.parseDouble(sourceLatTextField.getText());

            Double dLon = Double.parseDouble(destLonTextField.getText());
            Double sLon = Double.parseDouble(sourceLonTextField.getText());

            Double sHeight = Double.parseDouble(sourceHeiTextField.getText());
            Double dHeight = Double.parseDouble(destHeiTextField.getText());

            double result = DistanceCalculator.calculateAirpDist(sLon, sLat, sHeight, dLon, dLat, dHeight);

            if (unitToggleGroup.getSelectedToggle().equals(metRadioMenuItem)) {
                result = result / 1000.0;
                NumberFormat formatter = new DecimalFormat("#0.00 km");
                resultText.setText(formatter.format(result));
            } else if (unitToggleGroup.getSelectedToggle().equals(impRadioMenuItem)) {
                result = result * 0.00062137;
                NumberFormat formatter = new DecimalFormat("#0.00 mi");
                resultText.setText(formatter.format(result));
            }

        } catch (NumberFormatException e) {
            Alert lonLatAlert = new Alert(Alert.AlertType.ERROR);
            lonLatAlert.setTitle("Invalid Longitude or Latitude");
            ((Stage) lonLatAlert.getDialogPane().getScene().getWindow()).getIcons()
                    .add(new Image(getClass().getClassLoader().getResourceAsStream("icon.png")));
            lonLatAlert.setHeaderText("One or more entry of Longitude or Latitude are invalid.");
            lonLatAlert.setContentText("Please enter correct values for all fields before pressing Calculate.");
            lonLatAlert.showAndWait();
        }
    }


    /**
     * FXML initialize method that sets up toggle group for units.
     */
    @FXML
    @SuppressWarnings("unused")
    private void initialize() {
        metRadioMenuItem.setToggleGroup(unitToggleGroup);
        impRadioMenuItem.setToggleGroup(unitToggleGroup);
    }

}
