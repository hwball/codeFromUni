package seng202.group6.controller;

import java.io.IOException;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableRow;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.stage.Modality;
import javafx.stage.Stage;
import seng202.group6.DataContainer;
import seng202.group6.model.Airline;
import seng202.group6.model.AirlineList;
import seng202.group6.model.Airport;
import seng202.group6.model.AirportList;
import seng202.group6.model.Flight;
import seng202.group6.model.FlightList;
import seng202.group6.model.RecordList;
import seng202.group6.model.Route;
import seng202.group6.model.RouteList;
import seng202.group6.utils.RecordType;

/**
 * The controller class to handle the Raw Data Viewer GUI.
 */
public class RawDataViewerController {
    // Raw data tab pane
    @FXML
    @SuppressWarnings("unused")
    private TabPane tabPaneRawData;
    @FXML
    @SuppressWarnings("unused")
    private Tab dataTabAirport;
    @FXML
    @SuppressWarnings("unused")
    private Tab dataTabAirline;
    @FXML
    @SuppressWarnings("unused")
    private Tab dataTabRoute;
    @FXML
    @SuppressWarnings("unused")
    private Tab dataTabFlight;

    // add/edit/delete entry buttons
    @FXML
    @SuppressWarnings("unused")
    private Button buttonAddEntry;
    @FXML
    @SuppressWarnings("unused")
    private Button buttonEditEntry;
    @FXML
    @SuppressWarnings("unused")
    private Button buttonDeleteEntry;

    //airport table
    @FXML
    @SuppressWarnings("unused")
    private TableView<Airport> airportTable;
    @FXML
    @SuppressWarnings("unused")
    private TableColumn<Airport, String> airportTableColName;
    @FXML
    @SuppressWarnings("unused")
    private TableColumn<Airport, String> airportTableColCity;
    @FXML
    @SuppressWarnings("unused")
    private TableColumn<Airport, String> airportTableColCountry;
    @FXML
    @SuppressWarnings("unused")
    private TableColumn<Airport, String> airportTableColIATA;
    @FXML
    @SuppressWarnings("unused")
    private TableColumn<Airport, String> airportTableColICAO;
    @FXML
    @SuppressWarnings("unused")
    private TableColumn<Airport, Double> airportTableColLat;
    @FXML
    @SuppressWarnings("unused")
    private TableColumn<Airport, Double> airportTableColLon;
    @FXML
    @SuppressWarnings("unused")
    private TableColumn<Airport, Double> airportTableColAlt;
    @FXML
    @SuppressWarnings("unused")
    private TableColumn<Airport, String> airportTableColTimeZ;
    @FXML
    @SuppressWarnings("unused")
    private TableColumn<Airport, String> airportTableColDst;
    @FXML
    @SuppressWarnings("unused")
    private TableColumn<Airport, String> airportTableColTimeZDataBase;
    @FXML
    @SuppressWarnings("unused")
    private TableColumn<Airport, Integer> airportTableColNumRoutes;

    //airline table
    @FXML
    @SuppressWarnings("unused")
    private TableView<Airline> airlineTable;
    @FXML
    @SuppressWarnings("unused")
    private TableColumn<Airline, String> airlineTableColName;
    @FXML
    @SuppressWarnings("unused")
    private TableColumn<Airline, String> airlineTableColAlias;
    @FXML
    @SuppressWarnings("unused")
    private TableColumn<Airline, String> airlineTableColIATA;
    @FXML
    @SuppressWarnings("unused")
    private TableColumn<Airline, String> airlineTableColICAO;
    @FXML
    @SuppressWarnings("unused")
    private TableColumn<Airline, String> airlineTableColCall;
    @FXML
    @SuppressWarnings("unused")
    private TableColumn<Airline, String> airlineTableColCountry;
    @FXML
    @SuppressWarnings("unused")
    private TableColumn<Airline, Boolean> airlineTableColAct;

    //route table
    @FXML
    @SuppressWarnings("unused")
    private TableView<Route> routeTable;
    @FXML
    @SuppressWarnings("unused")
    private TableColumn<Route, String> routeTableColAirline;
    @FXML
    @SuppressWarnings("unused")
    private TableColumn<Route, String> routeTableColSource;
    @FXML
    @SuppressWarnings("unused")
    private TableColumn<Route, String> routeTableColDest;
    @FXML
    @SuppressWarnings("unused")
    private TableColumn<Route, String> routeTableColCode;
    @FXML
    @SuppressWarnings("unused")
    private TableColumn<Route, String> routeTableColStops;
    @FXML
    @SuppressWarnings("unused")
    private TableColumn<Route, String> routeTableColEquip;

