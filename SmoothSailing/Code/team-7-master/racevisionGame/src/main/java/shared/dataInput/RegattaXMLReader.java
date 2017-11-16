package shared.dataInput;

import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import shared.enums.XMLFileType;
import shared.exceptions.InvalidRegattaDataException;
import shared.exceptions.XMLReaderException;
import shared.model.GPSCoordinate;

import java.io.InputStream;

/**
 * XML reader class for regatta xml file.
 */
public class RegattaXMLReader extends XMLReader implements RegattaDataSource {
    /**
     * The regatta ID.
     */
    private int regattaID;

    /**
     * The regatta name.
     */
    private String regattaName;

    /**
     * The race ID.
     */
    private int raceID = 0;

    /**
     * The course name.
     */
    private String courseName;

    /**
     * The central latitude of the course.
     */
    private double centralLatitude;

    /**
     * The central longitude of the course.
     */
    private double centralLongitude;

    /**
     * The central altitude of the course.
     */
    private double centralAltitude;

    /**
     * The UTC offset of the course.
     */
    private float utcOffset;

    /**
     * The magnetic variation of the course.
     */
    private float magneticVariation;


    private int sequenceNumber = 0;


    /**
     * Constructor for Regatta XML using a file.
     *
     * @param file The file.
     * @param type How to read the file - e.g., load as resource.
     * @throws XMLReaderException Thrown if the file cannot be parsed.
     * @throws InvalidRegattaDataException Thrown if the file cannot be parsed correctly.
     */
    public RegattaXMLReader(String file, XMLFileType type) throws XMLReaderException, InvalidRegattaDataException {
        super(file, type);

        //Attempt to read boat xml file.
        try {
            read();
        } catch (Exception e) {
            throw new InvalidRegattaDataException("An error occurred while reading the regatta xml file", e);
        }
    }



    /**
     * Constructor for Regatta XML using an InputStream.
     * @param xmlString Input stream of the XML.
     * @throws XMLReaderException Thrown if the input stream cannot be parsed.
     * @throws InvalidRegattaDataException Thrown if the stream cannot be parsed correctly.
     */
    public RegattaXMLReader(InputStream xmlString) throws XMLReaderException, InvalidRegattaDataException {
        super(xmlString);

        //Attempt to read boat xml file.
        try {
            read();
        } catch (Exception e) {
            throw new InvalidRegattaDataException("An error occurred while reading the regatta xml stream", e);
        }
    }


    /**
     * Read the XML
     */
    private void read() {
        NodeList attributeConfig = doc.getElementsByTagName("RegattaConfig");
        Element attributes = (Element) attributeConfig.item(0);
        makeRegatta(attributes);
    }

    /**
     * Extracts the information from the attributes
     * @param attributes attributes to extract information form.
     */
    private void makeRegatta(Element attributes) {

        this.regattaID = Integer.parseInt(getTextValueOfNode(attributes, "RegattaID"));
        this.regattaName = getTextValueOfNode(attributes, "RegattaName");

        //this.raceID = Integer.parseInt(getTextValueOfNode(attributes, "RaceID"));
        this.courseName = getTextValueOfNode(attributes, "CourseName");

        this.centralLatitude = Double.parseDouble(getTextValueOfNode(attributes, "CentralLatitude"));
        this.centralLongitude = Double.parseDouble(getTextValueOfNode(attributes, "CentralLongitude"));
        this.centralAltitude = Double.parseDouble(getTextValueOfNode(attributes, "CentralAltitude"));

        this.utcOffset = Float.parseFloat(getTextValueOfNode(attributes, "UtcOffset"));

        this.magneticVariation = Float.parseFloat(getTextValueOfNode(attributes, "MagneticVariation"));

    }

    public int getRegattaID() {
        return regattaID;
    }

    public void setRegattaID(int ID) {
        this.regattaID = ID;
    }

    public String getRegattaName() {
        return regattaName;
    }

    public void setRegattaName(String regattaName) {
        this.regattaName = regattaName;
    }

    public int getRaceID() {
        return raceID;
    }

    public void setRaceID(int raceID) {
        this.raceID = raceID;
    }

    public String getCourseName() {
        return courseName;
    }

    public void setCourseName(String courseName) {
        this.courseName = courseName;
    }

    public double getCentralLatitude() {
        return centralLatitude;
    }

    public void setCentralLatitude(double centralLatitude) {
        this.centralLatitude = centralLatitude;
    }

    public double getCentralLongitude() {
        return centralLongitude;
    }

    public void setCentralLongitude(double centralLongitude) {
        this.centralLongitude = centralLongitude;
    }

    public double getCentralAltitude() {
        return centralAltitude;
    }

    public void setCentralAltitude(double centralAltitude) {
        this.centralAltitude = centralAltitude;
    }

    public float getUtcOffset() {
        return utcOffset;
    }

    public void setUtcOffset(float utcOffset) {
        this.utcOffset = utcOffset;
    }

    public float getMagneticVariation() {
        return magneticVariation;
    }

    public void setMagneticVariation(float magneticVariation) {
        this.magneticVariation = magneticVariation;
    }

    /**
     * Returns the GPS coorindates of the centre of the regatta.
     * @return The gps coordinate for the centre of the regatta.
     */
    public GPSCoordinate getGPSCoordinate() {
        return new GPSCoordinate(centralLatitude, centralLongitude);
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
