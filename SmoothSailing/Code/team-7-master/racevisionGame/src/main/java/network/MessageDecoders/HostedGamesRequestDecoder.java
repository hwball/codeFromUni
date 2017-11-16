package network.MessageDecoders;

import network.Exceptions.InvalidMessageException;
import network.Messages.AC35Data;
import network.Messages.HostGame;
import network.Messages.HostGamesRequest;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static network.Utils.ByteConverter.bytesToInt;

public class HostedGamesRequestDecoder  implements MessageDecoder{
    @Override
    public AC35Data decode(byte[] encodedMessage) throws InvalidMessageException {
        try{
            int numberOfGames = bytesToInt(Arrays.copyOfRange(encodedMessage, 0, 4));

            HostGameMessageDecoder lineDecoder = new HostGameMessageDecoder();
            List<HostGame> knownGames = new ArrayList<>();
            int byteIndex = 4;
            for (int i = 0; i < numberOfGames; i++){
                knownGames.add((HostGame) lineDecoder.decode(Arrays.copyOfRange(encodedMessage, byteIndex, byteIndex+13)));
                byteIndex += 13;
            }

            return new HostGamesRequest(knownGames);

        } catch (Exception e) {
            e.printStackTrace();
            throw new InvalidMessageException("Could not decode Host game message.", e);
        }
    }
}
