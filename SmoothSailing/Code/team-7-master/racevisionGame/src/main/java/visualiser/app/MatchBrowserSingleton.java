package visualiser.app;

import visualiser.network.MatchBrowserInterface;

import java.io.IOException;
import java.net.DatagramSocket;

public class MatchBrowserSingleton {
    private DatagramSocket udpSocket;
    private MatchBrowserInterface matchBrowserInterface;

    private static MatchBrowserSingleton instance = null;

    public MatchBrowserSingleton() {
        this.matchBrowserInterface = new MatchBrowserInterface();
        try{
            this.udpSocket = matchBrowserInterface.setupMatchBrowserConnection();
        }catch (IOException e){
            System.err.println("Error in setting up connection with match browser");
        }
    }

    public static MatchBrowserSingleton getInstance() {
        if (instance == null){
            instance = new MatchBrowserSingleton();
        }
        return instance;
    }

    public DatagramSocket getUdpSocket() {
        return udpSocket;
    }

    public MatchBrowserInterface getMatchBrowserInterface() {
        return matchBrowserInterface;
    }
}
