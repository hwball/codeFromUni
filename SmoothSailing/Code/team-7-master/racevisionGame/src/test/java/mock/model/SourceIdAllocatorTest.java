package mock.model;

import mock.exceptions.SourceIDAllocationException;
import network.Messages.Enums.RaceStatusEnum;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.*;


/**
 * Tests if allocating source IDs works.
 */
public class SourceIdAllocatorTest {

    private MockRace mockRace;
    private SourceIdAllocator sourceIdAllocator;


    @Before
    public void setUp() throws Exception {

        mockRace = MockRaceTest.createMockRace();

        sourceIdAllocator = new SourceIdAllocator(mockRace);

    }


    /**
     * Tests that allocation fails when we don't have any source IDs to allocate.
     */
    @Test
    public void emptyAllocationTest() {

        mockRace.getRaceDataSource().getParticipants().removeAll(mockRace.getBoatDataSource().getBoats().keySet());
        mockRace.getRaceDataSource().getParticipants().addAll(mockRace.getBoatDataSource().getBoats().keySet());


        try {
            int sourceID = sourceIdAllocator.allocateSourceID();

            fail("Exception should have been thrown, but wasn't.");

        } catch (SourceIDAllocationException e) {

            //We expect this exception to be thrown - success.

        }

    }


    /**
     * Tests that we can allocate a source ID.
     * @throws Exception Thrown in case of error.
     */
    @Test
    public void allocationTest() throws Exception {

        mockRace.setRaceStatusEnum(RaceStatusEnum.PRESTART);

        int sourceID = sourceIdAllocator.allocateSourceID();

    }


    /**
     * Tests that we can allocate source IDs, but it will eventually be unable to allocate source IDs.
     */
    @Test
    public void allocationEventuallyFailsTest() {

        while (true) {

            try {
                int sourceID = sourceIdAllocator.allocateSourceID();

            } catch (SourceIDAllocationException e) {
                //We expect to encounter this exception after enough allocations - success.
                break;

            }

        }

    }


    /**
     * Tests if we can allocate a source ID, return it, and reallocate it.
     * @throws Exception Thrown in case of error.
     */
    @Test
    public void reallocationTest() throws Exception {

        mockRace.setRaceStatusEnum(RaceStatusEnum.PRESTART);

        //Allocate.
        int sourceID = sourceIdAllocator.allocateSourceID();

        //Return.
        sourceIdAllocator.returnSourceID(sourceID);

        //Reallocate.
        int sourceID2 = sourceIdAllocator.allocateSourceID();

    }
}
