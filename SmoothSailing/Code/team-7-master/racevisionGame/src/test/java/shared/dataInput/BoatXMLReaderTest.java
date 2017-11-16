package shared.dataInput;


import org.junit.Before;
import org.junit.Test;
import shared.enums.XMLFileType;
import shared.exceptions.InvalidBoatDataException;
import shared.exceptions.XMLReaderException;
import shared.model.Boat;
import shared.model.Mark;

import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.*;

/**
 * Created by cbt24 on 10/05/17.
 */
public class BoatXMLReaderTest {

    private BoatDataSource boatData;
    private List<Boat> boats;
    private List<Mark> marks;


    public static BoatDataSource createBoatDataSource() throws InvalidBoatDataException {

        String boatXMLString = null;

        try {
            boatXMLString = XMLReader.readXMLFileToString("mock/mockXML/boatTest.xml", StandardCharsets.UTF_8);

        } catch (XMLReaderException e) {
            throw new InvalidBoatDataException("Could not read boat XML file into a string.", e);

        }


        try {

            BoatDataSource boatData = new BoatXMLReader(boatXMLString, XMLFileType.Contents);
            return boatData;


        } catch (XMLReaderException e) {
            throw new InvalidBoatDataException("Could not parse boat XML file.", e);
        }


    }

    @Before
    public void setUp() throws InvalidBoatDataException {

        boatData = createBoatDataSource();

        boats = new ArrayList<>(boatData.getBoats().values());
        marks = new ArrayList<>(boatData.getMarkerBoats().values());

    }

    @Test
    public void boatsReadNameFromFile() {
        String[] names = {
                "Team ORACLE USA","Land Rover BAR","SoftBank Team Japan","Groupama Team France","Artemis Racing","Emirates Team New Zealand"
        };
        for(int i = 0; i < boats.size(); i++) {
            assertEquals(names[i], boats.get(i).getName());
        }
    }

    @Test
    public void marksReadNameFromFile() {
        String[] names = {
                "PRO","PIN","Marker1","WGL","WGR","LGL","LGR","FL","FR"
        };
        for(int i = 0; i < marks.size(); i++) {
            assertEquals(names[i], marks.get(i).getName());
        }
    }

    //TODO should add more tests for various BoatXMLReader functions.
}
