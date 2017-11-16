package seng202.group6.controller;

import java.util.Collection;
import java.util.HashMap;
import java.util.stream.Collectors;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.Tooltip;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.stage.Stage;
import seng202.group6.DataContainer;
import seng202.group6.model.AirlineFilter;
import seng202.group6.model.AirportFilter;
import seng202.group6.model.FlightFilter;
import seng202.group6.model.RouteFilter;
import seng202.group6.utils.Verify;
import seng202.group6.utils.AdvFilterCondition;
import seng202.group6.utils.RecordType;

/**
 * Controller class for the advanced filter which creates, deletes, views, and applies advanced
 * filters to the data containers lists.
 */
public class AdvFilterController {
    @FXML
    private ComboBox<String> comboDataType;
    @FXML
    private ComboBox<String> comboSearchType;
    @FXML
    private TextField textFieldSearchTerm;
    @FXML
    private Button buttonExit;
    @FXML
    private TableView<AdvFilterCondition> tableFilter;
    @FXML
    private TableColumn<AdvFilterCondition, String> columnDataType;
    @FXML
    private TableColumn<AdvFilterCondition, String> columnSearchType;
    @FXML
    private TableColumn<AdvFilterCondition, String> columnSearchTerm;
    @FXML
    private TextField textFieldMin;
    @FXML
    private TextField textFieldMax;

    private Tooltip termHint;

    /**
     * The list of conditions to display in the table.
     */
    private final ObservableList<AdvFilterCondition> filterConditions = FXCollections.observableArrayList();

    /**
     * Maps search type to tool tip.
     */
    private HashMap<String, String> toolTip;

    /**
     * Maps record type to search types.
     */
    private HashMap<String, String[]> searchTypes;

    /**
     * The data types that can be filtered.
     */
    private RecordType[] dataTypes;

    /**
     * Whether the currently selected search type is a range.
     */
    private boolean isRange = false;

    private DataContainer dataContainer;
    private BasicFilterController basicFilterController;


    /**
     * Accepts a string and converts it to a Double[2], representing a double range. If a number is
     * missing it signifies  +/- infinity as appropriate - this is represented in the output array
     * as a null value.
     *
     * @param string A string representing a double range.
     * @return A Double array representation of the range.
     */
    static Double[] parseRangeDouble(String string) {
        Double[] range = {null, null};
        String[] stringRange = {null, null};

        if (string.indexOf(',') >= 0) {
            stringRange = string.split(",", 2);
        } else if (!string.isEmpty()) {
            stringRange[0] = string;
        }

        for (int i = 0; i < 2; i++) {
            if (stringRange[i] != null && !stringRange[i].isEmpty()) {
                range[i] = Double.parseDouble(stringRange[i]);
            } else {
                range[i] = null;
            }
        }
        return range;
    }


    /**
     * Accepts a string and converts it to a two element Integer[], representing an integer range.
     * If a number is missing it signifies +/- infinity as appropriate - this is represented in the
     * output array as a null value.
     *
     * @param string a string representing the range.
     * @return An Integer array representation of the range.
     */
    static Integer[] parseRangeInteger(String string) {
        Integer[] range = {null, null};
        String[] stringRange = {null, null};

        if (string.indexOf(',') >= 0) {
            stringRange = string.split(",", 2);
        } else if (!string.isEmpty()) {
            stringRange[0] = string;
        }

        for (int i = 0; i < 2; i++) {
            if (stringRange[i] != null && !stringRange[i].isEmpty()) {
                range[i] = Integer.parseInt(stringRange[i].trim());
            } else {
                range[i] = null;
            }
        }
        return range;
    }


    /**
     * Adds a filter to the filter table. Called by the add filter button.
     */
    @FXML
    @SuppressWarnings("unused")
    private void addFilter() {
        if (!isValidFilterCondition()) {
            Alert addFilterAlert = new Alert(Alert.AlertType.ERROR);
            addFilterAlert.setTitle("Unable to add filter");
            ((Stage) addFilterAlert.getDialogPane().getScene().getWindow()).getIcons()
                    .add(new Image(getClass().getClassLoader().getResourceAsStream("icon.png")));
            addFilterAlert.setHeaderText(null);
            addFilterAlert.setContentText("One or more fields are empty or otherwise incorrect.\n" +
                    "Please select values for all fields before\nadding a filter condition.");
            addFilterAlert.showAndWait();
        } else {
            String searchTerm;
            if (isRange) {
                String min = textFieldMin.getText();
                String max = textFieldMax.getText();
                searchTerm = min + ((!max.isEmpty()) ? ", " + max : "");
            } else {
                searchTerm = textFieldSearchTerm.getText();
            }
            filterConditions.add(
                    new AdvFilterCondition(RecordType.parseName(comboDataType.getValue()),
                            comboSearchType.getValue(), searchTerm));
        }
    }


