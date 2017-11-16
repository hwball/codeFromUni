package mock.app;

import mock.dataInput.PolarParser;
import mock.exceptions.EventConstructionException;
import mock.model.MockRace;
import mock.model.Polars;
import mock.model.RaceLogic;
import mock.model.SourceIdAllocator;
import mock.model.commandFactory.CompositeCommand;
import mock.model.wind.ShiftingWindGenerator;
import mock.model.wind.WindGenerator;
import mock.xml.RaceXMLCreator;
import network.Messages.Enums.RaceStatusEnum;
import network.Messages.HostGame;
import network.Messages.LatestMessages;
import shared.dataInput.*;
import shared.enums.XMLFileType;
import shared.exceptions.InvalidBoatDataException;
import shared.exceptions.InvalidRaceDataException;
import shared.exceptions.InvalidRegattaDataException;
import shared.exceptions.XMLReaderException;
import shared.model.Bearing;
import shared.model.Constants;

import java.io.IOException;
import java.net.Inet4Address;
import java.nio.charset.StandardCharsets;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;


/**
 * A Race Event, this holds all of the race's information as well as handling the connection to its clients.
 */
public class Event {

    /**
     * Contents of the various xml files.
     */
    private String raceXML;
    private String regattaXML;
    private String boatXML;
    private XMLFileType xmlFileType;
    private Polars boatPolars;
    /**
     * Data sources containing data from the xml files.
     */
    private RaceDataSource raceDataSource;
    private BoatDataSource boatDataSource;
    private RegattaDataSource regattaDataSource;
    private ConnectionAcceptor connectionAcceptor;
    private LatestMessages latestMessages;
    private CompositeCommand compositeCommand;
    /**
     * This is used to allocate source IDs.
     */
    private SourceIdAllocator sourceIdAllocator;

    private RaceLogic raceLogic;

    private Thread raceThread;
    private Thread connectionThread;
    private int mapIndex;

