package mock.model.commandFactory;

import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedDeque;

/**
 * Wraps multiple commands into a composite to execute queued commands during a frame.
 */
public class CompositeCommand implements Command {
    private Queue<Command> commands;

    public CompositeCommand() {
        this.commands = new ConcurrentLinkedDeque<>();
    }

    public void addCommand(Command command) {
        commands.offer(command);
    }

    @Override
    public void execute() {
        while(commands.peek() != null) commands.poll().execute();
    }
}
