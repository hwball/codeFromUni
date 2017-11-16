package shared.dataInput;

import org.w3c.dom.Element;
import org.w3c.dom.Node;
import shared.enums.XMLFileType;
import shared.exceptions.InvalidBoatDataException;
import shared.exceptions.XMLReaderException;
import shared.model.Boat;
import shared.model.GPSCoordinate;
import shared.model.Mark;

import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;

/**
 * Xml Reader class for Boat XML used for the race.
 */
public class BoatXMLReader extends XMLReader implements BoatDataSource {

    /**
     * A map of source ID to boat for all boats in the race.
     */
    private final Map<Integer, Boat> boatMap = new HashMap<>();

    /**
     * A map of source ID to mark for all marks in the race.
     */
    private final Map<Integer, Mark> markerMap = new HashMap<>();


    private int sequenceNumber = 0;

    /**
     * Constructor for Boat XML using a file.
     *
     * @param file The file to read.
     * @param type How to read the file - e.g., load as resource.
     * @throws XMLReaderException Thrown if the file cannot be parsed.
     * @throws InvalidBoatDataException Thrown if the file cannot be parsed correctly.
     */
    public BoatXMLReader(String file, XMLFileType type) throws XMLReaderException, InvalidBoatDataException {
        super(file, type);

        //Attempt to read boat xml file.
        try {
            read();
        } catch (Exception e) {
            throw new InvalidBoatDataException("An error occurred while reading the boat xml file", e);
        }
    }


    /**
     * Constructor for Boat XML, using an InputStream.
     *
     * @param fileStream Stream to read boat data from.
     * @throws XMLReaderException Thrown if the file cannot be parsed.
     * @throws InvalidBoatDataException Thrown if the stream cannot be parsed correctly.
     */
    public BoatXMLReader(InputStream fileStream) throws XMLReaderException, InvalidBoatDataException {
        super(fileStream);

        //Attempt to read boat xml stream.
        try {
            read();
        } catch (Exception e) {
            throw new InvalidBoatDataException("An error occurred while reading the boat xml stream", e);
        }
    }



    /**
     * Read the XML
     */
    public void read() {
        readSettings();
        readShapes();
        readBoats();
    }

    /**
     * Read the Boats
     */
    private void readBoats() {
        Element nBoats = (Element) doc.getElementsByTagName("Boats").item(0);
        for (int i = 0; i < nBoats.getChildNodes().getLength(); i++) {
            Node boat = nBoats.getChildNodes().item(i);
            if (boat.getNodeName().equals("Boat")) {
                readBoatNode(boat);
            }
        }
    }

    /**
     * Ignored data
     */
    private void readShapes() {

    }

    /**
     * Ignored data
     */
    private void readSettings() {

    }

    /**
     * Checks if the node (XMl data node) is a Yacht or not
     * @param boatNode Node from the XML
     * @return Whether the node is a yacht node or not
     */
    private boolean isYachtNode(Node boatNode) {
        return boatNode.getAttributes().getNamedItem("Type").getTextContent().toLowerCase().equals("yacht");
    }

    /**
     * Reads the information about one boat
     * Ignored values: ShapeID, StoweName, HullNum, Skipper, Type
     * @param boatNode The node representing a boat.
     */
    private void readBoatNode(Node boatNode) {
        int sourceID = Integer.parseInt(boatNode.getAttributes().getNamedItem("SourceID").getTextContent());
        String name = boatNode.getAttributes().getNamedItem("BoatName").getTextContent();

        if (isYachtNode(boatNode)) readYacht(boatNode, sourceID, name);
        else readMark(boatNode, sourceID, name);
    }

    /**
     * Read a Yacht Node
     * @param boatNode Node to be read
     * @param sourceID Source ID of the Yacht
     * @param name Name of the Boat
     */
    private void readYacht(Node boatNode, int sourceID, String name) {
        String shortName = boatNode.getAttributes().getNamedItem("ShortName").getTextContent();
        if (exists(boatNode, "Country")) {
            String country = boatNode.getAttributes().getNamedItem("Country").getTextContent();
            boatMap.put(sourceID, new Boat(sourceID, name, country));
        } else {
            boatMap.put(sourceID, new Boat(sourceID, name, shortName));
        }
    }

    /**
     * Read Marker Boats
     * @param boatNode Node to be read
     * @param sourceID Source ID of the boat
     * @param name Name of the Marker Boat
     */
    private void readMark(Node boatNode, int sourceID, String name) {
        Node nCoord = ((Element)boatNode).getElementsByTagName("GPSposition").item(0);
        double x = Double.parseDouble(nCoord.getAttributes().getNamedItem("X").getTextContent());
        double y = Double.parseDouble(nCoord.getAttributes().getNamedItem("Y").getTextContent());
        Mark mark = new Mark(sourceID, name, new GPSCoordinate(y,x));
        markerMap.put(sourceID, mark);
    }

    /**
     * Get the boats that are going to participate in this race
     * @return Dictionary of boats that are to participate in this race indexed by SourceID
     */
    @Override
    public Map<Integer, Boat> getBoats() {
        return boatMap;
    }

    /**
     * Get the marker Boats that are participating in this race
     * @return Dictionary of the Markers Boats that are in this race indexed by their Source ID.
     */
    @Override
    public Map<Integer, Mark> getMarkerBoats() {
        return markerMap;
    }


    @Override
    public int getSequenceNumber() {
        return sequenceNumber;
    }

    public void setSequenceNumber(int sequenceNumber) {
        this.sequenceNumber = sequenceNumber;
    }

    @Override
    public void incrementSequenceNumber() {
        sequenceNumber++;
    }
}
