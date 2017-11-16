package network.MessageDecoders;

import network.Exceptions.InvalidMessageException;
import network.Messages.AC35Data;
import network.Messages.CourseWinds;
import network.Messages.Enums.RaceStatusEnum;
import network.Messages.HostGame;
import network.Messages.RaceStatus;

import java.util.Arrays;

import static network.Utils.ByteConverter.bytesToInt;
import static network.Utils.ByteConverter.bytesToLong;

public class HostGameMessageDecoder implements MessageDecoder {

    /**
     * The encoded message.
     */
    private byte[] encodedMessage;

    /**
     * The decoded message.
     */
    private HostGame message;

    /**
     * Constructor
     */
    public HostGameMessageDecoder() {
    }

    @Override
    public AC35Data decode(byte[] encodedMessage) throws InvalidMessageException {
        this.encodedMessage = encodedMessage;

        try{
            byte ipPart1 = encodedMessage[0];
            byte ipPart2 = encodedMessage[1];
            byte ipPart3 = encodedMessage[2];
            byte ipPart4 = encodedMessage[3];
            String ipString = bytesToLong(ipPart1) + "." + bytesToLong(ipPart2) + "." + bytesToLong(ipPart3) + "." + bytesToLong(ipPart4);
//            System.out.println(ipString);
            int port = bytesToInt(Arrays.copyOfRange(encodedMessage, 4, 8));
            byte map = encodedMessage[8];
            byte speed = encodedMessage[9];
            byte status = encodedMessage[10];
            byte requiredNumPlayers = encodedMessage[11];
            byte currentNumPlayers = encodedMessage[12];


            message = new HostGame(ipString, port, map,
                    speed, RaceStatusEnum.fromByte(status),
                    requiredNumPlayers, currentNumPlayers);

            return message;

        } catch (Exception e) {
            throw new InvalidMessageException("Could not decode Host game message.", e);
        }
    }


}
