package seng202.group6.controller;

import java.io.IOException;
import java.util.Observable;
import java.util.Observer;
import java.util.Set;
import java.util.TreeSet;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.image.Image;
import javafx.stage.Modality;
import javafx.stage.Stage;
import seng202.group6.DataContainer;
import seng202.group6.model.AirlineFilter;
import seng202.group6.model.AirlineList;
import seng202.group6.model.AirportFilter;
import seng202.group6.model.AirportList;
import seng202.group6.model.Flight;
import seng202.group6.model.FlightFilter;
import seng202.group6.model.FlightList;
import seng202.group6.model.Route;
import seng202.group6.model.RouteFilter;
import seng202.group6.model.RouteList;
import seng202.group6.utils.RecordType;

/**
 * The controller class to handle the Basic Filter GUI.
 */
public class BasicFilterController implements Observer {
    //filtering combo boxes
    @FXML
    @SuppressWarnings("unused")
    private ComboBox<String> airportComboCountry;
    @FXML
    @SuppressWarnings("unused")
    private ComboBox<String> airlineComboCountry;
    @FXML
    @SuppressWarnings("unused")
    private ComboBox<String> airlineComboStatus;
    @FXML
    @SuppressWarnings("unused")
    private ComboBox<String> routeComboDep;
    @FXML
    @SuppressWarnings("unused")
    private ComboBox<String> routeComboDest;
    @FXML
    @SuppressWarnings("unused")
    private ComboBox<String> routeComboDirect;
    @FXML
    @SuppressWarnings("unused")
    private ComboBox<String> routeComboEquip;
    @FXML
    @SuppressWarnings("unused")
    private ComboBox<String> flightComboDep;
    @FXML
    @SuppressWarnings("unused")
    private ComboBox<String> flightComboDest;

    @FXML
    private Button buttonApply;
    @FXML
    private Button buttonClear;

    /**
     * The data container to be filtered.
     */
    private DataContainer dataContainer;


    /**
     * Disables the basic filter when when the advanced filter is in use.
     *
     * @param advancedFiltered boolean stating if advance filter in use
     */
    void setAdvancedFiltered(boolean advancedFiltered) {
        buttonApply.setDisable(advancedFiltered);

        airportComboCountry.setDisable(advancedFiltered);
        airlineComboCountry.setDisable(advancedFiltered);
        airlineComboStatus.setDisable(advancedFiltered);
        routeComboDep.setDisable(advancedFiltered);
        routeComboDest.setDisable(advancedFiltered);
        routeComboDirect.setDisable(advancedFiltered);
        routeComboEquip.setDisable(advancedFiltered);
        flightComboDep.setDisable(advancedFiltered);
        flightComboDest.setDisable(advancedFiltered);

        buttonClear.setDisable(!advancedFiltered);

    }


    /**
     * Java FX initialization method. Sets the fixed-content combo boxes.
     */
    @FXML
    @SuppressWarnings("unused")
    private void initialize() {
        routeComboDirect.getItems().clear();
        airlineComboStatus.getItems().clear();
        airlineComboStatus.getItems().addAll("[Status]", "Active", "Inactive");
        routeComboDirect.getItems().addAll("[Direct / Indirect]", "Direct", "Indirect");

        buttonClear.setDisable(true);
    }


    /**
     * Sets the data container to be filtered.
     *
     * @param dataContainer the data container used by the program
     */
    public void setUp(DataContainer dataContainer) {
        this.dataContainer = dataContainer;
        dataContainer.addObserver(this);
    }


    @Override
    public void update(Observable o, Object arg) {
        if (o == dataContainer) {
            if (arg instanceof AirlineList || arg == RecordType.AIRLINE) {
                updateAirlineComboBoxes();
            } else if (arg instanceof AirportList || arg == RecordType.AIRPORT) {
                updateAirportComboBoxes();
            } else if (arg instanceof FlightList || arg == RecordType.FLIGHT) {
                updateFlightComboBoxes();
            } else if (arg instanceof RouteList || arg == RecordType.ROUTE) {
                updateRouteComboBoxes();
            }
        }
    }


