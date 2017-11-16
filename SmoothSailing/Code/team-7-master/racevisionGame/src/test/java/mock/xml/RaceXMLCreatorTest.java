package mock.xml;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import shared.dataInput.RaceXMLReader;
import shared.exceptions.InvalidRaceDataException;
import shared.exceptions.XMLReaderException;

import javax.xml.bind.JAXBException;

import static org.mockito.Mockito.mock;

public class RaceXMLCreatorTest {

    String fileToTest = "mock/mockXML/raceSchemaTest.xml";
    @Mock
    RaceXMLReader reader;


    @Before
    public void setup() throws XMLReaderException, JAXBException, InvalidRaceDataException {

    }

    @Test
    public void getLineAngleTest(){

    }

}
