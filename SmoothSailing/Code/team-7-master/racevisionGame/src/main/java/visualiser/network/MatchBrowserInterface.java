package visualiser.network;

import network.BinaryMessageEncoder;
import network.Exceptions.InvalidMessageException;
import network.MessageEncoders.HostGameMessageEncoder;
import network.MessageEncoders.HostedGamesRequestEncoder;
import network.Messages.AC35Data;
import network.Messages.Enums.MessageType;
import network.Messages.HostGamesRequest;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.ArrayList;

/**
 * UDP interface for the matchBrowser to send out hosted game info and get in other hosts info
 */
public class MatchBrowserInterface {
    //ip of the server
    private InetAddress IPAddress;
    //port server is hosted on
    private int port;

    public MatchBrowserInterface() {
        try {//132.181.16.13 is the ip of the CI as of 13/9/17
            //this.IPAddress = InetAddress.getByName("132.181.16.13"); //InetAddress.getLocalHost();
            //this.IPAddress = InetAddress.getByName("umbrasheep.com"); //InetAddress.getLocalHost();
            this.IPAddress = InetAddress.getByName("191.101.233.116"); //InetAddress.getLocalHost();
        } catch (UnknownHostException e) {
            e.printStackTrace();
        }
        this.port = 3779;
    }

    /**
     * Used by host to send out race information to the server
     * @param gameInfo the hostGame info for message
     * @param socket the udp socket assigned on startup
     * @throws IOException socket error
     */
    protected void sendOutGameInfo(AC35Data gameInfo, DatagramSocket socket) throws IOException{
        byte[] fullMessageToSend;
        try{
            HostGameMessageEncoder encoder = new HostGameMessageEncoder();
            byte[] message = encoder.encode(gameInfo);
            BinaryMessageEncoder messageEncoder = new BinaryMessageEncoder(MessageType.HOST_GAME
                    ,System.currentTimeMillis(), 1,(short) 13 ,message);
            fullMessageToSend = messageEncoder.getFullMessage();

            DatagramPacket sendPacket = new DatagramPacket(fullMessageToSend, fullMessageToSend.length, IPAddress, port);
            socket.send(sendPacket);
        }catch (InvalidMessageException e){
            System.err.println("HostGameMessage could not be encoded");
        }
    }

    /**
     *  start to send these messages on repeat until game stopped
     * @param gameInfo hostgame data
     * @param socket socket to send to
     */
    public void startSendingHostData(AC35Data gameInfo, DatagramSocket socket){
        MatchBrowserHostRunnable hostRunnable = new MatchBrowserHostRunnable(this, socket, gameInfo);
        Thread hostRunnableThread = new Thread(hostRunnable, "Socket: " + socket.toString());
        hostRunnableThread.start();
    }

    /**
     * Used by a client to setup a connection with the match browser server
     * @return the socket created for this connection
     * @throws IOException socket error
     */
    public DatagramSocket setupMatchBrowserConnection() throws IOException{
        DatagramSocket clientSocket = new DatagramSocket();

        //creates and empty hostedGamesRequest packet that can be sent to the server
        //this lets the server check the udp fields for ip and port to know that this client exists
        try{
            HostedGamesRequestEncoder encoder = new HostedGamesRequestEncoder();
            byte[] message = encoder.encode(new HostGamesRequest(new ArrayList()));
            BinaryMessageEncoder messageEncoder = new BinaryMessageEncoder(MessageType.HOSTED_GAMES_REQUEST
                    ,System.currentTimeMillis(), 1,(short) 13 ,message);

            DatagramPacket sendPacket = new DatagramPacket(messageEncoder.getFullMessage(), messageEncoder.getFullMessage().length, IPAddress, port);
            clientSocket.send(sendPacket);
        }catch (InvalidMessageException e){
            System.err.println("HostedGamesRequestMessage could not be encoded");
        }

        return clientSocket;
    }
}
