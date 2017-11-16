package seng202.group6.persistence;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonArray;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import com.google.gson.JsonSerializationContext;
import com.google.gson.JsonSerializer;
import com.google.gson.JsonSyntaxException;
import com.google.gson.reflect.TypeToken;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import seng202.group6.DataContainer;
import seng202.group6.model.Airline;
import seng202.group6.model.AirlineList;
import seng202.group6.model.Airport;
import seng202.group6.model.AirportList;
import seng202.group6.model.Flight;
import seng202.group6.model.FlightList;
import seng202.group6.model.FlightPoint;
import seng202.group6.model.Route;
import seng202.group6.model.RouteList;

/**
 * Uses gson to store the lists within the data container persistently.
 * <p>
 * Could be rewritten to use gson type adapters (with static Gson field from the outer class)
 * rather than de/serializers to reduce the overhead from the intermediate stage of JsonObject.
 */
public class PersistenceManager {

    // Types for parametrised lists.
    private static final Type TYPE_L_AIRLINE = new TypeToken<List<Airline>>() {
    }.getType();
    private static final Type TYPE_L_AIRPORT = new TypeToken<List<Airport>>() {
    }.getType();
    private static final Type TYPE_L_FLIGHT = new TypeToken<List<Flight>>() {
    }.getType();
    private static final Type TYPE_L_FLIGHT_POINT = new TypeToken<List<FlightPoint>>() {
    }.getType();
    private static final Type TYPE_L_ROUTE = new TypeToken<List<Route>>() {
    }.getType();

    // Labels for json elements
    // CHANGING THESE WILL INVALIDATE ALL PREVIOUS SERIALIZED FILES.
    private static final String NAME = "name";
    private static final String CITY = "city";
    private static final String COUNTRY = "country";
    private static final String IATA = "iata";
    private static final String ICAO = "icao";
    private static final String LATITUDE = "lat";
    private static final String LONGITUDE = "long";
    private static final String ALTITUDE = "alt";
    private static final String TIMEZONE = "tz";
    private static final String TIMEZONEDB = "tz_db";
    private static final String DST = "dst";
    private static final String ALIAS = "alias";
    private static final String CALLSIGN = "callsign";
    private static final String ACTIVE = "active";
    private static final String TYPE = "type";
    private static final String ID = "id";
    private static final String RECORDS = "records";
    private static final String AIRLINE = "airline";
    private static final String SOURCE = "src";
    private static final String DESTINATION = "dest";
    private static final String CODE_SHARE = "codeshare";
    private static final String STOPS = "stops";
    private static final String EQUIPMENT = "eqiup";

    /**
     * The DataContainer that will be serialised / deserialized.
     */
    private final DataContainer dataContainer;
    private final Gson gson;
    private Long version = 2L;


    /**
     * Instantiates a new Persistence Manager, linked to the given DataContainer.
     *
     * @param dataContainer a data container
     */
    public PersistenceManager(DataContainer dataContainer) {
        this.dataContainer = dataContainer;
        gson = buildGson();

        final String[] labels = {NAME, CITY, COUNTRY, IATA, ICAO, LATITUDE, LONGITUDE, ALTITUDE, TIMEZONE,
                TIMEZONEDB, DST, ALIAS, CALLSIGN, ACTIVE, TYPE, ID, RECORDS, AIRLINE, SOURCE,
                DESTINATION, CODE_SHARE, STOPS, EQUIPMENT};

        version = 31 * version + Arrays.hashCode(labels);
    }


