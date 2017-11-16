package mock.xml;

import mock.model.NewPolars;
import org.xml.sax.SAXException;
import shared.dataInput.RaceXMLReader;
import shared.enums.XMLFileType;
import shared.exceptions.InvalidRaceDataException;
import shared.exceptions.XMLReaderException;
import shared.model.Bearing;
import shared.model.CompoundMark;
import shared.model.Constants;
import shared.model.GPSCoordinate;
import shared.xml.Race.*;
import shared.xml.XMLUtilities;

import javax.xml.bind.JAXBException;
import javax.xml.parsers.ParserConfigurationException;
import java.io.IOException;
import java.io.InputStream;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Map;

/**
 * Helper Class for creating a Race XML
 */
public class RaceXMLCreator {


    /**
     * get the windward gate in a race
     * @param reader reads in the mark
     * @return the windward gate.
     */
    public static CompoundMark getWindwardGate(RaceXMLReader reader){
        for (CompoundMark mark: reader.getCompoundMarks()){
            if (mark.getName().equals("Windward Gate")) return mark;
        }
        return null;
    }

    /**
     * get the leeward gate in a race
     * @param reader reads in the mark
     * @return the leeward gate.
     */
    public static CompoundMark getLeewardGate(RaceXMLReader reader){
        for (CompoundMark mark: reader.getCompoundMarks()){
            if (mark.getName().equals("Leeward Gate")) return mark;
        }
        return null;
    }

    /**
     * Rotates the race in a specified direction.
     * @param s xml file name or contents.
     * @param fileType Whether s is a file name or contents.
     * @param degrees degrees to rotate. -1 means don't rotate.
     * @return the new xml file as a string
     * @throws XMLReaderException if the xml is not readable
     * @throws InvalidRaceDataException if the race is invalid
     */
    public static String alterRaceToWind(String s, XMLFileType fileType, double degrees) throws XMLReaderException, InvalidRaceDataException {

        RaceXMLReader reader = new RaceXMLReader(s, fileType);

        try {

            XMLRace race = XMLUtilities.xmlToClass(
                    s,
                    RaceXMLCreator.class.getClassLoader().getResource("mock/mockXML/schema/raceSchema.xsd"),
                    XMLRace.class);



            CompoundMark leewardGate = getLeewardGate(reader);
            CompoundMark windwardGate = getWindwardGate(reader);

            double raceOriginalBearing = 0;

            /*if (leewardGate != null && windwardGate != null) {
                raceOriginalBearing = getLineAngle(
                        leewardGate.getMark1Position(),
                        windwardGate.getMark1Position() );
            }*/

            double degreesToRotate = degrees - raceOriginalBearing;

            if (degrees >= 0) {
                alterRaceRotation(race, degreesToRotate);
            }

            return XMLUtilities.classToXML(race);

        } catch (ParserConfigurationException | IOException | SAXException | JAXBException e) {
            throw new InvalidRaceDataException("Could not parse or marshall race data file.", e);
        }
    }

    /**
     * Rotate the features in a race such as the boundary, and the marks.
     * @param race the race to alter
     * @param degrees the degrees to rotate by.
     */
    public static void alterRaceRotation(XMLRace race, double degrees){
        GPSCoordinate center = getCenter(race);
        for(XMLLimit limit: race.getCourseLimit().getLimit()){
            GPSCoordinate rotatedLim = rotate(center, limitToGPSCoordinate(limit), degrees);
            limit.setLat(rotatedLim.getLatitude());
            limit.setLon(rotatedLim.getLongitude());
        }

        for(XMLCompoundMark compoundMark: race.getCourse().getCompoundMark()){
            for (XMLMark mark: compoundMark.getMark()){
                GPSCoordinate rotatedMark = rotate(center, markToGPSCoordinate(mark), degrees);
                mark.setTargetLat(rotatedMark.getLatitude());
                mark.setTargetLng(rotatedMark.getLongitude());
            }
        }
    }


    /**
     * Rotates the race in a specified direction.
     * @param s xml file name or contents.
     * @param windSpeed speed that the wind is at.
     * @param milliseconds time the race should take at fastest
     * @return the new xml file as a string
     * @throws XMLReaderException if the xml is not readable
     * @throws InvalidRaceDataException if the race is invalid
     */
    public static String scaleRaceSize(String s, double windSpeed, double milliseconds) throws XMLReaderException, InvalidRaceDataException {

        try {

            XMLRace race = XMLUtilities.xmlToClass(
                    s,
                    RaceXMLCreator.class.getClassLoader().getResource("mock/mockXML/schema/raceSchema.xsd"),
                    XMLRace.class);

            scaleRace(race, windSpeed, milliseconds);

            return XMLUtilities.classToXML(race);

        } catch (ParserConfigurationException | IOException | SAXException | JAXBException e) {
            throw new InvalidRaceDataException("Could not parse or marshall race data file.", e);
        }
    }

