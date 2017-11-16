package mock.model;

import javafx.animation.AnimationTimer;
import mock.model.collider.Collision;
import mock.model.commandFactory.CollisionCommand;
import mock.model.commandFactory.Command;
import mock.model.commandFactory.CompositeCommand;
import mock.model.commandFactory.CommandFactory;
import network.Messages.Enums.BoatActionEnum;
import network.Messages.Enums.BoatStatusEnum;
import network.Messages.Enums.RaceStatusEnum;
import network.Messages.LatestMessages;
import shared.model.RunnableWithFramePeriod;

import java.util.ArrayList;
import java.util.Observable;
import java.util.Observer;


public class RaceLogic implements RunnableWithFramePeriod, Observer {
    /**
     * State of current race modified by this object
     */
    private MockRace race;
    /**
     * High-level interface to AC35 protocol server
     */
    private RaceServer server;

    private CompositeCommand commands;

    private boolean loopBool = true;

    /**
     * Initialises race loop with state and server message queue
     * @param race state of race to modify
     * @param messages to send to server
     * @param compositeCommand Commands from clients to execute.
     */
    public RaceLogic(MockRace race, LatestMessages messages, CompositeCommand compositeCommand) {
        this.race = race;
        this.server = new RaceServer(race, messages);
        this.commands = compositeCommand;

        race.getColliderRegistry().addObserver(this);
    }

    /**
     * Initialise boats and start countdown timer
     */
    @Override
    public void run() {

        prestartCountdown();

        race.initialiseBoats();

        countdown();

        raceLoop();
    }

    public void boolFalse(){
        loopBool = false;
    }


    /**
     * The countdown timer until the prestart period is finished. This timer will stop 3 minutes before the race starts, and players can no longer start participating.
     */
    private void prestartCountdown() {

        long previousFrameTime = System.currentTimeMillis();

        while (((race.getRaceStatusEnum() == RaceStatusEnum.PRESTART)
                || (race.getRaceStatusEnum() == RaceStatusEnum.NOT_ACTIVE)
                || (race.getRaceStatusEnum() == RaceStatusEnum.WARNING)) && loopBool) {

            long currentTime = System.currentTimeMillis();

            //Update race time.
            race.updateRaceTime(currentTime);

            race.delayRaceStart();

            //Update the race status based on the current time.
            race.updateRaceStatusEnum();

            //Provide boat's with an estimated time at next mark until the race starts.
            race.setBoatsTimeNextMark(race.getRaceClock().getCurrentTime());

            //Parse the race snapshot.
            server.parseSnapshot();


            waitForFramePeriod(previousFrameTime, currentTime, 16);
            previousFrameTime = currentTime;
        }
    }

    /**
     * Countdown timer until race starts.
     */
    private void countdown() {

        long previousFrameTime = System.currentTimeMillis();

        while (race.getRaceStatusEnum() != RaceStatusEnum.STARTED && loopBool) {

            long currentTime = System.currentTimeMillis();

            //Update race time.
            race.updateRaceTime(currentTime);

            //Update the race status based on the current time.
            race.updateRaceStatusEnum();

            //Provide boat's with an estimated time at next mark until the race starts.
            race.setBoatsTimeNextMark(race.getRaceClock().getCurrentTime());

            //Parse the race snapshot.
            server.parseSnapshot();


            // Change wind direction
            race.changeWindDirection();


            if (race.getRaceStatusEnum() == RaceStatusEnum.STARTED) {
                race.setBoatsStatusToRacing();
            }

            waitForFramePeriod(previousFrameTime, currentTime, 16);
            previousFrameTime = currentTime;

        }
    }


    /**
     * Timer that runs for the duration of the race, until all boats finish.
     */
    private void raceLoop() {

        ArrayList<MockBoat> collisionBoats = new ArrayList<>();

        long previousFrameTime = System.currentTimeMillis();

        while (race.getRaceStatusEnum() != RaceStatusEnum.FINISHED && loopBool) {

            //Get the current time.
            long currentTime = System.currentTimeMillis();

            // Execute commands from clients.
            commands.execute();

            // Notify Observers
            race.setChanged();
            race.notifyObservers();

            //Update race time.
            race.updateRaceTime(currentTime);

            //As long as there is at least one boat racing, we still simulate the race.
            if (race.getNumberOfActiveBoats() != 0) {
                //System.out.println(race.getNumberOfActiveBoats());
                //Get the time period of this frame.
                long framePeriod = currentTime - previousFrameTime;

                //For each boat, we update its position, and generate a BoatLocationMessage.
                for (MockBoat boat : race.getBoats()) {

                    //If it is still racing, update its position.
                    if (boat.getStatus() == BoatStatusEnum.RACING) {
                        race.updatePosition(boat, framePeriod, race.getRaceClock().getDurationMilli());

                        if(race.getColliderRegistry().rayCast(boat)){
                            //Add boat to list
                            collisionBoats.add(boat);
                        }
                    }


                }

            } else {
                //Otherwise, the race is over!
                raceFinished.start();
                race.setRaceStatusEnum(RaceStatusEnum.FINISHED);
            }

            if (race.getNumberOfActiveBoats() != 0) {
                // Change wind direction
                race.changeWindDirection();

                //Pass collision boats in
                server.parseBoatCollisions(collisionBoats);

                //Parse the race snapshot.
                server.parseSnapshot();

                collisionBoats.clear();

                //Update the last frame time.
                previousFrameTime = currentTime;
            }

            waitForFramePeriod(previousFrameTime, currentTime, 16);
            previousFrameTime = currentTime;
        }
    }

    /**
     * Broadcast that the race has finished.
     */
    protected AnimationTimer raceFinished = new AnimationTimer(){
        int iters = 0;
        @Override
        public void handle(long now) {

            server.parseSnapshot();

            if (iters > 500) {
                stop();
            }
            iters++;
        }
    };


    /**
     * Returns the race state that this RaceLogic is simulating.
     * @return Race state this RaceLogic is simulating.
     */
    public MockRace getRace() {
        return race;
    }

    @Override
    public void update(Observable o, Object arg) {
        if(arg instanceof Collision) {
            Collision collision = (Collision)arg;
            commands.addCommand(new CollisionCommand(race, (MockBoat)collision.getBoat()));
        }
    }
}
