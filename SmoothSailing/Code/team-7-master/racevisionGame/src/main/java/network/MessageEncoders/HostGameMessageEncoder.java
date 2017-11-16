package network.MessageEncoders;

import network.Exceptions.InvalidMessageException;
import network.Messages.AC35Data;
import network.Messages.HostGame;

import java.nio.ByteBuffer;

import static network.Utils.ByteConverter.intToBytes;


public class HostGameMessageEncoder implements MessageEncoder{

    /**
     * Constructor
     */
    public HostGameMessageEncoder() {
    }

    @Override
    public byte[] encode(AC35Data message) throws InvalidMessageException {
        try{
            //Downcast
            HostGame hostGame = (HostGame) message;

            ByteBuffer hostGameMessage = ByteBuffer.allocate(13);

            ByteBuffer ipBytes = ByteBuffer.allocate(4);
            String ip = hostGame.getIp();
            String[] ipValues = ip.split("\\.");
            for(String value:ipValues){
                ipBytes.put(intToBytes(Integer.parseInt(value), 1)[0]);
            }
            byte raceStatus = hostGame.getStatus().getValue();

            hostGameMessage.put(ipBytes.array());
            hostGameMessage.put(intToBytes(hostGame.getPort()));
            hostGameMessage.put(hostGame.getMap());
            hostGameMessage.put(hostGame.getSpeed());
            hostGameMessage.put(raceStatus);
            hostGameMessage.put(hostGame.getRequiredNumPlayers());
            hostGameMessage.put(hostGame.getCurrentNumPlayers());


//            System.out.println(hostGameMessage.array()[4]);
            return hostGameMessage.array();

        } catch (Exception e) {
            throw new InvalidMessageException("Could not encode Host game message.", e);
        }
    }
}