    /**
     * Reads the contents of the given file and loads into the data container. The previous contents
     * of the data container are lost. The first line is an integer, verifying the format of the
     * contents. The file must have then have a further four lines, each one a json array of
     * AirlineLists, AirportLists, FlightLists and RouteLists respectively.
     *
     * @param file the file to read the data from
     * @throws IOException         on IO error
     * @throws FileFormatException on indication of incorrect fields or bad file format.
     */
    public void loadFromFile(final File file) throws IOException, FileFormatException {
        try (FileInputStream inputStream = new FileInputStream(file)) {
            BufferedReader in = new BufferedReader(new InputStreamReader(inputStream, "UTF-8"));
            String firstLine = in.readLine();
            try {
                int reported = Integer.parseInt(firstLine);
                if (reported != version) {
                    throw new FileFormatException("Required format check code: " + version +
                            "\nReported format check code: " + reported);
                }
            } catch (NumberFormatException e) {
                throw new FileFormatException("Could not retrieve format check code.\n" +
                        "File cannot be read", e);
            }
            List<AirlineList> airlineList = gson.fromJson(in.readLine(), AirlineListListWrapper.class).getLists();
            List<AirportList> airportList = gson.fromJson(in.readLine(), AirportListListWrapper.class).getLists();
            List<FlightList> flightList = gson.fromJson(in.readLine(), FlightListListWrapper.class).getLists();
            List<RouteList> routeList = gson.fromJson(in.readLine(), RouteListListWrapper.class).getLists();

            dataContainer.reset(airlineList, airportList, flightList, routeList);
            in.close();
        } catch (JsonSyntaxException e) {
            throw new FileFormatException("Bad Json syntax.", e);
        }
    }


    /**
     * Saves the records in the data container into the given file. Writes the format check number
     * on the first line. Then writes four lines, each one respectively a json array of the
     * AirlineLists, AirportLists, FlightLists and RouteLists contained in the data container. Does
     * not effect the data container.
     *
     * @param file the file to store the records in
     * @throws IOException on IO error
     */
    public void saveToFile(final File file) throws IOException {
        try (FileOutputStream outputStream = new FileOutputStream(file)) {
            BufferedWriter out = new BufferedWriter(new OutputStreamWriter(outputStream, "UTF-8"));

            out.write(version.toString());
            out.newLine();
            out.write(gson.toJson(dataContainer.getAirlineLists()));
            out.newLine();
            out.write(gson.toJson(dataContainer.getAirportLists()));
            out.newLine();
            out.write(gson.toJson(dataContainer.getFlightLists()));
            out.newLine();
            out.write(gson.toJson(dataContainer.getRouteLists()));
            out.newLine();

            out.flush();
            out.close();
            dataContainer.setModified(false);
        }
    }


    /**
     * Builds a Gson with the type adapters necessary for de/serializing a DataContainer.
     *
     * @return the gson
     */
    private Gson buildGson() {
        GsonBuilder gsonBuilder = new GsonBuilder();

        // Register the de/serializers for de/serializing lists of the specializations of RecordList.
        gsonBuilder.registerTypeAdapter(AirlineListListWrapper.class, new AirlineListListWrapper());
        gsonBuilder.registerTypeAdapter(AirportListListWrapper.class, new AirportListListWrapper());
        gsonBuilder.registerTypeAdapter(FlightListListWrapper.class, new FlightListListWrapper());
        gsonBuilder.registerTypeAdapter(RouteListListWrapper.class, new RouteListListWrapper());

        // Register the de/serializers for de/serializing the specializations of RecordList.
        gsonBuilder.registerTypeAdapter(AirlineList.class, new AirlineListSerializer());
        gsonBuilder.registerTypeAdapter(AirportList.class, new AirportListSerializer());
        gsonBuilder.registerTypeAdapter(FlightList.class, new FlightListSerializer());
        gsonBuilder.registerTypeAdapter(RouteList.class, new RouteListSerializer());


        // Register the de/serializers for de/serializing the specializations of Record.
        gsonBuilder.registerTypeAdapter(Airline.class, new AirlineSerializer());
        gsonBuilder.registerTypeAdapter(Airport.class, new AirportSerializer());
        gsonBuilder.registerTypeAdapter(Flight.class, new FlightSerializer());
        gsonBuilder.registerTypeAdapter(FlightPoint.class, new FlightPointSerializer());
        gsonBuilder.registerTypeAdapter(Route.class, new RouteSerializer());

        return gsonBuilder.create();
    }