    //flight table
    @FXML
    @SuppressWarnings("unused")
    private TableView<Flight> flightTable;
    @FXML
    @SuppressWarnings("unused")
    private TableColumn<Flight, String> flightTableColSource;
    @FXML
    @SuppressWarnings("unused")
    private TableColumn<Flight, String> flightTableColDest;

    /**
     * Container for the data to display.
     */
    private DataContainer dataContainer;


    /**
     * Sets the data container and initializes the components dependent on that.
     *
     * @param dataContainer the data container being used by the program
     */
    public void setUp(DataContainer dataContainer) {
        this.dataContainer = dataContainer;
        initialiseTables();
    }


    /**
     * Indicate the currently selected tab in the raw data tab pane.
     *
     * @return Which tab is currently selected, or null if none are selected.
     */
    private RecordType currentDataTab() {
        Tab selectedTab = tabPaneRawData.getSelectionModel().getSelectedItem();
        if (selectedTab == dataTabAirline) {
            return RecordType.AIRLINE;
        } else if (selectedTab == dataTabAirport) {
            return RecordType.AIRPORT;
        } else if (selectedTab == dataTabRoute) {
            return RecordType.ROUTE;
        } else if (selectedTab == dataTabFlight) {
            return RecordType.FLIGHT;
        } else {
            return null;
        }
    }


    /**
     * Sets up tables, linking them to the data container.
     */
    private void initialiseTables() {
        setUpAirportTable();
        setUpAirlineTable();
        setUpRouteTable();
        setUpFlightTable();
    }


    /**
     * Sets up the airport table.
     */
    private void setUpAirportTable() {
        airportTableColName.setCellValueFactory(new PropertyValueFactory<>("name"));
        airportTableColCity.setCellValueFactory(new PropertyValueFactory<>("city"));
        airportTableColCountry.setCellValueFactory(new PropertyValueFactory<>("country"));
        airportTableColIATA.setCellValueFactory(new PropertyValueFactory<>("iata"));
        airportTableColICAO.setCellValueFactory(new PropertyValueFactory<>("icao"));
        airportTableColLat.setCellValueFactory(new PropertyValueFactory<>("latitude"));
        airportTableColLon.setCellValueFactory(new PropertyValueFactory<>("longitude"));
        airportTableColAlt.setCellValueFactory(new PropertyValueFactory<>("altitude"));
        airportTableColNumRoutes.setCellValueFactory(new PropertyValueFactory<>("numRoutes"));
        airportTableColTimeZ.setCellValueFactory(new PropertyValueFactory<>("timeZone"));
        airportTableColDst.setCellValueFactory(new PropertyValueFactory<>("dst"));
        airportTableColTimeZDataBase.setCellValueFactory(new PropertyValueFactory<>("timeZoneDatabase"));
        airportTable.setItems(dataContainer.getAirportData());
    }


    /**
     * Sets up the airline table.
     */
    private void setUpAirlineTable() {
        airlineTableColName.setCellValueFactory(new PropertyValueFactory<>("name"));
        airlineTableColAlias.setCellValueFactory(new PropertyValueFactory<>("alias"));
        airlineTableColIATA.setCellValueFactory(new PropertyValueFactory<>("iata"));
        airlineTableColICAO.setCellValueFactory(new PropertyValueFactory<>("icao"));
        airlineTableColCall.setCellValueFactory(new PropertyValueFactory<>("callSign"));
        airlineTableColCountry.setCellValueFactory(new PropertyValueFactory<>("country"));
        airlineTableColAct.setCellValueFactory(new PropertyValueFactory<>("active"));
        airlineTable.setItems(dataContainer.getAirlineData());
    }


    /**
     * Sets up the route table.
     */
    private void setUpRouteTable() {
        routeTableColAirline.setCellValueFactory(new PropertyValueFactory<>("airline"));
        routeTableColCode.setCellValueFactory(new PropertyValueFactory<>("codeShare"));
        routeTableColDest.setCellValueFactory(new PropertyValueFactory<>("destinationAirport"));
        routeTableColEquip.setCellValueFactory(new PropertyValueFactory<>("equipment"));
        routeTableColSource.setCellValueFactory(new PropertyValueFactory<>("sourceAirport"));
        routeTableColStops.setCellValueFactory(new PropertyValueFactory<>("stops"));
        routeTable.setItems(dataContainer.getRouteData());
    }