    /**
     * Checks whether the current fields constitute a valid filter condition.
     *
     * @return Whether the current fields constitute a valid filter condition.
     */
    private boolean isValidFilterCondition() {
        boolean validSearchTerm;
        if (isRange) {
            validSearchTerm = Verify.validateDecimal(textFieldMin.getText()) &&
                    Verify.validateDecimal(textFieldMax.getText()) &&
                    !(textFieldMax.getText().isEmpty() && textFieldMin.getText().isEmpty());
        } else {
            validSearchTerm = !textFieldSearchTerm.getText().isEmpty();
        }
        return comboDataType.getValue() != null && comboSearchType.getValue() != null &&
                validSearchTerm;
    }


    /**
     * Sets the DataContainer's filters to be as specified by the list of filter conditions here.
     * Called by apply filter button.
     */
    @FXML
    @SuppressWarnings("unused")
    private void applyFilter() {
        // Create new empty filters
        AirportFilter airportFilter = new AirportFilter();
        AirlineFilter airlineFilter = new AirlineFilter();
        RouteFilter routeFilter = new RouteFilter();
        FlightFilter flightFilter = new FlightFilter();

        for (AdvFilterCondition condition : filterConditions) {
            switch (condition.getDataType()) {
                case AIRLINE:
                    addAirlineCondition(condition, airlineFilter);
                    break;
                case AIRPORT:
                    addAirportCondition(condition, airportFilter);
                    break;
                case FLIGHT:
                    addFlightCondition(condition, flightFilter);
                    break;
                case ROUTE:
                    addRouteCondition(condition, routeFilter);
                    break;
            }
        }
        dataContainer.setFilters(airportFilter, airlineFilter, routeFilter, flightFilter);
        dataContainer.applyFilters();
        basicFilterController.setAdvancedFiltered(true);
        exitAdvFilter();
    }


    /**
     * Adds an airline filter condition to the airline filter.
     *
     * @param condition     The condition to add
     * @param airlineFilter The filter to add to
     */
    private void addAirlineCondition(AdvFilterCondition condition, AirlineFilter airlineFilter) {
        String term = condition.getSearchTerm();
        switch (condition.getSearchType()) {
            case "Name":
                airlineFilter.addName(term);
                break;
            case "Alias":
                airlineFilter.addAlias(term);
                break;
            case "IATA":
                airlineFilter.addIata(term);
                break;
            case "ICAO":
                airlineFilter.addIcao(term);
                break;
            case "Call sign":
                airlineFilter.addCallSign(term);
                break;
            case "Country":
                airlineFilter.addCountry(term);
                break;
            case "Status":
                Boolean status = Boolean.getBoolean(term);
                airlineFilter.setActive(status);
                break;
        }
    }


    /**
     * Adds an airport filter condition to the airport filter.
     *
     * @param condition     The condition to add.
     * @param airportFilter The filter to add to.
     */
    private void addAirportCondition(AdvFilterCondition condition, AirportFilter airportFilter) {
        String term = condition.getSearchTerm();
        Double[] rangeD;
        Integer[] rangeI;
        switch (condition.getSearchType()) {
            case "Name":
                airportFilter.addName(term);
                break;
            case "City":
                airportFilter.addCity(term);
                break;
            case "Country":
                airportFilter.addCountry(term);
                break;
            case "IATA/FAA":
                airportFilter.addIata(term);
                break;
            case "ICAO":
                airportFilter.addIcao(term);
                break;
            case "Latitude":
                rangeD = parseRangeDouble(term);
                airportFilter.setLatitude(rangeD[0], rangeD[1]);
                break;
            case "Longitude":
                rangeD = parseRangeDouble(term);
                airportFilter.setLongitude(rangeD[0], rangeD[1]);
                break;
            case "Altitude":
                rangeD = parseRangeDouble(term);
                airportFilter.setAltitude(rangeD[0], rangeD[1]);
                break;
            case "Time zone":
                airportFilter.addTimeZone(term);
                break;
            case "DST (Daylight Savings Time)":
                airportFilter.addDaylightSavingTime(term);
                break;
            case "Tz database":
                airportFilter.addTimeZoneDatabase(term);
                break;
            case "Number of Routes":
                rangeI = parseRangeInteger(term);
                airportFilter.setRoutes(rangeI[0], rangeI[1]);
                break;
        }
    }


