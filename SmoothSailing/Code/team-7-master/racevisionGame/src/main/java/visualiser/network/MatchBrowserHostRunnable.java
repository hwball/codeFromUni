package visualiser.network;

import network.Exceptions.InvalidMessageException;
import network.Messages.AC35Data;
import network.Messages.HostGame;
import shared.model.RunnableWithFramePeriod;

import java.io.IOException;
import java.net.DatagramSocket;


public class MatchBrowserHostRunnable implements RunnableWithFramePeriod {

    private MatchBrowserInterface matchBrowserInterface;
    private DatagramSocket socket;
    private AC35Data gameInfo;

    public MatchBrowserHostRunnable(MatchBrowserInterface matchBrowserInterface, DatagramSocket socket, AC35Data gameInfo) {
        this.matchBrowserInterface = matchBrowserInterface;
        this.socket = socket;
        this.gameInfo = gameInfo;
    }

    @Override
    public void run(){
        long previousFrameTime = System.currentTimeMillis();

        while (!Thread.interrupted()) {
            try{
                matchBrowserInterface.sendOutGameInfo(gameInfo, socket);
            }catch (IOException e){
                System.err.println("HostGameMessage could not be sent");
            }

            long currentFrameTime = System.currentTimeMillis();
            waitForFramePeriod(previousFrameTime, currentFrameTime, 10000);
            previousFrameTime = currentFrameTime;
        }
    }
}
