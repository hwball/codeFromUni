package visualiser.network;

import network.Exceptions.InvalidMessageException;
import network.MessageDecoders.HostedGamesRequestDecoder;
import network.Messages.HostGame;
import network.Messages.HostGamesRequest;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.util.ArrayList;
import java.util.List;
import java.util.Observable;

/**
 * Used to receive lobby information from server
 */
public class MatchBrowserLobbyInterface extends Observable {
    private DatagramSocket socket;
    private MatchBrowserClientRunnable clientRunnable;
    private List<HostGame> games = new ArrayList<>();
    private Thread clientRunnableThread;

    public MatchBrowserLobbyInterface() {

    }

    /**
     * start receiving game info
     * @param socket to receive from
     */
    public void startReceivingHostData(DatagramSocket socket) {
        this.socket = socket;
        clientRunnable = new MatchBrowserClientRunnable(this, socket);
        clientRunnableThread = new Thread(clientRunnable, "Socket: " + socket.toString());
        clientRunnableThread.start();
    }

    /**
     * Used by client to received race information from the server
     * @param socket socket to read from
     * @throws IOException socket error
     */
    protected void receiveGameInfo(DatagramSocket socket) throws IOException {
        byte[] data = new byte[64];
        DatagramPacket receivedPacket = new DatagramPacket(data, 64);
        socket.receive(receivedPacket);

        HostedGamesRequestDecoder hostedGamesRequestDecoder = new HostedGamesRequestDecoder();
        try {
            HostGamesRequest message = (HostGamesRequest) hostedGamesRequestDecoder.decode(data);
            games = message.getKnownGames();
//            System.out.println(games.get(0).getIp());
            setChanged();
            notifyObservers();
        } catch (InvalidMessageException e) {
            System.err.println("HostedGamesRequestMessage could not be decoded");
        }
    }

    /**
     * Gets the host games
     * @return games to be returned in list
     */
    public List<HostGame> getGames() {
        return games;
    }

    /**
     * Used to close the socket and runnable once out of the lobby
     */
    public void closeSocket() {
        clientRunnableThread.interrupt();
        this.socket.close();
    }
}
