package shared.dataInput;




/**
 * Provides information about a race regatta.
 */
public interface RegattaDataSource {

    /**
     * Returns the ID of the regatta.
     * @return The ID of the regatta.
     */
    int getRegattaID();

    /**
     * Returns the name of the regatta.
     * @return The name of the regatta.
     */
    String getRegattaName();

    /**
     * Returns the ID of the race this regatta relates to.
     * @return The ID of the race that this regatta relates to.
     */
    int getRaceID();

    /**
     * Returns the name of the course.
     * @return the name of the course
     */
    String getCourseName();


    /**
     * Returns the latitude of the centre of the course.
     * @return The latitude of the centre of the course.
     */
    double getCentralLatitude();

    /**
     * Returns the longitude of the centre of the course.
     * @return The longitude of the centre of the course.
     */
    double getCentralLongitude();

    /**
     * Returns the altitude of the centre of the course.
     * @return The altitude of the centre of the course.
     */
    double getCentralAltitude();


    /**
     * Returns the UTC offset of the course's location.
     * @return The UTC offset of the course.
     */
    float getUtcOffset();


    /**
     * Returns the magnetic variation of the course's location.
     * @return The magnetic variation of the course.
     */
    float getMagneticVariation();


    /**
     * Returns the sequence number associated with this data source. Used to indicate when it has changed.
     * @return Sequence number.
     */
    int getSequenceNumber();

    /**
     * Increments the sequence number for this data source. Used to indicate that it has changed.
     */
    void incrementSequenceNumber();

}
