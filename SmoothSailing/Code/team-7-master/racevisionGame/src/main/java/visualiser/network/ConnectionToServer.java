package visualiser.network;


import mock.model.commandFactory.Command;
import network.Messages.AC35Data;
import network.Messages.Enums.RequestToJoinEnum;
import network.Messages.JoinAcceptance;
import network.Messages.RequestToJoin;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import shared.model.RunnableWithFramePeriod;
import visualiser.enums.ConnectionToServerState;

import java.util.concurrent.BlockingQueue;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * This class tracks the state of the connection to a server.
 */
public class ConnectionToServer implements RunnableWithFramePeriod {


    /**
     * The state of the connection to the client.
     */
    private ConnectionToServerState connectionState = ConnectionToServerState.UNKNOWN;


    /**
     * The type of join request to make to server.
     */
    private RequestToJoinEnum requestType;


    /**
     * The queue to place outgoing messages on.
     */
    private BlockingQueue<AC35Data> outgoingMessages;



    /**
     * The {@link JoinAcceptance} message that has been received, if any.
     */
    @Nullable
    private JoinAcceptance joinAcceptance;


    /**
     * The incoming commands to execute.
     */
    private BlockingQueue<Command> incomingCommands;



    /**
     * Constructs a ConnectionToServer with a given state.
     * @param connectionState The state of the connection.
     * @param requestType The type of join request to make to server.
     * @param incomingCommands The queue of commands to execute.
     * @param outgoingMessages The queue to place outgoing messages on.
     */
    public ConnectionToServer(ConnectionToServerState connectionState, RequestToJoinEnum requestType, BlockingQueue<Command> incomingCommands, BlockingQueue<AC35Data> outgoingMessages) {
        this.connectionState = connectionState;
        this.requestType = requestType;
        this.incomingCommands = incomingCommands;
        this.outgoingMessages = outgoingMessages;
    }


    /**
     * Returns the state of this connection.
     * @return The state of this connection.
     */
    public ConnectionToServerState getConnectionState() {
        return connectionState;
    }

    /**
     * Sets the state of this connection.
     * @param connectionState The new state of this connection.
     */
    public void setConnectionState(ConnectionToServerState connectionState) {
        this.connectionState = connectionState;
    }


    /**
     * Returns the {@link JoinAcceptance} message received from the server, if any.
     * @return The JoinAcceptance message from server. Null if no response from server.
     */
    @Nullable
    public JoinAcceptance getJoinAcceptance() {
        return joinAcceptance;
    }

    /**
     * Sets the {@link JoinAcceptance} message received from the server, if any.
     * @param joinAcceptance The new JoinAcceptance message from server.
     */
    public void setJoinAcceptance(@NotNull JoinAcceptance joinAcceptance) {
        this.joinAcceptance = joinAcceptance;
    }



    @Override
    public void run() {

        try {
            sendRequestToJoinMessage(requestType);

        } catch (InterruptedException e) {
            Logger.getGlobal().log(Level.SEVERE, "ConnectionToServer: " + this + " was interrupted on thread: " + Thread.currentThread() + " while sending RequestToJoin.", e);
            Thread.currentThread().interrupt();

        }

        while (!Thread.interrupted()) {

            try {
                Command command = incomingCommands.take();
                command.execute();

            } catch (InterruptedException e) {
                Logger.getGlobal().log(Level.SEVERE, "ConnectionToServer: " + this + " was interrupted on thread: " + Thread.currentThread() + " while reading command.", e);
                Thread.currentThread().interrupt();
            }

        }

        //If we get interrupted, we consider the connection to have timed-out.
        connectionState = ConnectionToServerState.TIMED_OUT;

    }



    /**
     * Sends the server a {@link RequestToJoin} message.
     * @param requestType The type of request to send
     * @throws InterruptedException Thrown if the thread is interrupted while placing message on the outgoing message queue.
     */
    private void sendRequestToJoinMessage(RequestToJoinEnum requestType) throws InterruptedException {

        //Send them the source ID.
        RequestToJoin requestToJoin = new RequestToJoin(requestType);

        send(requestToJoin);

        connectionState = ConnectionToServerState.REQUEST_SENT;
    }


    /**
     * Sends a given message to the server, via the {@link #outgoingMessages} queue.
     * @param message Message to send.
     * @throws InterruptedException Thrown if thread is interrupted while sending message.
     */
    public void send(AC35Data message) throws InterruptedException {
        outgoingMessages.put(message);
    }


    /**
     * Returns the type of join request that was made.
     * @return Type of join request made.
     */
    public RequestToJoinEnum getRequestType() {
        return requestType;
    }
}
