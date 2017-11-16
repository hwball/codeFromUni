package shared.dataInput;

import mock.app.Event;
import shared.enums.XMLFileType;
import shared.exceptions.InvalidRaceDataException;
import shared.exceptions.XMLReaderException;

import javax.xml.transform.TransformerException;
import java.nio.charset.StandardCharsets;

public class RaceXMLReaderTest {



    public static RaceDataSource createRaceDataSource() throws InvalidRaceDataException {

        String raceXMLString = null;

        try {
            raceXMLString = Event.setRaceXMLAtCurrentTimeToNow(XMLReader.readXMLFileToString("mock/mockXML/raceTest.xml", StandardCharsets.UTF_8));

        } catch (XMLReaderException e) {
            throw new InvalidRaceDataException("Could not read race XML file into a string.", e);

        }


        try {

            RaceXMLReader raceData = new RaceXMLReader(raceXMLString, XMLFileType.Contents);
            return raceData;


        } catch (XMLReaderException e) {
            throw new InvalidRaceDataException("Could not parse race XML file.", e);
        }


    }
}
