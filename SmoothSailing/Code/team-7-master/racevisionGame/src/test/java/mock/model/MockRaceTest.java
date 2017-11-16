package mock.model;

import mock.dataInput.PolarParserTest;
import mock.model.wind.ConstantWindGenerator;
import mock.model.wind.WindGenerator;
import shared.dataInput.*;
import shared.exceptions.InvalidBoatDataException;
import shared.exceptions.InvalidRaceDataException;
import shared.exceptions.InvalidRegattaDataException;
import shared.model.Bearing;
import shared.model.Constants;

public class MockRaceTest {
//TODO


    /**
     * Creates a MockRace for use in testing.
     * Has a constant wind generator.
     * @return MockRace for testing.
     * @throws InvalidBoatDataException If the BoatDataSource cannot be created.
     * @throws InvalidRaceDataException If the RaceDataSource cannot be created.
     * @throws InvalidRegattaDataException If the RegattaDataSource cannot be created.
     */
    public static MockRace createMockRace() throws InvalidBoatDataException, InvalidRaceDataException, InvalidRegattaDataException {

        BoatDataSource boatDataSource = BoatXMLReaderTest.createBoatDataSource();
        RaceDataSource raceDataSource = RaceXMLReaderTest.createRaceDataSource();
        RegattaDataSource regattaDataSource = RegattaXMLReaderTest.createRegattaDataSource();


        Polars polars = PolarParserTest.createPolars();

        WindGenerator windGenerator = new ConstantWindGenerator(Bearing.fromDegrees(230), 10);

        MockRace mockRace = new MockRace(boatDataSource, raceDataSource, regattaDataSource, polars, Constants.RaceTimeScale, windGenerator);


        return mockRace;

    }
}
