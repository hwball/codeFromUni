package visualiser.model;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.Socket;

/**
 * Created by David on 26/07/2017.
 */
public class Ping implements Runnable {

    private final String hostname;
    private final int port;
    private final RaceConnection rc;

    public Ping(String hostname, int port, RaceConnection rc){
        this.hostname = hostname;
        this.port = port;
        this.rc = rc;
    }

    public boolean pingPort() {
        InetSocketAddress i = new InetSocketAddress(hostname, port);
        try (Socket s = new Socket()){
            s.connect(i, 1500);
            s.shutdownInput();
            s.shutdownOutput();
            s.close();
            rc.statusProperty().set("Ready");
            return true;
        } catch (IOException e) {
            rc.statusProperty().set("Offline");
        }
        return false;
    }

    @Override
    public void run() {
        pingPort();
    }
}
