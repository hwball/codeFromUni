package seng202.group6.controller;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.List;

import javafx.beans.binding.BooleanBinding;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.RadioButton;
import javafx.scene.control.Toggle;
import javafx.scene.control.ToggleGroup;
import javafx.scene.image.Image;
import javafx.scene.text.Text;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import seng202.group6.DataContainer;
import seng202.group6.NoRecordsException;
import seng202.group6.importer.AirlineImporter;
import seng202.group6.importer.AirportImporter;
import seng202.group6.importer.FlightImporter;
import seng202.group6.importer.RouteImporter;
import seng202.group6.model.Airline;
import seng202.group6.model.AirlineList;
import seng202.group6.model.Airport;
import seng202.group6.model.AirportList;
import seng202.group6.model.Flight;
import seng202.group6.model.FlightList;
import seng202.group6.model.RecordList;
import seng202.group6.model.Route;
import seng202.group6.model.RouteList;

/**
 * Controller class for the importer GUI.
 */
public class ImporterController {
    @FXML
    private ComboBox<String> listComboBox;
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
    private Text pathText;
    @FXML
    private Label mainTextDisplay;
    @FXML
    private Button selectButton;
    @FXML
    private Button cancelButton;

    /**
     * The data container to import to.
     */
    private DataContainer dataContainer;

    private ArrayList<Airline> validAirlines;
    private ArrayList<Airport> validAirports;
    private ArrayList<Flight> validFlights;
    private ArrayList<Route> validRoutes;
    private ArrayList<String[]> invalidRecords;

    /**
     * The imported file.
     */
    private File inFile;


