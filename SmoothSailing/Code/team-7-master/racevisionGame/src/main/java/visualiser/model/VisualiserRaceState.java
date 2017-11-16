package visualiser.model;


import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.paint.Color;
import network.Messages.Enums.BoatStatusEnum;
import shared.dataInput.BoatDataSource;
import shared.dataInput.RaceDataSource;
import shared.dataInput.RegattaDataSource;
import shared.exceptions.BoatNotFoundException;
import shared.exceptions.MarkNotFoundException;
import shared.model.*;

import java.time.Duration;
import java.util.*;


/**
 * This class contains all of the state of a race on the client (visualiser) side.
 */
public class VisualiserRaceState extends RaceState {


    /**
     * A list of boats in the race.
     */
    private ObservableList<VisualiserBoat> boats;

    /**
     * The source ID of the boat assigned to the player.
     * 0 if no boat has been assigned.
     */
    private int playerBoatID;


    /**
     * Maps between a Leg to a list of boats, in the order that they finished the leg.
     * Used by the Sparkline to ensure it has correct information.
     * TODO BUG: if we receive a race.xml file during the race, then we need to add/remove legs to this, without losing information.
     */
    private Map<Leg, List<VisualiserBoat>> legCompletionOrder;




    /**
     * An array of colors used to assign colors to each boat - passed in to the VisualiserRace constructor.
     */
    private List<Color> unassignedColors = new ArrayList<>(Arrays.asList(
            Color.BLUEVIOLET,
            Color.BLACK,
            Color.RED,
            Color.ORANGE,
            Color.DARKOLIVEGREEN,
            Color.LIMEGREEN,
            Color.PURPLE,
            Color.DARKGRAY,
            Color.YELLOW
            //TODO may need to add more colors.
    ));


    /**
     * Constructs a visualiser race which models a yacht race.
     * @param raceDataSource The raceDataSource to initialise with.
     * @param regattaDataSource The regattaDataSource to initialise with.
     * @param boatDataSource The boatDataSource to initialise with.
     */
    public VisualiserRaceState(RaceDataSource raceDataSource, RegattaDataSource regattaDataSource, BoatDataSource boatDataSource) {

        this.boats = FXCollections.observableArrayList();

        this.playerBoatID = 0;

        this.legCompletionOrder = new HashMap<>();


        setRaceDataSource(raceDataSource);
        setRegattaDataSource(regattaDataSource);
        setBoatDataSource(boatDataSource);

    }




    /**
     * Sets the race data source for this race to a new RaceDataSource.
     * Uses the boundary and legs specified by the new RaceDataSource.
     * @param raceDataSource The new RaceDataSource to use.
     */
    public void setRaceDataSource(RaceDataSource raceDataSource) {
        super.setRaceDataSource(raceDataSource);

        if (getBoatDataSource() != null) {
            this.generateVisualiserBoats(this.boats, getBoatDataSource().getBoats(), getRaceDataSource().getParticipants(), unassignedColors);
        }

        initialiseLegCompletionOrder();
    }

    /**
     * Sets the boat data source for this race to a new BoatDataSource.
     * Uses the marker boats specified by the new BoatDataSource.
     * @param boatDataSource The new BoatDataSource to use.
     */
    public void setBoatDataSource(BoatDataSource boatDataSource) {
        super.setBoatDataSource(boatDataSource);

        if (getRaceDataSource() != null) {
            this.generateVisualiserBoats(this.boats, getBoatDataSource().getBoats(), getRaceDataSource().getParticipants(), unassignedColors);
        }
    }


    /**
     * Sets the regatta data source for this race to a new RegattaDataSource.
     * @param regattaDataSource The new RegattaDataSource to use.
     */
    public void setRegattaDataSource(RegattaDataSource regattaDataSource) {
        super.setRegattaDataSource(regattaDataSource);

    }


    /**
     * Initialises the {@link #legCompletionOrder} map.
     */
    public void initialiseLegCompletionOrder() {
        //Initialise the leg completion order map.
        for (Leg leg : getLegs()) {
            this.legCompletionOrder.put(leg, new ArrayList<>(this.boats.size()));
        }
    }



    /**
     * Generates a list of VisualiserBoats given a list of Boats, and a list of participating boats.
     * This will add VisualiserBoats for newly participating sourceID, and remove VisualiserBoats for any participating sourceIDs that have been removed.
     *
     * @param existingBoats The visualiser boats that already exist in the race. This will be populated when we receive a new race.xml or boats.xml.
     * @param boats The map of {@link Boat}s describing boats that are potentially in the race. Maps boat sourceID to boat.
     * @param sourceIDs The list of boat sourceIDs describing which specific boats are actually participating.
     * @param colors The list of unassignedColors to be used for the boats.
     */
    private void generateVisualiserBoats(ObservableList<VisualiserBoat> existingBoats, Map<Integer, Boat> boats, List<Integer> sourceIDs, List<Color> colors) {

        //Remove any VisualiserBoats that are no longer participating.
        for (VisualiserBoat boat : new ArrayList<>(existingBoats)) {

            //Boat no longer is participating.
            if (!sourceIDs.contains(boat.getSourceID())) {
                //Return their colors to the color list.
                colors.add(boat.getColor());

                //Remove boat.
                existingBoats.remove(boat);
            }

        }

        //Get source IDs of already existing boats.
        List<Integer> existingBoatIDs = new ArrayList<>();
        for (VisualiserBoat boat : existingBoats) {
            existingBoatIDs.add(boat.getSourceID());
        }

        //Get source IDs of only newly participating boats.
        List<Integer> newBoatIDs = new ArrayList<>(sourceIDs);
        newBoatIDs.removeAll(existingBoatIDs);

        //Create VisualiserBoat for newly participating boats.
        for (Integer sourceID : newBoatIDs) {

            if (boats.containsKey(sourceID)) {

                VisualiserBoat boat = new VisualiserBoat(
                        boats.get(sourceID),
                        colors.remove(colors.size() - 1));//TODO potential bug: not enough colors for boats.

                boat.setCurrentLeg(getLegs().get(0));

                existingBoats.add(boat);

            }

        }

        setPlayerBoat();


    }


