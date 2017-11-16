package network.Messages;

import network.Messages.Enums.MessageType;

import java.util.List;

public class HostGamesRequest extends AC35Data{

    private List<HostGame> knownGames;

    /**
     * Constructor
     * @param knownGames games known by sender
     */
    public HostGamesRequest(List knownGames) {
        super(MessageType.HOSTED_GAMES_REQUEST);
        this.knownGames = knownGames;
    }

    public List<HostGame> getKnownGames() {
        return knownGames;
    }
}
