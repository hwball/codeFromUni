package shared.dataInput;

import network.Messages.Enums.RaceTypeEnum;
import org.w3c.dom.Element;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import shared.enums.RoundingType;
import shared.enums.XMLFileType;
import shared.exceptions.InvalidRaceDataException;
import shared.exceptions.XMLReaderException;
import shared.model.*;

import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;

/**
 * Xml Reader class for Race XML used for the race.
 */
public class RaceXMLReader extends XMLReader implements RaceDataSource {


    /**
     * The GPS coordinate of the top left of the race boundary.
     */
    private GPSCoordinate mapTopLeft;

    /**
     * The GPS coordinate of the bottom right of the race boundary.
     */
    private GPSCoordinate mapBottomRight;


    /**
     * A list of GPS coordinates that make up the boundary of the race.
     */
    private final List<GPSCoordinate> boundary = new ArrayList<>();

    /**
     * A map between compoundMarkID and a CompoundMark for all CompoundMarks in a race.
     */
    private final Map<Integer, CompoundMark> compoundMarkMap = new HashMap<>();

    /**
     * A list of boat sourceIDs participating in the race.
     */
    private final List<Integer> participants = new ArrayList<>();

    /**
     * A list of legs in the race.
     */
    private final List<Leg> legs = new ArrayList<>();

    /**
     * List of corners in the race
     */
    private final List<Corner> cornersList = new ArrayList<>();

    /**
     * The time that the race.xml file was created.
     */
    private ZonedDateTime creationTimeDate;

    /**
     * The time that the race should start at, if it hasn't been postponed.
     */
    private ZonedDateTime raceStartTime;

    /**
     * Whether or not the race has been postponed.
     */
    private boolean postpone;


    /**
     * The ID number of the race.
     */
    private int raceID;

    /**
     * The type of the race.
     */
    private RaceTypeEnum raceType;


    private int sequenceNumber = 0;



    /**
     * Constructor for Streamed Race XML
     * @param file The file to read.
     * @param type How to read the file - e.g., load as resource.
     * @throws XMLReaderException Thrown if an XML reader cannot be constructed for the given file.
     * @throws InvalidRaceDataException Thrown if the XML file is invalid in some way.
     */
    public RaceXMLReader(String file, XMLFileType type) throws XMLReaderException, InvalidRaceDataException {

        super(file, type);


        //Attempt to read race xml file.
        try {
            read();
        } catch (Exception e) {
            throw new InvalidRaceDataException("An error occurred while reading the race xml file", e);
        }
    }

    /**
     * Reads the contents of the race xml file.
     * @throws InvalidRaceDataException Thrown if we cannot parse the document properly.
     */
    private void read() throws InvalidRaceDataException {
        readRace();
        readParticipants();
        readCourse();
    }

    /**
     * Reads race related data from the race xml file.
     */
    private void readRace() {

        DateTimeFormatter dateFormat = DateTimeFormatter.ISO_OFFSET_DATE_TIME;
        Element settings = (Element) doc.getElementsByTagName("Race").item(0);
        NamedNodeMap raceTimeTag = doc.getElementsByTagName("RaceStartTime").item(0).getAttributes();

        if (raceTimeTag.getNamedItem("Time") != null) {
            dateFormat = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ssZ");
        }


        //Race ID.
        raceID = Integer.parseInt(getTextValueOfNode(settings, "RaceID"));

        //Race type.
        String raceTypeString = getTextValueOfNode(settings, "RaceType");
        raceType = RaceTypeEnum.fromString(raceTypeString);

        //XML creation time.
        creationTimeDate = ZonedDateTime.parse(getTextValueOfNode(settings, "CreationTimeDate"), dateFormat);

        //Race start time.
        if (raceTimeTag.getNamedItem("Time") != null) {
            raceStartTime = ZonedDateTime.parse(raceTimeTag.getNamedItem("Time").getTextContent(), dateFormat);

        } else {
            raceStartTime = ZonedDateTime.parse(raceTimeTag.getNamedItem("Start").getTextContent(), dateFormat);
        }

        //Postpone status.
        postpone = Boolean.parseBoolean(raceTimeTag.getNamedItem("Postpone").getTextContent());
    }


    /**
     * Reads in the participants for this race.
     */
    private void readParticipants() {

        //Gets the "<Participants>..</Participants>" element.
        Element participants = (Element) doc.getElementsByTagName("Participants").item(0);

        //Gets the number of participants.
        int numberOfParticipants = participants.getChildNodes().getLength();

        //For each participant, read its sourceID.
        for (int i = 0; i < numberOfParticipants; i++) {

            //Get the participating yacht.
            Node yacht = participants.getChildNodes().item(i);

            if (yacht.getNodeName().equals("Yacht")) {
                if (exists(yacht, "SourceID")) {

                    //If the node is a valid yacht with a sourceID, add it to participant list.
                    int sourceID = Integer.parseInt(yacht.getAttributes().getNamedItem("SourceID").getTextContent());
                    this.participants.add(sourceID);

                }
            }

        }
    }


