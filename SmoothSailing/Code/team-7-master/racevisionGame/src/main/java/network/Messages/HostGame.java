package network.Messages;


import network.Messages.Enums.MessageType;
import network.Messages.Enums.RaceStatusEnum;
import network.Utils.ByteConverter;

import java.nio.ByteBuffer;

import static network.Utils.ByteConverter.intToBytes;

public class HostGame extends AC35Data {

    private String ip;
    private int port;
    private byte map;
    private byte speed;
    private RaceStatusEnum status;
    private byte requiredNumPlayers;
    private byte currentNumPlayers;

    public HostGame(String ip, int port, byte map, byte speed,
                    RaceStatusEnum status, byte requiredNumPlayers,
                    byte currentNumPlayers) {
        super(MessageType.HOST_GAME);
        this.ip = ip;
        this.port = port;
        this.map = map;
        this.speed = speed;
        this.status = status;
        this.requiredNumPlayers = requiredNumPlayers;
        this.currentNumPlayers = currentNumPlayers;

    }

    /**
     * @return the ip of host
     */
    public String getIp() {
        return ip;
    }

    /**
     * @return the port of host
     */
    public int getPort() {
        return port;
    }

    /**
     * @return the map index
     */
    public byte getMap() {
        return map;
    }

    /**
     * @return the speed value of game
     */
    public byte getSpeed() {
        return speed;
    }

    /**
     * @return the status of race
     */
    public RaceStatusEnum getStatus() {
        return status;
    }

    /**
     * @return required number of players
     */
    public byte getRequiredNumPlayers() {
        return requiredNumPlayers;
    }

    /**
     * @return current number of players
     */
    public byte getCurrentNumPlayers() {
        return currentNumPlayers;
    }

    public void setIp(String ip) {
        this.ip = ip;
    }
}

