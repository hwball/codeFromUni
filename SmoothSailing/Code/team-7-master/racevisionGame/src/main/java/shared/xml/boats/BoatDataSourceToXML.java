package shared.xml.boats;

import shared.dataInput.BoatDataSource;
import shared.dataInput.RaceDataSource;
import shared.enums.RoundingType;
import shared.model.Boat;
import shared.model.CompoundMark;
import shared.model.Leg;
import shared.model.Mark;
import shared.xml.Race.*;
import shared.xml.XMLUtilities;

import javax.xml.bind.JAXBException;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;

/**
 * Has functions to convert a {@link shared.dataInput.BoatDataSource} to an {@link BoatConfig} object.
 */
public class BoatDataSourceToXML {


    /**
     * Converts a boat data source to an XMLRace object.
     * @param boatDataSource The data source to convert.
     * @return The XMLRace file.
     */
    public static BoatConfig toXML(BoatDataSource boatDataSource) {
        BoatConfig boatConfig = new BoatConfig();

        boatConfig.boats = new BoatConfig.Boats();
        boatConfig.boats.boat = new ArrayList<>();


        for (Boat boat : boatDataSource.getBoats().values()) {
            BoatConfig.Boats.Boat xmlBoat = new BoatConfig.Boats.Boat();

            xmlBoat.setType("Yacht");
            xmlBoat.setBoatName(boat.getName());
            xmlBoat.setSourceID(boat.getSourceID());
            xmlBoat.setStoweName(boat.getCountry());
            xmlBoat.setShortName(boat.getCountry());

            BoatConfig.Boats.Boat.GPSposition position = new BoatConfig.Boats.Boat.GPSposition();
            position.setX(boat.getPosition().getLongitude());
            position.setY(boat.getPosition().getLatitude());
            position.setZ(0);
            xmlBoat.setGPSposition(position);

            boatConfig.boats.boat.add(xmlBoat);
        }


        for (Mark mark : boatDataSource.getMarkerBoats().values()) {
            BoatConfig.Boats.Boat xmlBoat = new BoatConfig.Boats.Boat();

            xmlBoat.setType("Mark");
            xmlBoat.setBoatName(mark.getName());
            xmlBoat.setSourceID(mark.getSourceID());

            BoatConfig.Boats.Boat.GPSposition position = new BoatConfig.Boats.Boat.GPSposition();
            position.setX(mark.getPosition().getLongitude());
            position.setY(mark.getPosition().getLatitude());
            position.setZ(0);
            xmlBoat.setGPSposition(position);

            boatConfig.boats.boat.add(xmlBoat);
        }

        return boatConfig;
    }


    /**
     * Converts a boat data source to an xml string.
     * @param boatDataSource Data source to convert.
     * @return String containing xml file.
     * @throws JAXBException Thrown if it cannot be converted.
     */
    public static String toString(BoatDataSource boatDataSource) throws JAXBException {
        BoatConfig boats = toXML(boatDataSource);
        return XMLUtilities.classToXML(boats);
    }


}