    /**
     * Reads course data from the xml file.
     * @throws InvalidRaceDataException Thrown if we cannot parse the document properly.
     */
    private void readCourse() throws InvalidRaceDataException {
        readCompoundMarks();
        readCompoundMarkSequence();
        readCourseLimits();
        readMapTopLeft();
        readMapBottomRight();
    }

    /**
     * Indexes CompoundMark elements by their ID for use in generating the course, and populates list of Markers.
     * @throws InvalidRaceDataException thrown if we cannot create a compound mark from the document.
     * @see CompoundMark
     */
    private void readCompoundMarks() throws InvalidRaceDataException {

        //Gets the "<Course>...</..>" element.
        Element course = (Element) doc.getElementsByTagName("Course").item(0);

        //Get the list of CompoundMark elements.
        NodeList compoundMarkList = course.getElementsByTagName("CompoundMark");

        //Number of compound marks in the course.
        int numberOfCompoundMarks = compoundMarkList.getLength();

        //For each CompoundMark element, create a CompoundMark object.
        for(int i = 0; i < numberOfCompoundMarks; i++) {

            //Get the CompoundMark element.
            Element compoundMarkElement = (Element) compoundMarkList.item(i);


            //Convert to CompoundMark object.
            CompoundMark compoundMark = createCompoundMark(compoundMarkElement);

            compoundMarkMap.put(compoundMark.getId(), compoundMark);

        }
    }

    /**
     * Generates a CompoundMark from a given CompondMark element.
     * @param compoundMarkElement The CompoundMark element to turn into a CompoundMark object.
     * @return The corresponding CompoundMark object.
     * @throws InvalidRaceDataException If the element cannot be converted into a CompoundMark.
     */
    private CompoundMark createCompoundMark(Element compoundMarkElement) throws InvalidRaceDataException {

        //CompoundMark ID.
        int compoundMarkID = getCompoundMarkID(compoundMarkElement);

        //CompoundMark name.
        String compoundMarkName = getCompoundMarkName(compoundMarkElement);


        //Get the list of marks within the compound mark.
        NodeList marks = compoundMarkElement.getElementsByTagName("Mark");
        CompoundMark compoundMark;

        switch(marks.getLength()) {
            case 1: {
                //Create the Mark sub-object.
                Mark mark1 = createMark((Element) marks.item(0));

                //Create compound mark.
                compoundMark = new CompoundMark(compoundMarkID, compoundMarkName, mark1);
                break;

            } case 2: {
                //Create the Mark sub-objects.
                Mark mark1 = createMark((Element) marks.item(0));
                Mark mark2 = createMark((Element) marks.item(1));

                //Create compound mark.
                compoundMark = new CompoundMark(compoundMarkID, compoundMarkName, mark1, mark2);
                break;

            } default: {
                throw new InvalidRaceDataException("Cannot create CompoundMark from " + compoundMarkElement.toString());
            }
        }

        return compoundMark;

    }


    /**
     * Gets a mark from an Element.
     * @param mark The {@link Element} describing the {@link Mark}.
     * @return The {@link Mark}.
     */
    private Mark createMark(Element mark) {

        //Source ID.
        int sourceID = Integer.parseInt(mark.getAttribute("SourceID"));

        //Name.
        String name = mark.getAttribute("Name");

        //Latitude.
        double latitude = Double.parseDouble(mark.getAttribute("TargetLat"));

        //Longitude.
        double longitude = Double.parseDouble(mark.getAttribute("TargetLng"));

        //Create mark.
        return new Mark(sourceID, name, new GPSCoordinate(latitude, longitude));

    }


    /**
     * Reads "compoundMarkID" attribute of CompoundMark or Corner element.
     * @param element with "compoundMarkID" attribute.
     * @return value of "compoundMarkID" attribute.
     */
    private int getCompoundMarkID(Element element) {
        return Integer.parseInt(element.getAttribute("CompoundMarkID"));
    }


    /**
     * Reads "Name" attribute of a CompoundMark element.
     * @param element The CompoundMark element with a "Name" attribute.
     * @return value of "name" attribute.
     */
    private String getCompoundMarkName(Element element) {
        return element.getAttribute("Name");
    }

    private String getCompoundMarkRounding(Element element){return element.getAttribute("Rounding");}


