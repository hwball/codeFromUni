package network.MessageRouters;


import network.Messages.AC35Data;
import network.Messages.Enums.MessageType;
import org.jetbrains.annotations.NotNull;
import shared.model.RunnableWithFramePeriod;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.BlockingQueue;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * This class routes {@link network.Messages.AC35Data} messages to an appropriate message controller.
 */
public class MessageRouter implements RunnableWithFramePeriod {


    /**
     * Incoming queue of messages.
     */
    private BlockingQueue<AC35Data> incomingMessages;


    /**
     * The routing map, which maps from a {@link MessageType} to a message queue.
     */
    private Map<MessageType, BlockingQueue<AC35Data>> routeMap = new HashMap<>();


    /**
     * The default routing queue.
     * Messages without routes are sent here.
     * Nothing by default, which means unrouted messages are discarded
     */
    private Optional<BlockingQueue<AC35Data>> defaultRoute = Optional.empty();



    /**
     * Constructs a {@link MessageRouter} with a given incoming message queue.
     * @param incomingMessages Incoming message queue to read from.
     */
    public MessageRouter(BlockingQueue<AC35Data> incomingMessages) {
        this.incomingMessages = incomingMessages;
    }


    /**
     * Returns the queue the message router reads from.
     * Place messages onto this queue to pass them to the router.
     * @return Queue the message router reads from.
     */
    public BlockingQueue<AC35Data> getIncomingMessageQueue() {
        return incomingMessages;
    }


    /**
     * Adds a route, which routes a given type of message to a given queue.
     * @param messageType The message type to route.
     * @param queue The queue to route messages to.
     */
    public void addRoute(MessageType messageType, BlockingQueue<AC35Data> queue) {
        routeMap.put(messageType, queue);
    }

    /**
     * Removes the route for a given {@link MessageType}.
     * @param messageType MessageType to remove route for.
     */
    public void removeRoute(MessageType messageType) {
        routeMap.remove(messageType);
    }

    /**
     * Adds a given queue as the default route for any unrouted message types.
     * @param queue Queue to use as default route.
     */
    public void addDefaultRoute(@NotNull BlockingQueue<AC35Data> queue) {
        defaultRoute = Optional.of(queue);
    }

    /**
     * Removes the current default route, if it exists.
     */
    public void removeDefaultRoute() {
        defaultRoute = Optional.empty();
    }



    @Override
    public void run() {

        while (!Thread.interrupted()) {

            try {

                AC35Data message = incomingMessages.take();


                BlockingQueue<AC35Data> queue = routeMap.get(message.getType());

                if (queue != null) {
                    queue.put(message);

                } else {
                    //No route. Use default.
                    BlockingQueue<AC35Data> defaultQueue = defaultRoute.orElse(null);

                    if (defaultQueue != null) {
                        defaultQueue.put(message);
                    }

                }


            } catch (InterruptedException e) {
                Logger.getGlobal().log(Level.SEVERE, "MessageRouter: " + this + " was interrupted on thread: " + Thread.currentThread() + " while reading message.", e);
                Thread.currentThread().interrupt();

            }

        }

    }
}
