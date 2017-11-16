package network.StreamRelated;


import network.BinaryMessageDecoder;
import network.Exceptions.InvalidMessageException;
import network.Messages.AC35Data;
import shared.model.RunnableWithFramePeriod;

import java.io.*;
import java.nio.ByteBuffer;
import java.util.Arrays;
import java.util.concurrent.BlockingQueue;
import java.util.logging.Level;
import java.util.logging.Logger;

import static network.Utils.ByteConverter.bytesToShort;

/**
 * This class is responsible for converting data from an input stream into a queue of {@link AC35Data} messages.
 */
public class MessageDeserialiser implements RunnableWithFramePeriod {


    /**
     * The stream we're reading from.
     */
    private DataInputStream inputStream;

    /**
     * The messages we've read.
     */
    private BlockingQueue<AC35Data> messagesRead;


    /**
     * Determines whether or not this runnable is currently running.
     */
    private boolean isRunning;




    /**
     * Constructs a new MessageSerialiser to write a queue of messages to a given stream.
     * @param inputStream The stream to write to.
     * @param messagesRead The messages to send.
     */
    public MessageDeserialiser(InputStream inputStream, BlockingQueue<AC35Data> messagesRead) {
        this.inputStream = new DataInputStream(inputStream);
        this.messagesRead = messagesRead;
    }

    /**
     * Returns the queue of messages read from the socket.
     * @return Queue of messages read from socket.
     */
    public BlockingQueue<AC35Data> getMessagesRead() {
        return messagesRead;
    }


    /**
     * Reads and returns the next message as an array of bytes from the input stream. Use getNextMessage() to get the actual message object instead.
     * @return Encoded binary message bytes.
     * @throws IOException Thrown when an error occurs while reading from the input stream.
     */
    private byte[] getNextMessageBytes() throws IOException {
        inputStream.mark(0);
        short CRCLength = 4;
        short headerLength = 15;

        //Read the header of the next message.
        byte[] headerBytes = new byte[headerLength];
        inputStream.readFully(headerBytes);

        //Read the message body length.
        byte[] messageBodyLengthBytes = Arrays.copyOfRange(headerBytes, headerLength - 2, headerLength);
        short messageBodyLength = bytesToShort(messageBodyLengthBytes);

        //Read the message body.
        byte[] messageBodyBytes = new byte[messageBodyLength];
        inputStream.readFully(messageBodyBytes);

        //Read the message CRC.
        byte[] messageCRCBytes = new byte[CRCLength];
        inputStream.readFully(messageCRCBytes);

        //Put the head + body + crc into one large array.
        ByteBuffer messageBytes = ByteBuffer.allocate(headerBytes.length + messageBodyBytes.length + messageCRCBytes.length);
        messageBytes.put(headerBytes);
        messageBytes.put(messageBodyBytes);
        messageBytes.put(messageCRCBytes);

        return messageBytes.array();
    }


    /**
     * Reads and returns the next message object from the input stream.
     * @return The message object.
     * @throws IOException Thrown when an error occurs while reading from the input stream.
     * @throws InvalidMessageException Thrown when the message is invalid in some way.
     */
    private AC35Data getNextMessage() throws IOException, InvalidMessageException
    {
        //Get the next message from the socket as a block of bytes.
        byte[] messageBytes = this.getNextMessageBytes();

        //Decode the binary message into an appropriate message object.
        BinaryMessageDecoder decoder = new BinaryMessageDecoder(messageBytes);

        return decoder.decode();

    }


    /**
     * Determines whether or not this runnable is running.
     * @return True means that it is still running, false means that it has stopped.
     */
    public boolean isRunning() {
        return isRunning;
    }



    @Override
    public void run() {

        isRunning = true;

        while (!Thread.interrupted()) {

            //Reads the next message.
            try {
                AC35Data message = this.getNextMessage();
                messagesRead.add(message);
            }
            catch (InvalidMessageException e) {
                Logger.getGlobal().log(Level.WARNING, "Unable to read message on thread: " + Thread.currentThread() + ".", e);

            } catch (IOException e) {
                Logger.getGlobal().log(Level.SEVERE, "Unable to read inputStream: " + inputStream + " on thread: " + Thread.currentThread() + ".", e);
                isRunning = false;
                return;

            }

        }

    }
}
