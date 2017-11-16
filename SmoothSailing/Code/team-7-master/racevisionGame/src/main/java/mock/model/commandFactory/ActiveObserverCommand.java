package mock.model.commandFactory;

import java.util.Observable;

/**
 * Used to track the current active observer command. This is to ensure two commands that do similar things do not overlap.
 */
public class ActiveObserverCommand {
    private ObserverCommand currentVelocityCommand;
    private ObserverCommand currentAngularCommand;

    public ActiveObserverCommand() {

    }

    public void changeVelocityCommand(Observable o, ObserverCommand c) {
        o.deleteObserver(currentVelocityCommand);
        o.addObserver(c);
        currentVelocityCommand = c;
    }

    public void changeAngularCommand(Observable o, ObserverCommand c) {
        o.deleteObserver(currentAngularCommand);
        o.addObserver(c);
        currentAngularCommand = c;
    }
}