    /**
     * Adds the airport filter condition to the airport filter.
     *
     * @param condition    The filter condition to add.
     * @param flightFilter The filter to add to.
     */
    private void addFlightCondition(AdvFilterCondition condition, FlightFilter flightFilter) {
        String term = condition.getSearchTerm();
        switch (condition.getSearchType()) {
            case "Source Airport":
                flightFilter.addSourceAirport(term);
                break;
            case "Destination Airport":
                flightFilter.addDestinationAirport(term);
                break;
        }
    }


    /**
     * Adds the route filter condition to the route filter.
     *
     * @param condition   The filter condition to add.
     * @param routeFilter The filter to add to.
     */
    private void addRouteCondition(AdvFilterCondition condition, RouteFilter routeFilter) {
        String term = condition.getSearchTerm();
        Integer[] rangeI;
        switch (condition.getSearchType()) {
            case "Airline":
                routeFilter.addAirline(term);
                break;
            case "Source Airport":
                routeFilter.addSourceAirport(term);
                break;
            case "Destination Airport":
                routeFilter.addDestinationAirport(term);
                break;
            case "Codeshare":
                routeFilter.setCodeShare(term.toLowerCase().equals("y") ||
                        term.toLowerCase().equals("true") ||
                        term.toLowerCase().equals("yes"));
                break;
            case "Number of stops":
                rangeI = parseRangeInteger(term);
                routeFilter.setStops(rangeI[0], rangeI[1]);
                break;
            case "Equipment":
                routeFilter.addEquipment(term);
                break;
        }
    }


    /**
     * Deletes the filter condition currently selected in the table. Called by delete button.
     */
    @FXML
    @SuppressWarnings("unused")
    private void deleteFilter() {
        int selectedIndex = tableFilter.getSelectionModel().getSelectedIndex();
        if (selectedIndex >= 0) {
            tableFilter.getItems().remove(selectedIndex);
        }
    }


    /**
     * Closes the window. Called by close button.
     */
    @FXML
    @SuppressWarnings("unused")
    private void exitAdvFilter() {
        Stage st = (Stage) buttonExit.getScene().getWindow();
        st.hide();
    }


    /**
     * Sets the items in the search type combo box based on the selection in the data type combo
     * box. Called whenever the data type selection changes.
     */
    @FXML
    @SuppressWarnings("unused")
    private void setSearchTypes() {
        comboSearchType.getItems().clear();
        String searchType = comboDataType.getValue();
        comboSearchType.getItems().addAll(searchTypes.get(searchType));
    }


    /**
     * Enables or disables range specification. Called whenever the search type selection changes.
     * Delegates setting search hint.
     */
    @FXML
    @SuppressWarnings("unused")
    private void setForSearchType() {
        setSearchTip();

        isRange = RecordType.isRangeSearchType(comboSearchType.getValue());
        textFieldMax.setVisible(isRange);
        textFieldMin.setVisible(isRange);
        textFieldSearchTerm.setVisible(!isRange);
    }


    /**
     * Sets the search hint based on the current selection in the search type combo box.
     */
    private void setSearchTip() {
        if (comboSearchType.getValue() == null) {
            termHint.setText(toolTip.get(null));
        } else {
            String currType = comboSearchType.getValue();
            termHint.setText(toolTip.get(currType));
        }
    }


    /**
     * Initialises the AdvFilterController.
     */
    @FXML
    @SuppressWarnings("unused")
    private void initialize() {
        initDataTypes();
        initToolTips();
        initSearchTypes();

        textFieldMin.setVisible(false);
        textFieldMax.setVisible(false);
        textFieldSearchTerm.setVisible(true);

        columnDataType.setCellValueFactory(new PropertyValueFactory<>("dataType"));
        columnSearchType.setCellValueFactory(new PropertyValueFactory<>("searchType"));
        columnSearchTerm.setCellValueFactory(new PropertyValueFactory<>("searchTerm"));

        tableFilter.setEditable(true);
        tableFilter.setItems(filterConditions);
    }

