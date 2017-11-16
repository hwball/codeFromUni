package visualiser.network;

import network.Messages.AC35Data;
import shared.model.RunnableWithFramePeriod;

import java.io.IOException;
import java.net.DatagramSocket;

public class MatchBrowserClientRunnable implements RunnableWithFramePeriod {
    private MatchBrowserLobbyInterface matchBrowserLobbyInterface;
    private DatagramSocket socket;

    public MatchBrowserClientRunnable(MatchBrowserLobbyInterface matchBrowserInterface, DatagramSocket socket) {
        this.matchBrowserLobbyInterface = matchBrowserInterface;
        this.socket = socket;
    }

    @Override
    public void run(){
        long previousFrameTime = System.currentTimeMillis();

        while (!Thread.interrupted()) {
            try{
                matchBrowserLobbyInterface.receiveGameInfo(socket);
            }catch (IOException e){
                System.err.println("HostGameMessage could not be received");
            }

            long currentFrameTime = System.currentTimeMillis();
            waitForFramePeriod(previousFrameTime, currentFrameTime, 10000);
            previousFrameTime = currentFrameTime;
        }
    }
}