    /**
     * Opens a FileChooser, and invokes importer to read in valid and invalid records. Triggered by
     * select file button in GUI.
     */
    public void selectFile() {
        Stage chooserStage = new Stage();
        FileChooser fileChooser = new FileChooser();
        fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("Data Files", "*.dat", "*.csv"));
        fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("All Files", "*.*"));
        fileChooser.setTitle("Open " + ((RadioButton) toggleGroupDataType.getSelectedToggle()).getText() + " File");
        inFile = fileChooser.showOpenDialog(chooserStage);
        try {
            pathText.setText(inFile.toString());
        } catch (NullPointerException e) {
            // Cancelled file choice
        }
        readFile();
    }


    /**
     * Create a reader for the file and pass to to the correct data importer.
     */
    private void readFile() {
        if (inFile != null) {
            try {
                BufferedReader bufferedReader = new BufferedReader(new FileReader(inFile));

                Toggle currentToggle = toggleGroupDataType.getSelectedToggle();
                if (currentToggle == radioAirline) {
                    AirlineImporter importerAirline = new AirlineImporter(bufferedReader);
                    validAirlines = importerAirline.getAll();
                    invalidRecords = importerAirline.getInvalidRecords();
                } else if (currentToggle == radioAirport) {
                    AirportImporter importerAirport = new AirportImporter(bufferedReader);
                    validAirports = importerAirport.getAll();
                    invalidRecords = importerAirport.getInvalidRecords();
                } else if (currentToggle == radioFlight) {
                    FlightImporter importerFlight = new FlightImporter(bufferedReader);
                    validFlights.add(importerFlight.getFlight());
                    invalidRecords = importerFlight.getInvalidRecords();
                } else if (currentToggle == radioRoute) {
                    RouteImporter importerRoute = new RouteImporter(bufferedReader);
                    validRoutes = importerRoute.getAll();
                    invalidRecords = importerRoute.getInvalidRecords();
                }
            } catch (FileNotFoundException e) {
                System.err.println("File not found: " + inFile);
                e.printStackTrace();
            } catch (NullPointerException e) {
                e.printStackTrace();
            }
        }
        displayStatus();
    }


    /**
     * Updates the status pane.
     */
    private void displayStatus() {
        String toBeDisplayed = "";
        if (toggleGroupDataType.getSelectedToggle() == null) {
            toBeDisplayed = "Please select record type.\n";
        }
        if (inFile == null) {
            mainTextDisplay.setText(toBeDisplayed + "Please select a file to import\n");
        } else {
            this.showInvalidRecords();
        }
    }


    /**
     * Shows all invalid records in the main text display.
     */
    private void showInvalidRecords() {
        // indices for invalid lines from file importers
        final int ERROR = 0;
        final int LINE = 1;

        StringBuilder text = new StringBuilder();

        if (toggleGroupDataType.getSelectedToggle() != null) {
            Toggle currentToggle = toggleGroupDataType.getSelectedToggle();
            if (currentToggle == radioAirline) {
                text.append(validAirlines.size());
                text.append(" valid airline records.\n\n");
            } else if (currentToggle == radioAirport) {
                text.append(validAirports.size());
                text.append(" valid airport records.\n\n");
            } else if (currentToggle == radioFlight) {
                if (invalidRecords.size() == 0) {
                    text.append("1 valid flight record.\n\n");
                } else {
                    text.append("Invalid flight record!\n\n");
                }

            } else if (currentToggle == radioRoute) {
                if (invalidRecords.size() == 0) {
                    text.append(validRoutes.size());
                    text.append(" valid route records.\n\n");
                }
            }
        }

        // Build info string for invalid records
        text.append(invalidRecords.size());
        text.append(" invalid lines:\n");
        for (String[] erroneous : invalidRecords) {
            text.append(erroneous[ERROR]);
            text.append(": ");
            text.append(erroneous[LINE]);
            text.append("\n");
        }

        mainTextDisplay.setText(text.toString());
    }


    /**
     * Uses valid records to create new list in data container or appends them to an existing list,
     * then closes importer GUI. Triggered by import button in GUI.
     */
    public void importData() {
        if (toggleGroupDataType.getSelectedToggle() == null) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("No data to import!");
            ((Stage) alert.getDialogPane().getScene().getWindow()).getIcons()
                    .add(new Image(getClass().getClassLoader().getResourceAsStream("icon.png")));
            alert.setHeaderText(null);
            alert.setContentText("No record type selected");
            alert.showAndWait();
        } else if (inFile == null) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("No data to import!");
            ((Stage) alert.getDialogPane().getScene().getWindow()).getIcons()
                    .add(new Image(getClass().getClassLoader().getResourceAsStream("icon.png")));
            alert.setHeaderText(null);
            alert.setContentText("No file selected");
            alert.showAndWait();
        } else if (toggleGroupDataType.getSelectedToggle() == radioFlight &&
                invalidRecords.size() > 0) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Flight Invalid!");
            ((Stage) alert.getDialogPane().getScene().getWindow()).getIcons()
                    .add(new Image(getClass().getClassLoader().getResourceAsStream("icon.png")));
            alert.setHeaderText(null);
            alert.setContentText("Cannot import invalid flight");
            alert.showAndWait();
        } else {
            try {
                if (listComboBox.getSelectionModel().getSelectedIndex() == 0) {
                    addToNewList();
                } else {
                    appendToList();
                }
                closeImporter();
            } catch (NoRecordsException e) {
                Alert alert = new Alert(Alert.AlertType.WARNING);
                alert.setTitle("No data to import!");
                ((Stage) alert.getDialogPane().getScene().getWindow()).getIcons()
                        .add(new Image(getClass().getClassLoader().getResourceAsStream("icon.png")));
                alert.setHeaderText(null);
                alert.setContentText("No valid records present in selected file!");
                alert.showAndWait();
            }
        }

    }


    /**
     * Creates a new record list from the valid records and adds it to the data container.
     *
     * @throws NoRecordsException when there are no valid records
     */
    private void addToNewList() throws NoRecordsException {
        Toggle toggle = toggleGroupDataType.getSelectedToggle();
        if (toggle == radioAirline) {
            if (validAirlines.size() == 0) {
                throw new NoRecordsException();
            }
            AirlineList airlineList = new AirlineList(inFile.getName(), validAirlines);
            dataContainer.addAirlineList(airlineList);
            dataContainer.setActiveAirlineList(dataContainer.getAirlineLists().size() - 1);

        } else if (toggle == radioAirport) {
            if (validAirports.size() == 0) {
                throw new NoRecordsException();
            }
            AirportList airportList = new AirportList(inFile.getName(), validAirports);
            dataContainer.addAirportList(airportList);
            dataContainer.setActiveAirportList(dataContainer.getAirportLists().size() - 1);

        } else if (toggle == radioFlight) {
            if (validFlights.size() == 0) {
                throw new NoRecordsException();
            }
            FlightList flightList = new FlightList(inFile.getName(), validFlights);
            dataContainer.addFlightList(flightList);
            dataContainer.setActiveFlightList(dataContainer.getFlightLists().size() - 1);

        } else if (toggle == radioRoute) {
            if (validRoutes.size() == 0) {
                throw new NoRecordsException();
            }
            RouteList routeList = new RouteList(inFile.getName(), validRoutes);
            dataContainer.addRouteList(routeList);
            dataContainer.setActiveRouteList(dataContainer.getRouteLists().size() - 1);
        }
    }


    /**
     * Adds the valid records to an existing list in the data container.
     *
     * @throws NoRecordsException when there are no valid records
     */
    private void appendToList() throws NoRecordsException {
        int index = listComboBox.getSelectionModel().getSelectedIndex() - 1;
        Toggle toggle = toggleGroupDataType.getSelectedToggle();
        if (toggle == radioAirline) {
            if (validAirlines.size() == 0) {
                throw new NoRecordsException();
            }
            dataContainer.getAirlineList(index).addAll(validAirlines);
            dataContainer.setActiveAirlineList(index);

        } else if (toggle == radioAirport) {
            if (validAirports.size() == 0) {
                throw new NoRecordsException();
            }
            dataContainer.getAirportList(index).addAll(validAirports);
            dataContainer.setActiveAirportList(index);

        } else if (toggle == radioFlight) {
            if (validFlights.size() == 0) {
                throw new NoRecordsException();
            }
            dataContainer.getFlightList(index).addAll(validFlights);
            dataContainer.setActiveFlightList(index);

        } else if (toggle == radioRoute) {
            if (validRoutes.size() == 0) {
                throw new NoRecordsException();
            }
            dataContainer.getRouteList(index).addAll(validRoutes);
            dataContainer.setActiveRouteList(index);
        }
    }


    /**
     * Resets the GUI to allow for selecting a different type. Re-reads the file using the importer
     * (newly) selected type.
     */
    private void resetType() {
        populateLists();
        validAirlines = new ArrayList<>();
        validAirports = new ArrayList<>();
        validFlights = new ArrayList<>();
        validRoutes = new ArrayList<>();
        invalidRecords = new ArrayList<>();
        listComboBox.getSelectionModel().selectFirst();
        readFile();
    }


    /**
     * Populates the list selection dropdown based on the currently selected record type and the
     * lists in the data container.
     */
    private void populateLists() {
        listComboBox.getSelectionModel().clearSelection();
        listComboBox.getItems().clear();
        listComboBox.getItems().add("Create New List");

        if (dataContainer != null) {
            List<? extends RecordList> lists = new ArrayList<>();
            Toggle toggle = toggleGroupDataType.getSelectedToggle();
            if (toggle != null) {
                if (toggle == radioAirline) {
                    lists = dataContainer.getAirlineLists();
                } else if (toggle == radioAirport) {
                    lists = dataContainer.getAirportLists();
                } else if (toggle == radioFlight) {
                    lists = dataContainer.getFlightLists();
                } else if (toggle == radioRoute) {
                    lists = dataContainer.getRouteLists();
                }
                for (RecordList list : lists) {
                    listComboBox.getItems().add(list.getName());
                }
            }
        }
    }


    /**
     * Closes the file importer.
     */
    public void closeImporter() {
        Stage st = (Stage) cancelButton.getScene().getWindow();
        st.hide();
    }


    /**
     * Sets the data container and main controller references. Should be called once after fxml
     * loaded and before stage shown.
     *
     * @param dataContainer the reference to the data container the class uses
     */
    public void setUp(DataContainer dataContainer) {
        this.dataContainer = dataContainer;
        resetType();
    }


    /**
     * Initialization method.
     */
    @SuppressWarnings("unused")
    @FXML
    private void initialize() {
        pathText.setText("");
        toggleGroupDataType.selectedToggleProperty().addListener(v -> resetType());

        // only let user select file after selecting data type
        selectButton.disableProperty().bind(new BooleanBinding() {
            {
                super.bind(toggleGroupDataType.selectedToggleProperty());
            }

            @Override
            protected boolean computeValue() {
                return toggleGroupDataType.getSelectedToggle() == null;
            }
        });
    }
}