    /**
     * Sets up the flight table.
     */
    private void setUpFlightTable() {
        flightTableColSource.setCellValueFactory(new PropertyValueFactory<>("sourceAirport"));
        flightTableColDest.setCellValueFactory(new PropertyValueFactory<>("destinationAirport"));
        flightTable.setItems(dataContainer.getFlightData());

        //shows extra detail when table entry double clicked
        flightTable.setRowFactory(tv -> {
            TableRow<Flight> flightRow = new TableRow<>();
            flightRow.setOnMouseClicked(event -> {
                if (event.getClickCount() == 2 && (!flightRow.isEmpty())) {
                    flightExDetail();
                }
            });
            return flightRow;
        });
    }


    /**
     * Method to edit a single data entry.
     */
    @FXML
    @SuppressWarnings("unused")
    private void editEntry() {
        RecordType dataType = currentDataTab();
        int selectedIndex = -1;
        RecordList currList = null;

        if (dataType != null) {
            switch (dataType) {
                case AIRPORT:
                    currList = dataContainer.getActiveAirportList();
                    selectedIndex = airportTable.getSelectionModel().getSelectedIndex();
                    break;
                case AIRLINE:
                    currList = dataContainer.getActiveAirlineList();
                    selectedIndex = airlineTable.getSelectionModel().getSelectedIndex();
                    break;
                case ROUTE:
                    currList = dataContainer.getActiveRouteList();
                    selectedIndex = routeTable.getSelectionModel().getSelectedIndex();
                    break;
                case FLIGHT:
                    showWarningFlightEdit();
                    return;
            }
        }

        // Raise alert message and return if no entry has been selected.
        Alert nonSelectAlert = new Alert(Alert.AlertType.ERROR);
        nonSelectAlert.setTitle("Unable to Edit Entry");
        ((Stage) nonSelectAlert.getDialogPane().getScene().getWindow()).getIcons()
                .add(new Image(getClass().getClassLoader().getResourceAsStream("icon.png")));
        nonSelectAlert.setHeaderText(null);
        nonSelectAlert.setContentText("Either there is no data for the\nselected data type" +
                " or a data entry has\nnot been selected.");
        if (selectedIndex == -1) {
            nonSelectAlert.showAndWait();
            return;
        }

        // Open AEWindow with data type, selected index and edit mode.
        openAddEditEntry(currList, selectedIndex);
    }


    /**
     * Warns the user that flights cannot be manually edited.
     */
    private void showWarningFlightEdit() {
        Alert addFilterAlert = new Alert(Alert.AlertType.ERROR);
        addFilterAlert.setTitle("Unable to Add / Edit.");
        ((Stage) addFilterAlert.getDialogPane().getScene().getWindow()).getIcons()
                .add(new Image(getClass().getClassLoader().getResourceAsStream("icon.png")));
        addFilterAlert.setHeaderText(null);
        addFilterAlert.setContentText("Flight data is unable to be manually added or edited.");
        addFilterAlert.showAndWait();
    }


    /**
     * Method to manually add a single data entry.
     */
    @FXML
    @SuppressWarnings("unused")
    private void addEntry() {
        // Declare local variables
        RecordType dataType = currentDataTab();

        RecordList currList = null;
        if (dataType != null) {
            switch (dataType) {
                case AIRPORT:
                    currList = dataContainer.getActiveAirportList();
                    break;
                case AIRLINE:
                    currList = dataContainer.getActiveAirlineList();
                    break;
                case ROUTE:
                    currList = dataContainer.getActiveRouteList();
                    break;
                case FLIGHT:
                    showWarningFlightEdit();
                    return;
            }

            // Open AEWindow with data type, fake index and add mode.
            openAddEditEntry(currList, null);
        }
    }