    /**
     * Sets the boat the player has been assigned to as belonging to them.
     */
    private void setPlayerBoat() {

        if (getPlayerBoatID() != 0) {

            for (VisualiserBoat boat : new ArrayList<>(getBoats())) {

                if (boat.getSourceID() == getPlayerBoatID()) {
                    boat.setClientBoat(true);
                    ThisBoat.getInstance().setBoat(boat);
                }

            }

        }

    }


    /**
     * Initialise the boats in the race.
     * This sets their current leg.
     */
    @Override
    protected void initialiseBoats() {

        Leg startingLeg = getLegs().get(0);

        for (VisualiserBoat boat : boats) {
            boat.setCurrentLeg(startingLeg);
        }

    }




    /**
     * Update position of boats in race (e.g, 5th), no position if on starting leg or DNF.
     * @param boats The list of boats to update.
     */
    public void updateBoatPositions(List<VisualiserBoat> boats) {

        //Sort boats.
        sortBoatsByPosition(boats);

        //Assign new positions.
        for (int i = 0; i < boats.size(); i++) {
            VisualiserBoat boat = boats.get(i);


            if ((boat.getStatus() == BoatStatusEnum.DNF) || (boat.getStatus() == BoatStatusEnum.PRESTART) || (boat.getCurrentLeg().getLegNumber() < 0)) {
                boat.setPlacing("-");

            } else {
                boat.setPlacing(Integer.toString(i + 1));
            }
        }

    }


    /**
     * Sorts the list of boats by their position within the race.
     * @param boats The list of boats in the race.
     */
    private void sortBoatsByPosition(List<VisualiserBoat> boats) {

        boats.sort((a, b) -> {
            //Get the difference in leg numbers.
            int legNumberDelta = b.getCurrentLeg().getLegNumber() - a.getCurrentLeg().getLegNumber();

            //If they're on the same leg, we need to compare time to finish leg.
            if (legNumberDelta == 0) {

                //These are potentially null until we receive our first RaceStatus containing BoatStatuses.
                if ((a.getEstimatedTimeAtNextMark() != null) && (b.getEstimatedTimeAtNextMark() != null)) {

                    return (int) Duration.between(
                            b.getEstimatedTimeAtNextMark(),
                            a.getEstimatedTimeAtNextMark()  ).toMillis();

                }

            }

            return legNumberDelta;

        });

    }




    /**
     * Returns the boats participating in the race.
     * @return List of boats participating in the race.
     */
    public ObservableList<VisualiserBoat> getBoats() {
        return boats;
    }


    /**
     * Returns a boat by sourceID.
     * @param sourceID The source ID the boat.
     * @return The boat.
     * @throws BoatNotFoundException Thrown if there is no boat with the specified sourceID.
     */
    public VisualiserBoat getBoat(int sourceID) throws BoatNotFoundException {

        for (VisualiserBoat boat : boats) {

            if (boat.getSourceID() == sourceID) {
                return boat;
            }

        }

        throw new BoatNotFoundException("Boat with sourceID: " + sourceID + " was not found.");
    }

    /**
     * Returns whether or not there exists a {@link VisualiserBoat} with the given source ID.
     * @param sourceID SourceID of VisualiserBoat.
     * @return True if VisualiserBoat exists, false otherwise.
     */
    public boolean isVisualiserBoat(int sourceID) {

        try {
            getBoat(sourceID);
            return true;
        } catch (BoatNotFoundException e) {
            return false;
        }
    }


    /**
     * Returns a mark by sourceID.
     * @param sourceID The source ID the mark.
     * @return The mark.
     * @throws MarkNotFoundException Thrown if there is no mark with the specified sourceID.
     */
    public Mark getMark(int sourceID) throws MarkNotFoundException {

        for (Mark mark : getMarks()) {

            if (mark.getSourceID() == sourceID) {
                return mark;
            }

        }

        throw new MarkNotFoundException("Mark with sourceID: " + sourceID + " was not found.");
    }


    /**
     * Returns whether or not there exists a {@link Mark} with the given source ID.
     * @param sourceID SourceID of mark.
     * @return True if mark exists, false otherwise.
     */
    public boolean isMark(int sourceID) {

        try {
            getMark(sourceID);
            return true;
        } catch (MarkNotFoundException e) {
            return false;
        }
    }




    /**
     * Returns the order in which boats completed each leg. Maps the leg to a list of boats, ordered by the order in which they finished the leg.
     * @return Leg completion order for each leg.
     */
    public Map<Leg, List<VisualiserBoat>> getLegCompletionOrder() {
        return legCompletionOrder;
    }


    /**
     * Gets the source ID of the player's boat. 0 if not assigned.
     * @return Players boat source ID.
     */
    public int getPlayerBoatID() {
        return playerBoatID;
    }

    /**
     * sets the source ID of the player's boat. 0 if not assigned.
     * @param playerBoatID Players boat source ID.
     */
    public void setPlayerBoatID(int playerBoatID) {
        this.playerBoatID = playerBoatID;
        setPlayerBoat();
    }


}