    /**
     * Builds filters form the values in the combo boxes and passes it to the data container/
     */
    @FXML
    @SuppressWarnings("unused")
    private void applyFilter() {
        AirportFilter airportFilter = new AirportFilter();
        AirlineFilter airlineFilter = new AirlineFilter();
        RouteFilter routeFilter = new RouteFilter();
        FlightFilter flightFilter = new FlightFilter();

        // Airport
        if (!airportComboCountry.getSelectionModel().isEmpty() &&
                !airportComboCountry.getValue().startsWith("[")) {
            airportFilter.addCountry(airportComboCountry.getValue());
        }

        //Airline
        if (!airlineComboCountry.getSelectionModel().isEmpty() &&
                !airlineComboCountry.getValue().startsWith("[")) {
            airlineFilter.addCountry(airlineComboCountry.getValue());
        }
        if (!airlineComboStatus.getSelectionModel().isEmpty() &&
                !airlineComboStatus.getValue().startsWith("[")) {
            if (airlineComboStatus.getValue().equals("Active")) {
                airlineFilter.setActive(true);
            } else if (airlineComboStatus.getValue().equals("Inactive")) {
                airlineFilter.setActive(false);
            }
        }

        // Route
        if (!routeComboDep.getSelectionModel().isEmpty() &&
                !routeComboDep.getValue().startsWith("[")) {
            routeFilter.addSourceAirport(routeComboDep.getValue());
        }
        if (!routeComboDest.getSelectionModel().isEmpty() &&
                !routeComboDest.getValue().startsWith("[")) {
            routeFilter.addDestinationAirport(routeComboDest.getValue());
        }
        if (!routeComboDirect.getSelectionModel().isEmpty() &&
                !routeComboDirect.getValue().startsWith("[")) {

            switch (routeComboDirect.getValue()) {
                case "Direct":
                    routeFilter.setStops(0, 0);
                    break;
                case "Indirect":
                    routeFilter.setStops(1, null);
                    break;
            }
        }
        if (!routeComboEquip.getSelectionModel().isEmpty() &&
                !routeComboEquip.getValue().startsWith("[")) {
            routeFilter.addEquipment(routeComboEquip.getValue());
        }

        //Flight
        if (!flightComboDep.getSelectionModel().isEmpty() &&
                !flightComboDep.getValue().startsWith("[")) {
            flightFilter.addSourceAirport(flightComboDep.getValue());
        }
        if (!flightComboDest.getSelectionModel().isEmpty() &&
                !flightComboDest.getValue().startsWith("[")) {
            flightFilter.addDestinationAirport(flightComboDest.getValue());
        }

        dataContainer.setFilters(airportFilter, airlineFilter, routeFilter, flightFilter);
        dataContainer.applyFilters();

        buttonClear.setDisable(false);
    }


