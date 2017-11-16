package model;

public class ClientAddress {
    private String ip;
    private int port;

    public ClientAddress(String ip, int port) {
        this.ip = ip;
        this.port = port;
    }

    public String getIp() {
        return ip;
    }

    public int getPort() {
        return port;
    }

    @Override
    public boolean equals(Object o) {
        return o != null && o instanceof ClientAddress && hashCode() == o.hashCode();
    }

    @Override
    public int hashCode() {
        int result = ip != null ? ip.hashCode() : 0;
        result *= 31;
        return result;
    }

    @Override
    public String toString() {
        return  "{ip='" + ip + '\'' +
                ", port=" + port+"}";
    }
}
