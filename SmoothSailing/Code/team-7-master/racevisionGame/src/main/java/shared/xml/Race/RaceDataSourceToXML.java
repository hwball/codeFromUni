package shared.xml.Race;

import shared.dataInput.RaceDataSource;
import shared.enums.RoundingType;
import shared.model.CompoundMark;
import shared.model.Corner;
import shared.model.GPSCoordinate;
import shared.model.Leg;
import shared.xml.XMLUtilities;

import javax.xml.bind.JAXBException;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;

/**
 * Has functions to convert a {@link shared.dataInput.RaceDataSource} to an {@link XMLRace} object.
 */
public class RaceDataSourceToXML {


    /**
     * Converts a race data source to an XMLRace object.
     * @param raceDataSource The data source to convert.
     * @return The XMLRace file.
     */
    public static XMLRace toXML(RaceDataSource raceDataSource) {

        //Kind of ugly. Could be refactored/split up a bit.


        XMLRace race = new XMLRace();


        race.setRaceID(raceDataSource.getRaceId());
        race.setRaceType(raceDataSource.getRaceType().toString());


        race.setCreationTimeDate(raceDataSource.getCreationDateTime().format(DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ssZ")));


        XMLRaceStartTime startTime = new XMLRaceStartTime();
        startTime.setPostpone(String.valueOf(raceDataSource.getPostponed()));
        startTime.setTime(raceDataSource.getStartDateTime().format(DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ssZ")));
        race.setRaceStartTime(startTime);


        XMLParticipants participants = new XMLParticipants();
        participants.yacht = new ArrayList<>();
        for (int i : raceDataSource.getParticipants()) {
            XMLYacht yacht = new XMLYacht();
            yacht.setSourceID(i);
            participants.yacht.add(yacht);
        }
        race.setParticipants(participants);


        XMLCourseLimit courseLimit = new XMLCourseLimit();
        courseLimit.limit = new ArrayList<>();
        for (int i = 0; i < raceDataSource.getBoundary().size(); i++) {
            XMLLimit limit = new XMLLimit();
            limit.setLat(raceDataSource.getBoundary().get(i).getLatitude());
            limit.setLon(raceDataSource.getBoundary().get(i).getLongitude());
            limit.setSeqID(i + 1);
            courseLimit.limit.add(limit);
        }
        race.setCourseLimit(courseLimit);


        XMLCourse course = new XMLCourse();
        course.compoundMark = new ArrayList<>();
        for (CompoundMark compoundMark : raceDataSource.getCompoundMarks()) {
            XMLCompoundMark xmlCompoundMark = new XMLCompoundMark();
            xmlCompoundMark.setCompoundMarkID(compoundMark.getCompoundMarkID());
            xmlCompoundMark.setName(compoundMark.getName());

            if (compoundMark.getMark1() != null) {
                XMLMark xmlMark = new XMLMark();
                xmlMark.setName(compoundMark.getMark1().getName());
                xmlMark.setSourceID(compoundMark.getMark1().getSourceID());
                xmlMark.setSeqId(1);
                xmlMark.setTargetLat(compoundMark.getMark1().getPosition().getLatitude());
                xmlMark.setTargetLng(compoundMark.getMark1().getPosition().getLongitude());

                xmlCompoundMark.getMark().add(xmlMark);
            }
            if (compoundMark.getMark2() != null) {
                XMLMark xmlMark = new XMLMark();
                xmlMark.setName(compoundMark.getMark2().getName());
                xmlMark.setSourceID(compoundMark.getMark2().getSourceID());
                xmlMark.setSeqId(2);
                xmlMark.setTargetLat(compoundMark.getMark2().getPosition().getLatitude());
                xmlMark.setTargetLng(compoundMark.getMark2().getPosition().getLongitude());

                xmlCompoundMark.getMark().add(xmlMark);
            }

            course.compoundMark.add(xmlCompoundMark);
        }
        race.setCourse(course);

        XMLCompoundMarkSequence compoundMarkSequence = new XMLCompoundMarkSequence();
        compoundMarkSequence.corner = new ArrayList<>();
        for (Corner corner : raceDataSource.getCorners()) {
            XMLCorner xmlCorner = new XMLCorner();
            xmlCorner.setZoneSize(corner.getZoneSize());
            xmlCorner.setSeqID(corner.getSeqID());
            xmlCorner.setCompoundMarkID(corner.getCompoundMarkID());
            xmlCorner.setRounding(corner.getRounding());

            compoundMarkSequence.corner.add(xmlCorner);
        }
        race.setCompoundMarkSequence(compoundMarkSequence);


        return race;
    }


    /**
     * Converts a race data source to an xml string.
     * @param raceDataSource Data source to convert.
     * @return String containing xml file.
     * @throws JAXBException Thrown if it cannot be converted.
     */
    public static String toString(RaceDataSource raceDataSource) throws JAXBException {
        XMLRace race = toXML(raceDataSource);
        return XMLUtilities.classToXML(race);
    }


}
