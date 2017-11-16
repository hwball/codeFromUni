package network.MessageDecoders;


import network.MessageEncoders.HostGameMessageEncoder;
import network.MessageEncoders.HostedGamesRequestEncoder;
import network.Messages.Enums.RaceStatusEnum;
import network.Messages.HostGame;
import network.Messages.HostGamesRequest;
import org.junit.Assert;
import org.junit.Test;

import java.util.Arrays;
import java.util.List;

public class HostedGamesRequestDecoderTest {
    @Test
    public void hostGamesRequestMessageDecoderTest() throws Exception {
        HostGame testHostGame1 = new HostGame("127.0.0.1", 3779, (byte) 1, (byte) 2, RaceStatusEnum.PRESTART, (byte) 6, (byte) 2);
        HostGame testHostGame2 = new HostGame("127.0.0.1", 3780, (byte) 1, (byte) 2, RaceStatusEnum.PRESTART, (byte) 6, (byte) 2);

        List knownGames = Arrays.asList(testHostGame1, testHostGame2);

        HostedGamesRequestEncoder encoder = new HostedGamesRequestEncoder();

        byte[] encodedMessage = encoder.encode(new HostGamesRequest(knownGames));

        HostedGamesRequestDecoder decoder = new HostedGamesRequestDecoder();

        HostGamesRequest decodedTest = (HostGamesRequest) decoder.decode(encodedMessage);

        compareHostGamesRequestMessage(knownGames, decodedTest.getKnownGames());
    }

    public static void compareHostGamesRequestMessage(List<HostGame> original, List<HostGame> decoded) {
        Assert.assertEquals(original.get(0).getIp(), decoded.get(0).getIp());
        Assert.assertEquals(original.get(1).getPort(), decoded.get(1).getPort());
    }
}