    /**
     * Opens the advanced filter window.
     */
    @FXML
    @SuppressWarnings("unused")
    private void openAdvancedFilter() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getClassLoader().getResource("AdvFilter.fxml"));
            Parent root = loader.load();
            AdvFilterController controller = loader.getController();
            controller.setUp(dataContainer, this);

            Stage advFilStage = new Stage();
            advFilStage.initModality(Modality.APPLICATION_MODAL);
            advFilStage.setResizable(false);
            advFilStage.setTitle("Advanced Filter");
            advFilStage.getIcons()
                    .add(new Image(getClass().getClassLoader().getResourceAsStream("icon.png")));
            advFilStage.setScene(new Scene(root, 850, 450));
            advFilStage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    /**
     * Updates the combo boxes with data from the tables. Data is ordered alphabetically.
     */
    @FXML
    @SuppressWarnings("unused")
    private void updateComboBoxes() {
        updateAirlineComboBoxes();
        updateAirportComboBoxes();
        updateFlightComboBoxes();
        updateRouteComboBoxes();
    }


    /**
     * Updates the airline combo boxes, setting the first entry to [&lt;search type&gt;].
     */
    private void updateAirlineComboBoxes() {
        airlineComboCountry.getItems().clear();
        airlineComboCountry.getItems().add("[Country]");

        Set<String> airlineCountries = new TreeSet<>(String::compareTo);
        if (dataContainer.getActiveAirlineList() != null) {
            dataContainer.getActiveAirlineList().getRecords().forEach(
                    airline -> airlineCountries.add(airline.getCountry()));
        }
        airlineComboCountry.getItems().addAll(airlineCountries);
    }


    /**
     * Updates the airport combo boxes, setting the first entry to [&lt;search type&gt;].
     */
    private void updateAirportComboBoxes() {
        airportComboCountry.getItems().clear();
        airportComboCountry.getItems().add("[Country]");

        Set<String> airportCountries = new TreeSet<>(String::compareTo);
        if (dataContainer.getActiveAirportList() != null) {
            dataContainer.getActiveAirportList().getRecords().forEach(
                    airport -> airportCountries.add(airport.getCountry()));
        }
        airportComboCountry.getItems().addAll(airportCountries);
    }


    /**
     * Updates the flight combo boxes, setting the first entry to [&lt;search type&gt;].
     */
    private void updateFlightComboBoxes() {
        flightComboDep.getItems().clear();
        flightComboDest.getItems().clear();
        flightComboDep.getItems().add("[Departure Location]");
        flightComboDest.getItems().add("[Destination Location]");

        Set<String> flightDestIATAs = new TreeSet<>(String::compareTo);
        Set<String> flightDepIATAs = new TreeSet<>(String::compareTo);
        if (dataContainer.getActiveFlightList() != null) {
            for (Flight flight : dataContainer.getActiveFlightList().getRecords()) {
                flightDepIATAs.add(flight.getSourceAirport());
                flightDestIATAs.add(flight.getDestinationAirport());
            }
        }
        flightComboDep.getItems().addAll(flightDepIATAs);
        flightComboDest.getItems().addAll(flightDestIATAs);
    }


    /**
     * Updates the route combo boxes, setting the first entry to [&lt;search type&gt;].
     */
    private void updateRouteComboBoxes() {
        routeComboDep.getItems().clear();
        routeComboDest.getItems().clear();
        routeComboEquip.getItems().clear();
        routeComboDep.getItems().add("[Departure Location]");
        routeComboDest.getItems().add("[Destination Location]");
        routeComboEquip.getItems().add("[Equipment]");

        Set<String> destIATAs = new TreeSet<>(String::compareTo);
        Set<String> depIATAs = new TreeSet<>(String::compareTo);
        Set<String> equipment = new TreeSet<>(String::compareTo);
        if (dataContainer.getActiveRouteList() != null) {
            for (Route route : dataContainer.getActiveRouteList().getRecords()) {
                destIATAs.add(route.getDestinationAirport());
                depIATAs.add(route.getSourceAirport());
                equipment.addAll(route.getEquipment());
            }
        }
        routeComboDest.getItems().addAll(destIATAs);
        routeComboDep.getItems().addAll(depIATAs);
        routeComboEquip.getItems().addAll(equipment);

    }


    /**
     * Clears filters on the data and updates the combo boxes.
     */
    @FXML
    @SuppressWarnings("unused")
    private void clearFilters() {
        setAdvancedFiltered(false);
        routeComboDirect.getSelectionModel().clearSelection();
        airlineComboStatus.getSelectionModel().clearSelection();
        updateComboBoxes();
        dataContainer.setFilters(new AirportFilter(), new AirlineFilter(), new RouteFilter(), new FlightFilter());
        dataContainer.applyFilters();
    }
}
