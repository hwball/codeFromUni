package model;

/**
 * Used to create a key made of an ip and port.
 */
public class TableKey {

    private final String ip;
    private final int port;

    public TableKey(String ip, int port) {
        this.ip = ip;
        this.port = port;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof TableKey)) return false;
        TableKey key = (TableKey) o;
        return ip.equals(key.ip) && port == key.port;
    }

    @Override
    public int hashCode() {
        int result = port;
        result = 31 * result + ip.hashCode();
        return result;
    }

    @Override
    public String toString() {
        return  "[ip='" + ip + '\'' +
                ", port=" + port +
                ']';
    }
}
