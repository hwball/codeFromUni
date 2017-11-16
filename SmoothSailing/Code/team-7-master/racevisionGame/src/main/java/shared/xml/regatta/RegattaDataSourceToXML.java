package shared.xml.regatta;

import shared.dataInput.RegattaDataSource;
import shared.xml.Race.XMLRace;
import shared.xml.XMLUtilities;

import javax.xml.bind.JAXBException;

/**
 * Has functions to convert a {@link shared.dataInput.RegattaDataSource} to an {@link RegattaConfig} object.
 */
public class RegattaDataSourceToXML {


    /**
     * Converts a regatta data source to an XMLRace object.
     * @param regattaDataSource The data source to convert.
     * @return The XMLRace file.
     */
    public static RegattaConfig toXML(RegattaDataSource regattaDataSource) {

        RegattaConfig regatta = new RegattaConfig();

        regatta.setCentralAltitude(regattaDataSource.getCentralAltitude());
        regatta.setCentralLatitude(regattaDataSource.getCentralLatitude());
        regatta.setCentralLongitude(regattaDataSource.getCentralLongitude());

        regatta.setCourseName(regattaDataSource.getCourseName());

        regatta.setRegattaName(regattaDataSource.getRegattaName());

        regatta.setMagneticVariation(regattaDataSource.getMagneticVariation());

        regatta.setRegattaID(regattaDataSource.getRegattaID());

        regatta.setUtcOffset(regattaDataSource.getUtcOffset());

        return regatta;
    }


    /**
     * Converts a regatta data source to an xml string.
     * @param regattaDataSource Data source to convert.
     * @return String containing xml file.
     * @throws JAXBException Thrown if it cannot be converted.
     */
    public static String toString(RegattaDataSource regattaDataSource) throws JAXBException {
        RegattaConfig regatta = toXML(regattaDataSource);
        return XMLUtilities.classToXML(regatta);
    }


}