    /**
     * A wrapper for a list of AirlineLists, to alleviate the problems in deserializing a
     * parametrized list.
     */
    private static final class AirlineListListWrapper implements JsonDeserializer<AirlineListListWrapper> {
        /**
         * The list of AirlineList that this wrapper class holds.
         */
        private List<AirlineList> lists;


        /**
         * Constructor for AirlineListListWrapper that takes no parameters.
         *
         * @param lists the list of AirlineLists that this class will hold
         */
        public AirlineListListWrapper(List<AirlineList> lists) {
            this.lists = lists;
        }


        /**
         * Constructor for AirlineListListWrapper that takes no parameters.
         */
        public AirlineListListWrapper() {
        }


        /**
         * Gets the list of AirlineList this class holds
         *
         * @return the list of AirlineLists the class holds
         */
        public List<AirlineList> getLists() {
            return lists;
        }


        @Override
        public AirlineListListWrapper deserialize(JsonElement element, Type type, JsonDeserializationContext context)
                throws JsonParseException {

            List<AirlineList> lists = new ArrayList<>();
            for (JsonElement list : element.getAsJsonArray()) {
                lists.add(context.deserialize(list, AirlineList.class));
            }
            return new AirlineListListWrapper(lists);
        }
    }


    /**
     * A wrapper for a list of AirportLists, to alleviate the problems in deserializing a
     * parametrized list.
     */
    private static final class AirportListListWrapper implements JsonDeserializer<AirportListListWrapper> {
        /**
         * The list of AirportList that this wrapper class holds.
         */
        private List<AirportList> lists;


        /**
         * Constructor for AirportListListWrapper that takes no parameters.
         */
        public AirportListListWrapper() {
        }


        /**
         * Constructor for AirportListListWrapper that takes no parameters.
         *
         * @param lists the list of AirportLists that this class will hold
         */
        public AirportListListWrapper(List<AirportList> lists) {
            this.lists = lists;
        }


        /**
         * Gets the list of AirportList this class holds
         *
         * @return the list of AirportLists the class holds
         */
        public List<AirportList> getLists() {
            return lists;
        }


        @Override
        public AirportListListWrapper deserialize(JsonElement element, Type type, JsonDeserializationContext context)
                throws JsonParseException {

            List<AirportList> lists = new ArrayList<>();
            for (JsonElement list : element.getAsJsonArray()) {
                lists.add(context.deserialize(list, AirportList.class));
            }

            return new AirportListListWrapper(lists);
        }
    }


    /**
     * A wrapper for a list of FlightLists, to alleviate the problems in deserializing a
     * parametrized list.
     */
    private static final class FlightListListWrapper implements JsonDeserializer<FlightListListWrapper> {
        /**
         * The list of FlightList that this wrapper class holds.
         */
        private List<FlightList> lists;

        /**
         * Constructor for FlightListListWrapper that takes no parameters.
         */
        public FlightListListWrapper() {
        }


        /**
         * Constructor for FlightListListWrapper that takes no parameters.
         *
         * @param lists the list of FlightLists that this class will hold
         */
        public FlightListListWrapper(List<FlightList> lists) {
            this.lists = lists;
        }


        /**
         * Gets the list of FlightList this class holds
         *
         * @return the list of FlightLists the class holds
         */
        public List<FlightList> getLists() {
            return lists;
        }


        @Override
        public FlightListListWrapper deserialize(JsonElement element, Type type, JsonDeserializationContext context)
                throws JsonParseException {

            List<FlightList> lists = new ArrayList<>();
            for (JsonElement list : element.getAsJsonArray()) {
                lists.add(context.deserialize(list, FlightList.class));
            }

            return new FlightListListWrapper(lists);
        }
    }


    /**
     * A wrapper for a list of RouteLists, to alleviate the problems in deserializing a parametrized
     * list.
     */
    private static final class RouteListListWrapper implements JsonDeserializer<RouteListListWrapper> {
        /**
         * The list of RouteLists that this wrapper class holds.
         */
        private List<RouteList> lists;

