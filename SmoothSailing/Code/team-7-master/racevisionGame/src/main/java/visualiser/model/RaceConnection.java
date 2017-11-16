package visualiser.model;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

/**
 * Connection for Races
 */
public class RaceConnection {
    private final StringProperty hostname;
    private final int port;
    private final StringProperty status;
    private final StringProperty gamename;


    /**
     * Constructor for remote host connections.
     * @param hostname URL for remote host
     * @param port port for game feed
     * @param gamename Name of the game
     */
    public RaceConnection(String hostname, int port, String gamename) {
        this.hostname = new SimpleStringProperty(hostname);
        this.port = port;
        this.status = new SimpleStringProperty("");
        check();
        this.gamename = new SimpleStringProperty(gamename);
    }

    /**
     * Tries to create a socket to hostname and port, indicates status after test.
     * @return true if socket can connect
     */
    public boolean check() {
        Ping ping = new Ping(hostname.get(), port, this);
        new Thread(ping).start();
        return true;
    }

    public String getHostname() {
        return hostname.get();
    }

    public StringProperty hostnameProperty() {
        return hostname;
    }

    public int getPort() {
        return port;
    }

    public StringProperty statusProperty() {
        return status;
    }

    public StringProperty gamenameProperty() { return gamename;}
}