    public void setUp(DataContainer dataContainer, BasicFilterController basicFilterController) {
        this.basicFilterController = basicFilterController;
        this.dataContainer = dataContainer;
        populateFilterTable(dataContainer);
    }


    /**
     * Initialises the data types to those defined in RecordType.
     */
    private void initDataTypes() {
        dataTypes = RecordType.values();
        for (RecordType recordType : dataTypes) {
            comboDataType.getItems().add(recordType.toString());
        }
    }


    /**
     * Initialises the search types to those defined in RecordType.
     */
    private void initSearchTypes() {
        searchTypes = new HashMap<>();
        for (RecordType recordType : dataTypes) {
            searchTypes.put(recordType.toString(), recordType.getSearchTypes());
        }
    }


    /**
     * Initialises the tooltip mapping.
     */
    private void initToolTips() {
        toolTip = new HashMap<>();
        toolTip.put(null,
                "Selecting a Search Term will display a tip on the correct format of a Search Term.");
        toolTip.put("Name",
                "Enter a name of the chosen Data Type (e.g. Airport name or Airline name).");
        toolTip.put("City",
                "Enter a city.");
        toolTip.put("Country",
                "Enter a country.");
        toolTip.put("IATA/FAA",
                "Enter a 3-letter FAA code for an airport located in USA, otherwise enter 3-letter IATA code.");
        toolTip.put("ICAO",
                "Enter a 4-letter ICAO code.");
        toolTip.put("Latitude",
                "Specify a range of latitudes (in degrees) in the format '[min][, max]', where -180 <= min <= max <= 180. Either value can be omitted to specify no constraint in that direction. To indicate degrees W/E, use positive and negative numbers respectively.");
        toolTip.put("Longitude",
                "Specify a range of longitudes (in degrees) in the format '[min][, max]', where -90 <= min <= max <= 90. Either value can be omitted to specify no constraint in that direction. To indicate degrees N/S, use positive and negative numbers respectively.");
        toolTip.put("Altitude",
                "Specify a range of latitudes (in feet) in the format '[min][, max]'. Either value can be omitted to specify no constraint in that direction.");
        toolTip.put("Time zone",
                "Enter hours offset from UTC.");
        toolTip.put("DST (Daylight Savings Time)",
                "Enter from E (Europe), A (US/Canada), S (South America), O (Australia), Z (New Zealand), N (None), U (Unknown).");
        toolTip.put("Tz database",
                "Enter a Time zone in 'tz' (Olson) format.");
        toolTip.put("Alias",
                "Enter an alias of an airline. Usually a 3-4 letter code.");
        toolTip.put("IATA",
                "Enter a 2-letter IATA code.");
        toolTip.put("Call sign",
                "Enter a airline call sign.");
        toolTip.put("Status",
                "Enter an airport status. Y for active, N for inactive.");
        toolTip.put("Airline",
                "Enter a Unique OpenFlights identifier for an airline.");
        toolTip.put("Source Airport",
                "Enter the 3-letter IATA or 4-letter ICAO code of a source airport.");
        toolTip.put("Destination Airport",
                "Enter the 3-letter IATA or 4-letter ICAO code of a destination airport.");
        toolTip.put("Codeshare",
                "Enter Y for a code share flight.");
        toolTip.put("Number of stops",
                "Specify a range of number of stops.");
        toolTip.put("Equipment",
                "Enter the 3-letter code for a plane.");
        toolTip.put("Type",
                "Enter a flight type.");
        toolTip.put("Number of Routes",
                "Specify a range in the format '[max],[ min]'. The comma is mandatory but either value can be omitted to specify no constraint in that direction");

        termHint = new Tooltip(toolTip.get(null));
        termHint.setMaxWidth(600.0);
        termHint.setWrapText(true);
        textFieldSearchTerm.setTooltip(termHint);
    }


    /**
     * Reads the active filters into the list of filter conditions.
     *
     * @param dataContainer the source of the filters
     */
    private void populateFilterTable(DataContainer dataContainer) {
        readAirlineFilter(dataContainer.getAirlineFilter());
        readAirportFilter(dataContainer.getAirportFilter());
        readRouteFilter(dataContainer.getRouteFilter());
        readFlightFilter(dataContainer.getFlightFilter());
    }


    /**
     * Reads a flight filter into the filter condition list.
     *
     * @param flightFilter the flight filter to read
     */
    private void readFlightFilter(FlightFilter flightFilter) {
        RecordType recordType = RecordType.FLIGHT;
        readStringConditions(recordType, "Source Airport", flightFilter.getSourceAirport());
        readStringConditions(recordType, "Destination Airport", flightFilter.getDestinationAirport());
    }