        /**
         * Constructor for RouteListListWrapper that takes no parameters.
         */
        public RouteListListWrapper() {
        }


        /**
         * Constructor for RouteListListWrapper that takes no parameters.
         *
         * @param lists the list of RouteLists that this class will hold
         */
        public RouteListListWrapper(List<RouteList> lists) {
            this.lists = lists;
        }


        /**
         * Gets the list of RouteLists this class holds
         *
         * @return the list of RouteLists the class holds
         */
        public List<RouteList> getLists() {
            return lists;
        }


        @Override
        public RouteListListWrapper deserialize(JsonElement element, Type type, JsonDeserializationContext context)
                throws JsonParseException {

            List<RouteList> lists = new ArrayList<>();
            for (JsonElement list : element.getAsJsonArray()) {
                lists.add(context.deserialize(list, RouteList.class));
            }

            return new RouteListListWrapper(lists);
        }
    }


    /**
     * De/serializer for AirlineLists.
     */
    private static final class AirlineListSerializer
            implements JsonSerializer<AirlineList>, JsonDeserializer<AirlineList> {
        @Override
        public JsonElement serialize(AirlineList airlineList, Type type, JsonSerializationContext context) {
            final JsonObject object = new JsonObject();
            object.addProperty(NAME, airlineList.getName());
            final JsonElement airlines = context.serialize(airlineList.getRecords(), TYPE_L_AIRLINE);
            object.add(RECORDS, airlines);

            return object;
        }

        @Override
        public AirlineList deserialize(JsonElement element, Type type, JsonDeserializationContext context)
                throws JsonParseException {
            final JsonObject object = element.getAsJsonObject();
            String name = object.get(NAME).getAsString();
            final AirlineList airlineList = new AirlineList(name);
            final ArrayList<Airline> airlines = context.deserialize(object.get(RECORDS), TYPE_L_AIRLINE);
            airlineList.addAll(airlines);

            return airlineList;
        }
    }


    /**
     * De/serializer for AirportLists.
     */
    private static final class AirportListSerializer
            implements JsonSerializer<AirportList>, JsonDeserializer<AirportList> {
        @Override
        public JsonElement serialize(AirportList airportList, Type type, JsonSerializationContext context) {
            final JsonObject object = new JsonObject();
            object.addProperty(NAME, airportList.getName());
            final JsonElement airports = context.serialize(airportList.getRecords(), TYPE_L_AIRPORT);
            object.add(RECORDS, airports);

            return object;
        }


        @Override
        public AirportList deserialize(JsonElement element, Type type, JsonDeserializationContext context)
                throws JsonParseException {
            final JsonObject object = element.getAsJsonObject();
            String name = object.get(NAME).getAsString();
            final AirportList airportList = new AirportList(name);
            final ArrayList<Airport> airports = context.deserialize(object.get(RECORDS), TYPE_L_AIRPORT);
            airportList.addAll(airports);

            return airportList;
        }
    }


    /**
     * De/serializer for RouteLists.
     */
    private static final class FlightListSerializer
            implements JsonSerializer<FlightList>, JsonDeserializer<FlightList> {
        @Override
        public JsonElement serialize(FlightList flightList, Type type, JsonSerializationContext context) {
            final JsonObject object = new JsonObject();
            object.addProperty(NAME, flightList.getName());
            final JsonElement flights = context.serialize(flightList.getRecords(), TYPE_L_FLIGHT);
            object.add(RECORDS, flights);

            return object;
        }


        @Override
        public FlightList deserialize(JsonElement element, Type type, JsonDeserializationContext context)
                throws JsonParseException {
            final JsonObject object = element.getAsJsonObject();
            String name = object.get(NAME).getAsString();
            final FlightList flightList = new FlightList(name);
            final ArrayList<Flight> flights = context.deserialize(object.get(RECORDS), TYPE_L_FLIGHT);
            flightList.addAll(flights);

            return flightList;
        }
    }