    /**
     * Populates list of legs given CompoundMarkSequence element and referenced CompoundMark elements.
     */
    private void readCompoundMarkSequence() {

        //The "<CompoundMarkSequence>...</...>" element. This contains a sequence of Corner elements.
        Element compoundMarkSequence = (Element) doc.getElementsByTagName("CompoundMarkSequence").item(0);

        //Gets the list of Corner elements.
        NodeList corners = compoundMarkSequence.getElementsByTagName("Corner");

        //Gets the first corner.
        Element cornerElement = (Element) corners.item(0);

        //Gets the ID number of this corner element.
        int cornerID = getCompoundMarkID(cornerElement);

        //gets the Rounding of this corner element
        String cornerRounding = getCompoundMarkRounding(cornerElement);

        int cornerSeq = Integer.parseInt(getAttribute(cornerElement, "SeqID"));

        cornersList.add(new Corner(cornerID, cornerSeq, cornerRounding, 3));

        //Gets the CompoundMark associated with this corner.
        CompoundMark lastCompoundMark = this.compoundMarkMap.get(cornerID);

        //The name of the leg is the name of the first compoundMark in the leg.
        String legName = lastCompoundMark.getName();

        //Sets the rounding type of this compound mark

        lastCompoundMark.setRoundingType(RoundingType.getValueOf(cornerRounding));

        //For each following corner, create a leg between cornerN and cornerN+1.
        for(int i = 1; i < corners.getLength(); i++) {

            //Gets the next corner element.
            cornerElement = (Element) corners.item(i);

            //Gets the ID number of this corner element.
            cornerID = getCompoundMarkID(cornerElement);

            cornerSeq = Integer.parseInt(getAttribute(cornerElement, "SeqID"));

            cornersList.add(new Corner(cornerID, cornerSeq, getCompoundMarkRounding(cornerElement), 3));
            //gets the Rounding of this corner element
            cornerRounding = getCompoundMarkRounding(cornerElement);

            //Gets the CompoundMark associated with this corner.
            CompoundMark currentCompoundMark = this.compoundMarkMap.get(cornerID);

            //Sets the rounding type of this compound mark
            currentCompoundMark.setRoundingType(RoundingType.getValueOf(cornerRounding));

            //Create a leg from these two adjacent compound marks.
            Leg leg = new Leg(legName, lastCompoundMark, currentCompoundMark, i - 1);
            legs.add(leg);

            //Prepare for next iteration.
            lastCompoundMark = currentCompoundMark;
            legName = lastCompoundMark.getName();

        }

    }


    /**
     * Reads the boundary limits of the course.
     */
    private void readCourseLimits() {

        //The "<CourseLimit>...</...>" element. This contains a sequence of Limit elements.
        Element courseLimit = (Element) doc.getElementsByTagName("CourseLimit").item(0);

        //Get the list of Limit elements.
        NodeList limitList = courseLimit.getElementsByTagName("Limit");

        //For each limit element...
        for(int i = 0; i < limitList.getLength(); i++) {

            //Get the Limit element.
            Element limit = (Element) limitList.item(i);

            //Convert to GPSCoordinate.
            double latitude = Double.parseDouble(limit.getAttribute("Lat"));
            double longitude = Double.parseDouble(limit.getAttribute("Lon"));
            boundary.add(new GPSCoordinate(latitude, longitude));

        }
    }


    /**
     * Reads the gps coordinate of the top left of the map, using the course limits.
     */
    private void readMapTopLeft(){

        double minLatitude = boundary.stream().min(Comparator.comparingDouble(GPSCoordinate::getLatitude)).get().getLatitude();

        double minLongitude = boundary.stream().min(Comparator.comparingDouble(GPSCoordinate::getLongitude)).get().getLongitude();

        mapTopLeft = new GPSCoordinate(minLatitude, minLongitude);

    }

    /**
     * Reads the gps coordinate of the bottom right of the map, using the course limits.
     */
    private void readMapBottomRight(){

        double maxLatitude = boundary.stream().max(Comparator.comparingDouble(GPSCoordinate::getLatitude)).get().getLatitude();

        double maxLongitude = boundary.stream().max(Comparator.comparingDouble(GPSCoordinate::getLongitude)).get().getLongitude();

        mapBottomRight = new GPSCoordinate(maxLatitude, maxLongitude);

    }



    public List<GPSCoordinate> getBoundary() {
        return boundary;
    }

    public GPSCoordinate getMapTopLeft() {
        return mapTopLeft;
    }

    public GPSCoordinate getMapBottomRight() {
        return mapBottomRight;
    }

    public List<Leg> getLegs() {
        return legs;
    }

    public List<CompoundMark> getCompoundMarks() {
        return new ArrayList<>(compoundMarkMap.values());
    }


    public ZonedDateTime getCreationDateTime() {
        return creationTimeDate;
    }

    public ZonedDateTime getStartDateTime() {
        return raceStartTime;
    }

    public int getRaceId() {
        return raceID;
    }

    public RaceTypeEnum getRaceType() {
        return raceType;
    }

    public boolean getPostponed() {
        return postpone;
    }

    public List<Integer> getParticipants() {
        return participants;
    }

    public List<Corner> getCorners() {
        return cornersList;
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

    @Override
    public void setStartDateTime(ZonedDateTime time) {
        raceStartTime = time;
    }
}
