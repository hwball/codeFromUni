package mock.model;


import mock.exceptions.SourceIDAllocationException;
import network.Messages.Enums.RaceStatusEnum;

import java.util.ArrayList;
import java.util.List;

/**
 * This class is responsible for allocating boat source IDs for use in a race, upon request.
 */
public class SourceIdAllocator {


    /**
     * The race we are allocating for.
     */
    private MockRace mockRace;



    /**
     * Creates a SourceIdAllocator for a given race.
     * @param mockRace Race to allocate source IDs for.
     */
    public SourceIdAllocator(MockRace mockRace) {
        this.mockRace = mockRace;
    }


    /**
     * Allocates a source ID for a boat.
     * @return The allocated source ID.
     * @throws SourceIDAllocationException Thrown if we cannot allocate any more source IDs.
     */
    public synchronized int  allocateSourceID() throws SourceIDAllocationException {

        if (!((mockRace.getRaceStatusEnum() == RaceStatusEnum.PRESTART)
                || (mockRace.getRaceStatusEnum() == RaceStatusEnum.WARNING))) {
            throw new SourceIDAllocationException("Could not allocate a source ID. Can only allocate during pre-start or warning period. It is currently: " + mockRace.getRaceStatusEnum());
        }

        List<Integer> allocatedIDs = mockRace.getRaceDataSource().getParticipants();

        List<Integer> allIDs = new ArrayList<>(mockRace.getBoatDataSource().getBoats().keySet());

        //Get list of unallocated ids.
        List<Integer> unallocatedIDs = new ArrayList<>(allIDs);
        unallocatedIDs.removeAll(allocatedIDs);


        if (!unallocatedIDs.isEmpty()) {

            int sourceID = unallocatedIDs.remove(0);

            mockRace.generateMockBoat(sourceID);

            return sourceID;

        } else {
            throw new SourceIDAllocationException("Could not allocate a source ID.");

        }
    }


    /**
     * Returns a source ID to the source ID allocator, so that it can be reused.
     * @param sourceID Source ID to return.
     */
    public void returnSourceID(Integer sourceID) {
        mockRace.removeMockBoat(sourceID);
    }
}