    /**
     * Gets an estimate of how long a race will take using an average Speed
     * @param race race to estimate
     * @param averageSpeed average speed that the boats move at
     * @return the estimated amount of time it will take a boat to finish the race (skewed to minimum).
     */
    public static double getRaceLength(XMLRace race, double averageSpeed){
        double raceRoundingTime = 5000; //5 seconds to round a mark
        double totalDistance = 0; //in nautical miles
        XMLMark prevMark = null;
        double avgSpeed = averageSpeed / 60 / 60; //knots is /hour
        for (XMLCorner corner : race.getCompoundMarkSequence().getCorner()){
            int index = corner.getCompoundMarkID() - 1;
            XMLCompoundMark cm = race.getCourse().getCompoundMark().get(index);
            XMLMark mark = cm.getMark().get(0);
            if (prevMark != null){
                totalDistance += getDistance(mark, prevMark);
            }
            prevMark = mark;
        }
        //total time = total dist / average speed + race extra rounding time * number of marks
        double totalTime = totalDistance / avgSpeed * 1000 +
                raceRoundingTime * race.getCompoundMarkSequence().getCorner().size();
        return totalTime;
    }

    /**
     * gets the destance between two marks, in nautical miles.
     * @param a mark 1
     * @param b mark 2
     * @return Distance in nautical miles.
     */
    private static double getDistance(XMLMark a, XMLMark b){
        GPSCoordinate coorda = new GPSCoordinate(a.getTargetLat(), a.getTargetLng());
        GPSCoordinate coordb = new GPSCoordinate(b.getTargetLat(), b.getTargetLng());
        return GPSCoordinate.calculateDistanceNauticalMiles(coorda, coordb);
    }

    /**
     * Scales the race based on the windspeed the race is running at and the amount of time it should be completed in.
     * @param race Race to scale
     * @param windSpeed windspeed of the race, this is used with the polars
     * @param milliseconds milliseconds the race should take.
     */
    private static void scaleRace(XMLRace race, double windSpeed, double milliseconds) {
        GPSCoordinate center = getCenter(race);
        //sort the compound marks
        Collections.sort(race.getCompoundMarkSequence().getCorner(), (c1, c2) -> {
            if (c1.getSeqID() < c2.getSeqID()) return -1;
            if (c1.getSeqID() > c2.getSeqID()) return 1;
            return 0;
        });
        //sort compound mark id
        Collections.sort(race.getCourse().getCompoundMark(), (c1, c2) -> {
            if (c1.getCompoundMarkID() < c2.getCompoundMarkID()) return -1;
            if (c1.getCompoundMarkID() > c2.getCompoundMarkID()) return 1;
            return 0;
        });
        //get the fastest time it would take.
        double bestUpWindSpeed = NewPolars.setBestVMG(Bearing.fromDegrees(0), windSpeed, Bearing.fromDegrees(45)).getSpeed();
        double bestDownWindSpeed = NewPolars.setBestVMG(Bearing.fromDegrees(0), windSpeed, Bearing.fromDegrees(45)).getSpeed();
        double averageSpeed = (bestDownWindSpeed + bestUpWindSpeed) / 2;
        double raceApproximateTime = getRaceLength(race, averageSpeed);
        double scale = milliseconds / raceApproximateTime;
        Map<XMLCompoundMark, Boolean> hasBeenScaled = new HashMap<>();
        for (XMLCorner cm: race.getCompoundMarkSequence().getCorner()){
            int index = cm.getCompoundMarkID() - 1;
            XMLCompoundMark mark = race.getCourse().getCompoundMark().get(index);
            if (!hasBeenScaled.containsKey(mark)) {
                for (XMLMark m : mark.getMark()) {
                    scalePoint(m, center, scale);
                }
            }
            hasBeenScaled.put(mark, true);
        }
        for (XMLLimit limit: race.getCourseLimit().getLimit()){
            scalePoint(limit, center, scale);
        }
    }

    /**
     * Scales a point from the the center(pivot)
     * @param mark mark the scale
     * @param center center as pivot
     * @param scale scale to scale at.
     */
    private static void scalePoint(XMLMark mark, GPSCoordinate center, double scale){
        double latDiff = mark.getTargetLat() - center.getLatitude();
        double longDiff = mark.getTargetLng() - center.getLongitude();
        double latScaled = latDiff * scale + center.getLatitude();
        double longScaled = longDiff * scale + center.getLongitude();
        mark.setTargetLat(latScaled);
        mark.setTargetLng(longScaled);
    }