    /**
     * Opens the add/edit GUI with the appropriate information displayed.
     *
     * @param sourceList the list containing the record under edit or the list to add the new record
     *                   to
     * @param index      the index in the source list of the record under edit, or null if adding a
     *                   new record
     */
    @SuppressWarnings({"ConstantConditions", "unchecked"})
    private void openAddEditEntry(RecordList sourceList, Integer index) {
        if (sourceList == null) {
            Alert addFilterAlert = new Alert(Alert.AlertType.ERROR);
            addFilterAlert.setTitle("No active list!");
            ((Stage) addFilterAlert.getDialogPane().getScene().getWindow()).getIcons()
                    .add(new Image(getClass().getClassLoader().getResourceAsStream("icon.png")));
            addFilterAlert.setHeaderText(null);
            addFilterAlert.setContentText("Please select a list first.");
            addFilterAlert.showAndWait();
            return;
        } else if (sourceList instanceof FlightList) {
            showWarningFlightEdit();
            return;
        }

        try {
            Parent root;
            if (sourceList instanceof AirlineList) {
                FXMLLoader loader = new FXMLLoader(getClass().getClassLoader().getResource("AddEditAirline.fxml"));
                root = loader.load();
                AddEditAirline controller = loader.getController();
                controller.setUp(sourceList, index);

            } else if (sourceList instanceof AirportList) {
                FXMLLoader loader = new FXMLLoader(getClass().getClassLoader().getResource("AddEditAirport.fxml"));
                root = loader.load();
                AddEditAirport controller = loader.getController();
                controller.setUp(sourceList, index);

            } else if (sourceList instanceof RouteList) {
                FXMLLoader loader = new FXMLLoader(getClass().getClassLoader().getResource("AddEditRoute.fxml"));
                root = loader.load();
                AddEditRoute controller = loader.getController();
                controller.setUp(sourceList, index);
            } else {
                throw new IOException();
            }


            Scene scene = new Scene(root);

            scene.getStylesheets().add(getClass().getClassLoader().getResource("text-field-red-border.css").toExternalForm());

            Stage stage = new Stage();
            stage.initModality(Modality.APPLICATION_MODAL);
            stage.setResizable(false);
            stage.setTitle((index == null) ? "Add entry" : "Edit Entry");
            stage.getIcons().add(new Image(getClass().getClassLoader().getResourceAsStream("icon.png")));
            stage.setScene(scene);
            stage.show();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    /**
     * Deletes the selected record.
     */
    @FXML
    @SuppressWarnings({"unused", "cast"})
    private void deleteEntry() {
        // delete the selected entry
        // Local variables
        RecordType dataType = currentDataTab();
        int selectedIndex;

        // Raise alert message and return if no entry has been selected.
        Alert nonSelectAlert = new Alert(Alert.AlertType.ERROR);
        nonSelectAlert.setTitle("Unable to Edit Entry");
        ((Stage) nonSelectAlert.getDialogPane().getScene().getWindow()).getIcons()
                .add(new Image(getClass().getClassLoader().getResourceAsStream("icon.png")));
        nonSelectAlert.setHeaderText(null);
        nonSelectAlert.setContentText("Either there is no data for the\nselected data type" +
                " or a data entry has\nnot been selected.");

        // Get selected data index
        if (dataType == RecordType.AIRPORT) {
            selectedIndex = airportTable.getSelectionModel().getSelectedIndex();
            if (selectedIndex == -1) {
                nonSelectAlert.showAndWait();
                return;
            }
            dataContainer.getActiveAirportList().remove(selectedIndex);
        } else if (dataType == RecordType.AIRLINE) {
            selectedIndex = airlineTable.getSelectionModel().getSelectedIndex();
            if (selectedIndex == -1) {
                nonSelectAlert.showAndWait();
                return;
            }
            dataContainer.getActiveAirlineList().remove(selectedIndex);
        } else if (dataType == RecordType.ROUTE) {
            selectedIndex = routeTable.getSelectionModel().getSelectedIndex();
            if (selectedIndex == -1) {
                nonSelectAlert.showAndWait();
                return;
            }
            dataContainer.getActiveRouteList().remove(selectedIndex);
        }
    }


    /**
     * Shows the flight extra detail window for the flight currently selected in the flight table.
     */
    private void flightExDetail() {
        int index = flightTable.getSelectionModel().getSelectedIndex();
        Flight flight = dataContainer.getFlightData().get(index);

        try {
            FXMLLoader flightLoader = new FXMLLoader(getClass().getClassLoader().getResource("Flight.fxml"));
            Parent flightRoot = flightLoader.load();
            FlightDetailsController controllerFlight = flightLoader.getController();
            controllerFlight.setData(flight);

            Stage stage = new Stage();
            stage.initModality(Modality.APPLICATION_MODAL);
            stage.setResizable(false);
            stage.setTitle("Flight Points Table");
            stage.getIcons().add(new Image(getClass().getClassLoader().getResourceAsStream("icon.png")));
            stage.setScene(new Scene(flightRoot, 600, 400));
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