    /**
     * Reads a route filter into the filter condition list.
     *
     * @param routeFilter The route filter to read.
     */
    private void readRouteFilter(RouteFilter routeFilter) {
        RecordType recordType = RecordType.ROUTE;
        readStringConditions(recordType, "Airline", routeFilter.getAirline());
        readStringConditions(recordType, "Source Airport", routeFilter.getSourceAirport());
        readStringConditions(recordType, "Destination Airport", routeFilter.getDestinationAirport());
        readBooleanCondition(recordType, "Codeshare", routeFilter.getCodeShare());
        readRange(recordType, "Number of stops", routeFilter.getStops());
        readStringConditions(recordType, "Equipment", routeFilter.getEquipment());
    }


    /**
     * Reads a airport filter into the filter condition list.
     *
     * @param airportFilter The airport filter to read.
     */
    private void readAirportFilter(AirportFilter airportFilter) {
        RecordType recordType = RecordType.AIRPORT;
        readStringConditions(recordType, "Name", airportFilter.getName());
        readStringConditions(recordType, "City", airportFilter.getCity());
        readStringConditions(recordType, "Country", airportFilter.getCountry());
        readStringConditions(recordType, "IATA/FAA", airportFilter.getIata());
        readStringConditions(recordType, "ICAO", airportFilter.getIcao());
        readRange(recordType, "Latitude", airportFilter.getLatitude());
        readRange(recordType, "Longitude", airportFilter.getLongitude());
        readRange(recordType, "Altitude", airportFilter.getAltitude());
        readStringConditions(recordType, "Time zone", airportFilter.getTimeZone());
        readStringConditions(recordType, "DST (Daylight Savings Time)", airportFilter.getDaylightSavingTime());
        readStringConditions(recordType, "Tz database", airportFilter.getTimeZoneDatabase());
        readRange(recordType, "Number of Routes", airportFilter.getRoutes());
    }


    /**
     * Reads a airline filter into the filter condition list.
     *
     * @param airlineFilter The airline filter to read.
     */
    private void readAirlineFilter(AirlineFilter airlineFilter) {
        RecordType recordType = RecordType.AIRLINE;
        readBooleanCondition(recordType, "Status", airlineFilter.getActive());
        readStringConditions(recordType, "ICAO", airlineFilter.getIcao());
        readStringConditions(recordType, "Country", airlineFilter.getCountry());
        readStringConditions(recordType, "Alias", airlineFilter.getAlias());
        readStringConditions(recordType, "IATA", airlineFilter.getIata());
        readStringConditions(recordType, "Name", airlineFilter.getName());
        readStringConditions(recordType, "Call sign", airlineFilter.getCallSign());
    }


    /**
     * Reads a list of string conditions, creating an AdvFilterCondition for each and adds it to the
     * list of them.
     *
     * @param dataType   The RecordType.
     * @param searchType The search type matching those defined in RecordType.
     * @param conditions The string conditions.
     */
    private void readStringConditions(RecordType dataType, String searchType, Collection<String> conditions) {
        filterConditions.addAll(conditions.stream().map(term -> new AdvFilterCondition(dataType, searchType, term)).collect(Collectors.toList()));
    }


    /**
     * Adds a new AdvFilterCondition to the list, if the input Boolean is not null.
     *
     * @param dataType   The RecordType.
     * @param searchType The search type matching those defined in RecordType.
     * @param condition  The Boolean to read.
     */
    private void readBooleanCondition(RecordType dataType, String searchType, Boolean condition) {
        if (condition != null) {
            filterConditions.add(new AdvFilterCondition(dataType, searchType, condition.toString()));
        }
    }


    /**
     * Reads a range into an AdvFilterCondition and adds it to the list of conditions, if not {null,
     * null}.
     *
     * @param dataType   The RecordType.
     * @param searchType The search type matching those defined in RecordType.
     * @param range      The range to read.
     */
    private void readRange(RecordType dataType, String searchType, Number[] range) {
        if (range[0] != null || range[1] != null) {
            String string = ",";
            if (range[0] != null) {
                string = range[0].toString() + string;
            }
            if (range[1] != null) {
                string = string + " " + range[1].toString();
            }
            filterConditions.add(new AdvFilterCondition(dataType, searchType, string));
        }
    }
}