    /**
     * Scales a boundary from the center(pivot)
     * @param limit boundary point
     * @param center pivot
     * @param scale scale
     */
    private static void scalePoint(XMLLimit limit, GPSCoordinate center, double scale){
        double latDiff = limit.getLat() - center.getLatitude();
        double longDiff = limit.getLon() - center.getLongitude();
        double latScaled = latDiff * scale + center.getLatitude();
        double longScaled = longDiff * scale + center.getLongitude();
        limit.setLat(latScaled);
        limit.setLon(longScaled);
    }

    /**
     * Converts a Race.CourseLimit.Limit to a GPS coordinate
     * @param limit limit to convert
     * @return gps coordinate corresponding to the limit
     */
    public static GPSCoordinate limitToGPSCoordinate(XMLLimit limit){
        return new GPSCoordinate(limit.getLat(), limit.getLon());
    }

    /**
     * get new gps coordinate after rotating
     * @param pivot center point to rotating from.
     * @param point point to rotate
     * @param degrees number of degress to rotate by
     * @return the new GPSCoordinate of the transformed point.
     */
    public static GPSCoordinate rotate(GPSCoordinate pivot, GPSCoordinate point, double degrees){
        double radDeg = Math.toRadians(degrees);
        double deltaLat = (point.getLatitude() - pivot.getLatitude());
        double deltaLon = (point.getLongitude() - pivot.getLongitude());
        //map to (0,1) vector and use vector maths to rotate.
        double resLat = deltaLat * Math.cos(radDeg) - deltaLon * Math.sin(radDeg) + pivot.getLatitude();
        double resLon = deltaLat * Math.sin(radDeg) + deltaLon * Math.cos(radDeg) + pivot.getLongitude();
        return new GPSCoordinate(resLat, resLon);
    }

    /**
     * obtains the GPSCoordinates of a mark
     * @param mark mark to obtain the GPSCoordinates of
     * @return the GPSCOordinatess of a mark
     */
    public static GPSCoordinate markToGPSCoordinate(XMLMark mark){
        return new GPSCoordinate(mark.getTargetLat(), mark.getTargetLng());
    }

    /**
     * get the center of a race
     * @param race race to get the center of
     * @return GPSCoordinates of the center
     */
    public static GPSCoordinate getCenter(XMLRace race){
        double avgLat = 0;
        double avgLng = 0;
        for (XMLLimit limit: race.getCourseLimit().getLimit()){
            avgLat += limit.getLat();
            avgLng += limit.getLon();
        }
        avgLat = avgLat/race.getCourseLimit().getLimit().size();
        avgLng = avgLng/race.getCourseLimit().getLimit().size();
        return new GPSCoordinate(avgLat, avgLng);
    }

    /**
     * gets the angle of a line
     * @param coord1 point a of the line
     * @param coord2 point b of the line
     * @return the angle in degrees that the bearing of the line is [-180, 180]
     */
    public static double getLineAngle(GPSCoordinate coord1, GPSCoordinate coord2){
        double dx = coord1.getLongitude() - coord2.getLongitude();
        double dy = coord1.getLatitude() - coord2.getLatitude();
        return Math.atan2(dy, dx)/Math.PI * 180;
    }


    /**
     * sets the current race time of the xml
     * @param raceXML race xml to alter
     * @param racePrestartTime prestart time
     * @param racePreparatoryTime preparatory time
     * @deprecated this should be used from the RaceXMLCreator not from this function
     */
    public static void setRaceXMLAtCurrentTimeToNow(XMLRace raceXML, long racePrestartTime, long racePreparatoryTime){
        //The start time is current time + 4 minutes. prestart is 3 minutes, and we add another minute.
        long millisecondsToAdd = racePrestartTime + racePreparatoryTime;
        long secondsToAdd = millisecondsToAdd / 1000;

        DateTimeFormatter dateFormat = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ssZ");
        ZonedDateTime creationTime = ZonedDateTime.now();
        raceXML.setCreationTimeDate(dateFormat.format(creationTime));
        raceXML.getRaceStartTime().setTime(dateFormat.format(creationTime.plusSeconds(secondsToAdd)));
    }

    /**
     * Sets the xml description of the race to show the race was created now, and starts in 4 minutes
     * @param raceXML The race.xml contents.
     */
    public static void setRaceXMLAtCurrentTimeToNow(XMLRace raceXML) {
        setRaceXMLAtCurrentTimeToNow(raceXML, Constants.RacePreStartTime, Constants.RacePreparatoryTime);

    }

}
