package visualiser.gameController;

import network.BinaryMessageEncoder;
import network.Exceptions.InvalidMessageException;
import network.MessageEncoders.RaceVisionByteEncoder;
import network.Messages.AC35Data;
import network.Messages.BoatAction;
import network.Messages.Enums.BoatActionEnum;
import network.Messages.Enums.MessageType;
import visualiser.gameController.Keys.ControlKey;

import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.nio.ByteBuffer;
import java.util.concurrent.BlockingQueue;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Basic service for sending key presses to game server
 */
public class ControllerClient  {

    /**
     * Queue of messages to be sent to server.
     */
    private BlockingQueue<AC35Data> outgoingMessages;

    /**
     * Initialise controller client with live socket.
     * @param outgoingMessages Queue to place messages on to send to server.
     */
    public ControllerClient(BlockingQueue<AC35Data> outgoingMessages) {
        this.outgoingMessages = outgoingMessages;
    }

    /**
     * Send a keypress to server
     * @param key to send
     * @throws InterruptedException If thread is interrupted while writing message to outgoing message queue.
     */
    public void sendKey(ControlKey key) throws InterruptedException {
        BoatActionEnum protocolCode = key.getProtocolCode();
        if(protocolCode != BoatActionEnum.NOT_A_STATUS) {

            BoatAction boatAction = new BoatAction(protocolCode);

            outgoingMessages.put(boatAction);

        }
    }
}