    /**
     * De/serializer for RouteLists.
     */
    private static final class RouteListSerializer implements JsonSerializer<RouteList>, JsonDeserializer<RouteList> {
        @Override
        public JsonElement serialize(RouteList routeList, Type type, JsonSerializationContext context) {
            final JsonObject object = new JsonObject();
            object.addProperty(NAME, routeList.getName());
            final JsonElement routes = context.serialize(routeList.getRecords(), TYPE_L_ROUTE);
            object.add(RECORDS, routes);

            return object;
        }


        @Override
        public RouteList deserialize(JsonElement element, Type type, JsonDeserializationContext context)
                throws JsonParseException {
            final JsonObject object = element.getAsJsonObject();
            String name = object.get(NAME).getAsString();
            final RouteList routeList = new RouteList(name);
            final ArrayList<Route> routes = context.deserialize(object.get(RECORDS), TYPE_L_ROUTE);
            routeList.addAll(routes);

            return routeList;
        }
    }


    /**
     * De/serializer for Airlines.
     */
    private static final class AirlineSerializer implements JsonSerializer<Airline>, JsonDeserializer<Airline> {
        @Override
        public JsonElement serialize(Airline airline, Type type, JsonSerializationContext context) {
            final JsonObject jsonObject = new JsonObject();
            jsonObject.addProperty(NAME, airline.getName());
            jsonObject.addProperty(ALIAS, airline.getAlias());
            jsonObject.addProperty(IATA, airline.getIata());
            jsonObject.addProperty(ICAO, airline.getIcao());
            jsonObject.addProperty(CALLSIGN, airline.getCallSign());
            jsonObject.addProperty(COUNTRY, airline.getCountry());
            jsonObject.addProperty(ACTIVE, airline.getActive());

            return jsonObject;
        }


        @Override
        public Airline deserialize(JsonElement element,
                                   Type type,
                                   JsonDeserializationContext jsonDeserializationContext)
                throws JsonParseException {

            final JsonObject object = element.getAsJsonObject();
            String name = object.get(NAME).getAsString();
            String alias = object.get(ALIAS).getAsString();
            String iata = object.get(IATA).getAsString();
            String icao = object.get(ICAO).getAsString();
            String callSign = object.get(CALLSIGN).getAsString();
            String country = object.get(COUNTRY).getAsString();
            boolean active = object.get(ACTIVE).getAsBoolean();

            return new Airline(name, alias, iata, icao, callSign, country, active);
        }
    }


    /**
     * De/serializer for Airports.
     */
    private static final class AirportSerializer implements JsonSerializer<Airport>, JsonDeserializer<Airport> {
        @Override
        public JsonElement serialize(Airport airport, Type type, JsonSerializationContext context) {
            final JsonObject object = new JsonObject();
            object.addProperty(NAME, airport.getName());
            object.addProperty(IATA, airport.getIata());
            object.addProperty(ICAO, airport.getIcao());
            object.addProperty(CITY, airport.getCity());
            object.addProperty(COUNTRY, airport.getCountry());
            object.addProperty(LATITUDE, airport.getLatitude());
            object.addProperty(LONGITUDE, airport.getLongitude());
            object.addProperty(ALTITUDE, airport.getAltitude());
            object.addProperty(TIMEZONE, airport.getTimeZone());
            object.addProperty(DST, airport.getDst());
            object.addProperty(TIMEZONEDB, airport.getTimeZoneDatabase());

            return object;
        }


        @Override
        public Airport deserialize(JsonElement element, Type type, JsonDeserializationContext context)
                throws JsonParseException {
            final JsonObject object = element.getAsJsonObject();
            String name = object.get(NAME).getAsString();
            String city = object.get(CITY).getAsString();
            String country = object.get(COUNTRY).getAsString();
            String iata = object.get(IATA).getAsString();
            String icao = object.get(ICAO).getAsString();
            double latitude = object.get(LATITUDE).getAsDouble();
            double longitude = object.get(LONGITUDE).getAsDouble();
            double altitude = object.get(ALTITUDE).getAsDouble();
            String timezone = object.get(TIMEZONE).getAsString();
            String dst = object.get(DST).getAsString();
            String timezoneDatabase = object.get(TIMEZONEDB).getAsString();

            return new Airport(name, city, country, iata, icao, latitude,
                    longitude, altitude, timezone, dst, timezoneDatabase);
        }
    }


