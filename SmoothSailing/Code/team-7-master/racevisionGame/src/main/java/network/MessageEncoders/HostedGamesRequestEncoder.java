package network.MessageEncoders;

import network.Exceptions.InvalidMessageException;
import network.Messages.AC35Data;
import network.Messages.HostGame;
import network.Messages.HostGamesRequest;

import java.nio.ByteBuffer;

import static network.Utils.ByteConverter.intToBytes;

public class HostedGamesRequestEncoder implements MessageEncoder{

    /**
     * Constructor
     */
    public HostedGamesRequestEncoder() {
    }

    @Override
    public byte[] encode(AC35Data message) throws InvalidMessageException {
        try{
            //Downcast
            HostGamesRequest hostGamesRequest = (HostGamesRequest) message;

            int numGames = hostGamesRequest.getKnownGames().size();

            ByteBuffer hostedGamesRequestMessage = ByteBuffer.allocate(4+13*numGames);

            hostedGamesRequestMessage.put(intToBytes(numGames));

            HostGameMessageEncoder lineEncoder = new HostGameMessageEncoder();
            for (HostGame line: hostGamesRequest.getKnownGames()) {
                hostedGamesRequestMessage.put(lineEncoder.encode(line));
            }

            return hostedGamesRequestMessage.array();

        }catch(Exception e){
            throw new InvalidMessageException("Could not encode Host game message.", e);
        }
    }
}
