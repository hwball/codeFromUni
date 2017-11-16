package network.MessageDecoders;

import network.MessageEncoders.HostGameMessageEncoder;
import network.Messages.AC35Data;
import network.Messages.Enums.RaceStatusEnum;
import network.Messages.HostGame;
import org.junit.Assert;
import org.junit.Test;

public class HostGameMessageDecoderTest {

    @Test
    public void hostGameMessageDecoderTest() throws Exception {
        HostGame testHost = new HostGame("127.0.0.1", 3779, (byte) 1, (byte) 2, RaceStatusEnum.PRESTART, (byte) 6, (byte) 2);


        HostGameMessageEncoder encoder = new HostGameMessageEncoder();

        byte[] encodedMessage = encoder.encode(testHost);

        HostGameMessageDecoder decoder = new HostGameMessageDecoder();

        HostGame decodedTest = (HostGame) decoder.decode(encodedMessage);

        compareHostGameMessage(testHost, decodedTest);
    }

    public static void compareHostGameMessage(HostGame original, HostGame decoded) {
        Assert.assertEquals(original.getIp(), decoded.getIp());
        Assert.assertEquals(original.getPort(), decoded.getPort());
        Assert.assertEquals(original.getSpeed(), decoded.getSpeed());
        Assert.assertEquals(original.getStatus(), decoded.getStatus());
        Assert.assertEquals(original.getRequiredNumPlayers(), decoded.getRequiredNumPlayers());
        Assert.assertEquals(original.getCurrentNumPlayers(), decoded.getCurrentNumPlayers());
    }
}
