package network.MessageEncoders;


import network.BinaryMessageEncoder;
import network.Exceptions.InvalidMessageException;
import network.Exceptions.InvalidMessageTypeException;
import network.Messages.*;
import network.Messages.Enums.MessageType;

import java.nio.ByteBuffer;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

import static network.Utils.ByteConverter.intToBytes;
import static network.Utils.ByteConverter.longToBytes;



/**
 * Created by fwy13 on 19/04/17.
 */
public class RaceVisionByteEncoder {




    public static byte[] displayTextMessage(RaceMessage[] message){
        //ByteBuffer result = ByteBuffer.allocate(4 + numLines * 32);
        int messageVersionNumber = 0b1;//version number
        short ackNum = 0;//no clue what this does just a placeholder for 2 bytes.
        byte[] messLines = intToBytes(message.length, 1);

//        result.putInt(messageVersionNumber);
//        result.putShort(ackNum);
//        result.put(messLines);

        ArrayList<byte[]> messages = new ArrayList<byte[]>();
        int size = 4;

        for (int i = 0; i < message.length; i ++){
            int messageLen = message[i].getMessageText().getBytes().length;
            byte[] messageAsBytes = message[i].getMessageText().getBytes();
            if (messageLen < 30){
                messageLen = 30;
            }
            ByteBuffer mess = ByteBuffer.allocate(2 + messageLen);
            mess.put(intToBytes(message[i].getLineNumber(), 1));
            mess.put(intToBytes(messageLen, 1));
            for (int j = 0; j < messageLen; j ++){
                mess.put(messageAsBytes[j]);
            }
            messages.add(mess.array());
            size += 2 + messageLen;
        }

        ByteBuffer result = ByteBuffer.allocate(size);
        result.put(intToBytes(messageVersionNumber, 1));
        result.putShort(ackNum);
        result.put(messLines);

        for(byte[] mess: messages){
            result.put(mess);
        }

        return result.array();
    }

    public static byte[] yachtEventCode(long time, short acknowledgeNumber, int raceID, int destSourceID, int incidentID,
                                 int eventID){
        int messageVersion = 0b10;
        byte[] encodeTime = longToBytes(time, 6);
        short ackNum = acknowledgeNumber;
        int raceUID = raceID;//TODO chekc if this is an into for a 4 char string.
        int destSource = destSourceID;
        int incident = incidentID;
        byte[] event = intToBytes(eventID, 1);

        ByteBuffer result = ByteBuffer.allocate(22);
        result.put(intToBytes(messageVersion, 1));
        result.put(encodeTime);
        result.putShort(ackNum);
        result.put(intToBytes(raceUID));
        result.put(intToBytes(destSource));
        result.put(intToBytes(incident));
        result.put(event);
        return result.array();
    }

    public static byte[] chatterText(int messageType, String message){
        int messageVersion = 0b1;
        byte[] type = intToBytes(messageType, 1);
        byte[] text = message.getBytes();
        byte[] length = intToBytes(text.length, 1);

        ByteBuffer result = ByteBuffer.allocate(3 + text.length);
        result.put(intToBytes(messageVersion, 1));
        result.put(type);
        result.put(length);
        result.put(text);

        return result.array();
    }




    /**
     * Encodes a given message, to be placed inside a binary message (see {@link BinaryMessageEncoder}).
     * @param message Message to encode.
     * @return Encoded message.
     * @throws InvalidMessageException If the message cannot be encoded.
     */
    public static byte[] encode(AC35Data message) throws InvalidMessageException {

        MessageEncoder encoder = null;
        try {
            encoder = EncoderFactory.create(message.getType());

        } catch (InvalidMessageTypeException e) {
            throw new InvalidMessageException("Could not create encoder for MessageType: " + message.getType(), e);

        }

        byte[] encodedMessage = encoder.encode(message);

        return encodedMessage;
    }


    /**
     * Encodes a given messages, using a given ackNumber, and returns a binary message ready to be sent over-the-wire.
     * @param message The message to send.
     * @param ackNumber The ackNumber of the message.
     * @return A binary message ready to be transmitted.
     * @throws InvalidMessageException Thrown if the message cannot be encoded.
     */
    public static byte[] encodeBinaryMessage(AC35Data message, int ackNumber) throws InvalidMessageException {

        //Encodes the message.
        byte[] encodedMessage = RaceVisionByteEncoder.encode(message);

        //Encodes the full message with header.
        BinaryMessageEncoder binaryMessageEncoder = new BinaryMessageEncoder(
                message.getType(),
                System.currentTimeMillis(),
                ackNumber,
                (short) encodedMessage.length,
                encodedMessage  );


        return binaryMessageEncoder.getFullMessage();
    }


}