    /**
     * De/serializer for Flights.
     */
    private static final class FlightSerializer implements JsonSerializer<Flight>, JsonDeserializer<Flight> {
        @Override
        public JsonElement serialize(Flight flight, Type type, JsonSerializationContext context) {
            final JsonObject object = new JsonObject();
            final JsonElement flightpoints = context.serialize(flight.getFlightPoints(), TYPE_L_FLIGHT_POINT);
            object.add(RECORDS, flightpoints);

            return object;
        }


        @Override
        public Flight deserialize(JsonElement element, Type type, JsonDeserializationContext context)
                throws JsonParseException {

            final JsonObject object = element.getAsJsonObject();
            ArrayList<FlightPoint> flightPoints = context.deserialize(object.get(RECORDS), TYPE_L_FLIGHT_POINT);

            return new Flight(flightPoints);
        }
    }


    /**
     * De/serializer for FlightPoints.
     */
    private static final class FlightPointSerializer
            implements JsonSerializer<FlightPoint>, JsonDeserializer<FlightPoint> {
        @Override
        public JsonElement serialize(FlightPoint flightPoint, Type type, JsonSerializationContext context) {
            final JsonObject object = new JsonObject();
            object.addProperty(TYPE, flightPoint.getType());
            object.addProperty(ID, flightPoint.getId());
            object.addProperty(LATITUDE, flightPoint.getLatitude());
            object.addProperty(LONGITUDE, flightPoint.getLongitude());
            object.addProperty(ALTITUDE, flightPoint.getAltitude());

            return object;
        }


        @Override
        public FlightPoint deserialize(JsonElement element, Type type, JsonDeserializationContext context)
                throws JsonParseException {

            final JsonObject object = element.getAsJsonObject();
            String point_type = object.get(TYPE).getAsString();
            String id = object.get(ID).getAsString();
            double altitude = object.get(ALTITUDE).getAsDouble();
            double latitude = object.get(LATITUDE).getAsDouble();
            double longitude = object.get(LONGITUDE).getAsDouble();

            return new FlightPoint(point_type, id, altitude, latitude, longitude);
        }
    }

    /**
     * De/serializer for Routes.
     */
    private static final class RouteSerializer implements JsonSerializer<Route>, JsonDeserializer<Route> {
        @Override
        public JsonElement serialize(Route route, Type type, JsonSerializationContext context) {
            final JsonObject object = new JsonObject();
            object.addProperty(AIRLINE, route.getAirline());
            object.addProperty(SOURCE, route.getSourceAirport());
            object.addProperty(DESTINATION, route.getDestinationAirport());
            object.addProperty(CODE_SHARE, route.getCodeShare());
            object.addProperty(STOPS, route.getStops());
            JsonArray equipment = new JsonArray();
            if (!route.getEquipment().isEmpty()) {
                route.getEquipment().forEach(equipment::add);
            }
            object.add(EQUIPMENT, equipment);

            return object;
        }


        @Override
        public Route deserialize(JsonElement element, Type type, JsonDeserializationContext context)
                throws JsonParseException {

            final JsonObject object = element.getAsJsonObject();
            String airline = object.get(AIRLINE).getAsString();
            String source = object.get(SOURCE).getAsString();
            String destination = object.get(DESTINATION).getAsString();
            boolean codeShare = object.get(CODE_SHARE).getAsBoolean();
            int stops = object.get(STOPS).getAsInt();
            ArrayList<String> equipment = new ArrayList<>(
                    Arrays.asList(context.deserialize(object.get(EQUIPMENT), String[].class)));

            return new Route(airline, source, destination, codeShare, stops, equipment);
        }
    }
}
