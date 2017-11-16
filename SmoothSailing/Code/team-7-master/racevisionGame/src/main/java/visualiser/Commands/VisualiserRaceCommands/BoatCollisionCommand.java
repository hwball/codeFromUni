package visualiser.Commands.VisualiserRaceCommands;

import javafx.scene.media.AudioClip;
import mock.model.commandFactory.Command;
import network.Messages.YachtEvent;
import shared.exceptions.BoatNotFoundException;
import visualiser.model.VisualiserBoat;
import visualiser.model.VisualiserRaceState;

/**
 * Created by zwu18 on 4/09/17.
 */
public class BoatCollisionCommand implements Command {

    YachtEvent yachtEvent;

    VisualiserRaceState visualiserRace;

    public BoatCollisionCommand(YachtEvent yachtEvent, VisualiserRaceState visualiserRace){
        this.yachtEvent = yachtEvent;
        this.visualiserRace = visualiserRace;

    }

    @Override
    public void execute() {

        if(visualiserRace.getPlayerBoatID()==yachtEvent.getSourceID()){
            //System.out.println("I crashed!");
            AudioClip sound = new AudioClip(this.getClass().getResource("/visualiser/sounds/collision.wav").toExternalForm());
            sound.play();

        } else {
            //System.out.println("Someone else crashed!");
            AudioClip sound = new AudioClip(this.getClass().getResource("/visualiser/sounds/quietcollision.wav").toExternalForm());
            sound.play();
        }

        try {
            VisualiserBoat boat = visualiserRace.getBoat(yachtEvent.getSourceID());
            boat.setHasCollided(true);
        } catch (BoatNotFoundException e) {
            e.printStackTrace();
        }
    }
}
