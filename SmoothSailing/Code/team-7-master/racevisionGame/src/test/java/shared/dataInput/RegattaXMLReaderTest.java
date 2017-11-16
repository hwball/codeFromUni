package shared.dataInput;

import org.junit.Before;
import org.junit.Test;
import shared.enums.XMLFileType;
import shared.exceptions.InvalidRegattaDataException;
import shared.exceptions.XMLReaderException;

import javax.xml.transform.TransformerException;
import java.nio.charset.StandardCharsets;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

/**
 * Created by jjg64 on 19/04/17.
 * Tests RegattaXMLReader
 */
public class RegattaXMLReaderTest {
    private RegattaXMLReader regatta;


    public static RegattaDataSource createRegattaDataSource() throws InvalidRegattaDataException {

        String regattaXMLString = null;

        try {
            regattaXMLString = XMLReader.readXMLFileToString("mock/mockXML/regattaTest.xml", StandardCharsets.UTF_8);

        } catch (XMLReaderException e) {
            throw new InvalidRegattaDataException("Could not read regatta XML file into a string.", e);

        }


        try {

            RegattaDataSource raceData = new RegattaXMLReader(regattaXMLString, XMLFileType.Contents);
            return raceData;


        } catch (XMLReaderException e) {
            throw new InvalidRegattaDataException("Could not parse regatta XML file.", e);
        }


    }


    @Before
    public void findFile() {
        try {
            regatta = new RegattaXMLReader("shared/mockXML/regattaXML/regattaTest.xml", XMLFileType.ResourcePath);
        } catch (Exception e) {
            fail("Cannot find mockXML/regattaXML/regattaTest.xml in the resources folder");
        }
    }

    @Test
    public void correctValuesTest() {
        assertEquals(regatta.getRegattaID(), 3);
        assertEquals(regatta.getRegattaName(), "New Zealand Test");
        assertEquals(regatta.getCourseName(), "North Head");
        assertEquals(regatta.getCentralLatitude(), -36.82791529, 0.00000001);
        assertEquals(regatta.getCentralLongitude(), 174.81218919, 0.00000001);
        assertEquals(regatta.getCentralAltitude(), 0.00, 0.00000001);
        assertEquals(regatta.getUtcOffset(), 12.0, 0.001);
        assertEquals(regatta.getMagneticVariation(), 14.1, 0.001);
    }
}
