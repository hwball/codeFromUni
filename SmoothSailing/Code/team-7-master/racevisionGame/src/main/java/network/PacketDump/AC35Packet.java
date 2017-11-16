package network.PacketDump;

/**
 * Created by fwy13 on 25/04/17.
 */
public class AC35Packet {

    byte[] data;

    public AC35Packet(byte[] data){
        this.data = data;
    }

    public byte[] getData() {
        return data;
    }
}