    /**
     * Constructs an event, using various XML files.
     * @param singlePlayer Whether or not to create a single player event.
     * @param mapIndex Specifies which map to use.
     * @param raceLength The length of the race, in milliseconds.
     * @throws EventConstructionException Thrown if we cannot create an Event for any reason.
     */
    public Event(boolean singlePlayer, int mapIndex, int raceLength) throws
            EventConstructionException {
        PolarParser.parseNewPolars("mock/polars/acc_polars.csv");
        this.mapIndex = mapIndex;
        String raceXMLFile;
        String boatsXMLFile = "mock/mockXML/boatTest.xml";
        String regattaXMLFile = "mock/mockXML/regattaTest.xml";
        switch (mapIndex){
            case 0:raceXMLFile = "mock/mockXML/ac35MapLayout.xml";
                break;
            case 1:raceXMLFile = "mock/mockXML/oMapLayout.xml";
                break;
            case 2: raceXMLFile = "mock/mockXML/iMapLayout.xml";
                break;
            case 3: raceXMLFile = "mock/mockXML/mMapLayout.xml";
                break;
            case 4:
                raceXMLFile = "mock/mockXML/raceTutorial.xml";
                boatsXMLFile = "mock/mockXML/boatTutorial.xml";
                regattaXMLFile = "mock/mockXML/regattaTutorial.xml";
                break;
            default: raceXMLFile = "mock/mockXML/ac35MapLayout.xml";

        }


        if (singlePlayer) {
            boatsXMLFile = "mock/mockXML/boatsSinglePlayer.xml";
        }

        double windAngle = 300;
        double windSpeed = 12;

        //Read XML files.
        try {

            if(mapIndex==4){
                //This is the tutorial map.
                this.raceXML = Event.setRaceXMLAtCurrentTimeToNow(XMLReader.readXMLFileToString(raceXMLFile, StandardCharsets.UTF_8), 1000, 5000);
                this.raceXML = RaceXMLCreator.alterRaceToWind(this.raceXML, XMLFileType.Contents, -1);

            } else {
                this.raceXML = Event.setRaceXMLAtCurrentTimeToNow(XMLReader.readXMLFileToString(raceXMLFile, StandardCharsets.UTF_8));
                this.raceXML = RaceXMLCreator.alterRaceToWind(this.raceXML, XMLFileType.Contents, windAngle);
                this.raceXML = RaceXMLCreator.scaleRaceSize(raceXML,
                        windSpeed, raceLength);
            }

            this.boatXML = XMLReader.readXMLFileToString(boatsXMLFile, StandardCharsets.UTF_8);
            this.regattaXML = XMLReader.readXMLFileToString(regattaXMLFile, StandardCharsets.UTF_8);

        } catch (XMLReaderException | InvalidRaceDataException e) {
            throw new EventConstructionException("Could not read XML files.", e);
        }

        this.xmlFileType = XMLFileType.Contents;

        this.boatPolars = PolarParser.parse("mock/polars/acc_polars.csv");


        //Parse the XML files into data sources.
        try {
            this.raceDataSource = new RaceXMLReader(this.raceXML, this.xmlFileType);
            this.boatDataSource = new BoatXMLReader(this.boatXML, this.xmlFileType);
            this.regattaDataSource = new RegattaXMLReader(this.regattaXML, this.xmlFileType);


        } catch (XMLReaderException | InvalidRaceDataException | InvalidRegattaDataException | InvalidBoatDataException e) {
            throw new EventConstructionException("Could not parse XML files.", e);

        }


        this.compositeCommand = new CompositeCommand();
        this.latestMessages = new LatestMessages();

        WindGenerator windGenerator = new ShiftingWindGenerator(
                Bearing.fromDegrees(windAngle),
                windSpeed
        );

        MockRace mockRace = new MockRace(
                boatDataSource,
                raceDataSource,
                regattaDataSource,
                this.boatPolars,
                Constants.RaceTimeScale,
                windGenerator   );

        if(mapIndex==4){
            mockRace.setRacePreStartTime(1000);
            mockRace.setRacePreparatoryTime(5000);
        }


        this.raceLogic = new RaceLogic(
                mockRace,
                this.latestMessages,
                this.compositeCommand);

        this.raceThread = new Thread(raceLogic, "Event.Start()->RaceLogic thread");
        raceThread.start();


        //Create connection acceptor.
        this.sourceIdAllocator = new SourceIdAllocator(raceLogic.getRace());

        try {
            this.connectionAcceptor = new ConnectionAcceptor(latestMessages, compositeCommand, sourceIdAllocator, raceLogic);

        } catch (IOException e) {
            throw new EventConstructionException("Could not create ConnectionAcceptor.", e);
        }


        this.connectionThread = new Thread(connectionAcceptor, "Event.Start()->ConnectionAcceptor thread");
        connectionThread.start();


    }

    public void endEvent() throws IOException {
            this.connectionThread.interrupt();
            this.connectionAcceptor.closeConnection();
            this.raceThread.interrupt();
    }





    /**
     * Sets the xml description of the race to show the race was created now, and starts in 4 minutes
     * @param raceXML The race.xml contents.
     * @return String containing edited xml
     */
    public static String setRaceXMLAtCurrentTimeToNow(String raceXML) {
        return setRaceXMLAtCurrentTimeToNow(raceXML, Constants.RacePreStartTime, Constants.RacePreparatoryTime);
    }

    public static String setRaceXMLAtCurrentTimeToNow(String raceXML, long racePreStartTime, long racePreparatoryTime){

        long minutesToAdd = 10;

        DateTimeFormatter dateFormat = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ssZ");
        ZonedDateTime creationTime = ZonedDateTime.now();
        raceXML = raceXML.replace("RACE_CREATION_TIME", dateFormat.format(creationTime));
        raceXML = raceXML.replace("RACE_START_TIME", dateFormat.format(creationTime.plusMinutes(minutesToAdd)));

        return raceXML;
    }

    /**
     * Creates the needed data type for a network packet
     * @return hostGame Ac35DataType
     * @throws IOException Inet4Address issue
     */
    public HostGame getHostedGameData() throws IOException{
        String ip = Inet4Address.getLocalHost().getHostAddress();
        return new HostGame(ip, 3779,(byte) 1,(byte) 1, RaceStatusEnum.PRESTART, (byte) 1, (byte) 1);
    }


    public RaceLogic getRaceLogic() {
        return raceLogic;
    }

}
