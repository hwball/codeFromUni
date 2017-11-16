package mock.model.commandFactory;

import mock.exceptions.CommandConstructionException;
import mock.exceptions.SourceIDAllocationException;
import mock.model.*;
import network.Messages.BoatAction;
import network.Messages.Enums.BoatActionEnum;
import network.Messages.Enums.RaceStatusEnum;
import org.junit.Before;
import org.junit.Test;
import shared.exceptions.InvalidBoatDataException;
import shared.exceptions.InvalidRaceDataException;
import shared.exceptions.InvalidRegattaDataException;
import shared.model.Bearing;

import static org.testng.Assert.*;
import static org.mockito.Mockito.mock;

/**
 * Created by connortaylorbrown on 4/08/17.
 */
public class WindCommandTest {
    private MockRace race;
    private SourceIdAllocator allocator;
    private MockBoat boat;
    private Command upwind;
    private Command downwind;
    private double initial;

    private double offset = 3.0;

    @Before
    public void setUp() throws CommandConstructionException, InvalidBoatDataException, InvalidRegattaDataException, InvalidRaceDataException, SourceIDAllocationException {
        race = MockRaceTest.createMockRace();
        allocator = new SourceIdAllocator(race);
        race.setRaceStatusEnum(RaceStatusEnum.PRESTART);
        allocator.allocateSourceID();
        boat = race.getBoats().get(0);



        //when(race.getWindDirection()).thenReturn(Bearing.fromDegrees(0.0));
        boat.setBearing(Bearing.fromDegrees(45.0));

        BoatAction upwindAction = new BoatAction(BoatActionEnum.UPWIND);
        upwindAction.setSourceID(boat.getSourceID());

        BoatAction downwindAction = new BoatAction(BoatActionEnum.DOWNWIND);
        downwindAction.setSourceID(boat.getSourceID());

        upwind = CommandFactory.createCommand(race, upwindAction);
        downwind = CommandFactory.createCommand(race, downwindAction);

        initial = boat.getBearing().degrees();
    }

    /**
     * Ensure the difference between initial and final angle is 3 degrees
     */
    @Test
    public void upwindCommandDecreasesAngle() {
        upwind.execute();
        assertEquals(initial - boat.getBearing().degrees(), -offset, 1e-5);
    }

    @Test
    public void downwindCommandIncreasesAngle() {
        downwind.execute();
        assertEquals(initial - boat.getBearing().degrees(), offset, 1e-5);
    }
}
