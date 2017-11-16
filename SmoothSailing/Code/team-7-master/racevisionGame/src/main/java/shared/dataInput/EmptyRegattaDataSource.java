package shared.dataInput;

import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import shared.enums.XMLFileType;
import shared.exceptions.InvalidRegattaDataException;
import shared.exceptions.XMLReaderException;
import shared.model.GPSCoordinate;

import java.io.InputStream;

/**
 * An empty {@link RegattaDataSource}. Can be used to initialise a race with no data.
 */
public class EmptyRegattaDataSource implements RegattaDataSource {
    /**
     * The regatta ID.
     */
    private int regattaID = 0;

    /**
     * The regatta name.
     */
    private String regattaName = "";

    /**
     * The race ID.
     */
    private int raceID = 0;

    /**
     * The course name.
     */
    private String courseName = "";

    /**
     * The central latitude of the course.
     */
    private double centralLatitude = 0;

    /**
     * The central longitude of the course.
     */
    private double centralLongitude = 0;

    /**
     * The central altitude of the course.
     */
    private double centralAltitude = 0;

    /**
     * The UTC offset of the course.
     */
    private float utcOffset = 0;

    /**
     * The magnetic variation of the course.
     */
    private float magneticVariation = 0;



    private int sequenceNumber = -1;


    public EmptyRegattaDataSource() {
    }




    public int getRegattaID() {
        return regattaID;
    }


    public String getRegattaName() {
        return regattaName;
    }


    public int getRaceID() {
        return raceID;
    }


    public String getCourseName() {
        return courseName;
    }


    public double getCentralLatitude() {
        return centralLatitude;
    }


    public double getCentralLongitude() {
        return centralLongitude;
    }


    public double getCentralAltitude() {
        return centralAltitude;
    }


    public float getUtcOffset() {
        return utcOffset;
    }


    public float getMagneticVariation() {
        return magneticVariation;
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

    @Override
    public void incrementSequenceNumber() {
        sequenceNumber++;
    }
}
