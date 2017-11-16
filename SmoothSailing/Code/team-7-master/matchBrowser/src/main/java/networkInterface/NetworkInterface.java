package networkInterface;

import model.ClientAddress;
import model.MatchTable;
import network.BinaryMessageDecoder;
import network.Exceptions.InvalidMessageException;
import network.MessageDecoders.HostGameMessageDecoder;
import network.MessageDecoders.HostedGamesRequestDecoder;
import network.MessageEncoders.HostedGamesRequestEncoder;
import network.Messages.Enums.MessageType;
import network.Messages.HostGame;
import network.Messages.HostGamesRequest;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.time.LocalDateTime;
import java.util.*;

/**
 * Holds the output for the network for
 */
public class NetworkInterface {
    private Timer scheduler;
    private DatagramSocket serverSocket;
    private byte[] receiveData = new byte[1024];

    private Set<ClientAddress> clientsAddresses;
    private MatchTable matchTable;


    public NetworkInterface(){
        this.clientsAddresses = new HashSet<>();
        this.matchTable = new MatchTable();
        this.scheduler = new Timer(true);
        try {
            this.serverSocket = new DatagramSocket(3779);

            startBroadcast(5000);
            scheduleFlush(70000);
            this.run();
        } catch (IOException e) {
            System.err.println("Error listening on port: " +  this.serverSocket.getLocalPort() + ".");
            System.exit(-1);
        }

    }

    /**
     * Broadcasts match table to clients at a requested interval
     * @param period interval to broadcast table
     */
    private void startBroadcast(int period) {
        scheduler.scheduleAtFixedRate(new TimerTask() {
            @Override
            public void run() {
                List<HostGame> games = new ArrayList<>();

                for(Map.Entry<ClientAddress, HostGame> tableEntry: matchTable.getMatchTable().entrySet()) {
                    HostGame game = tableEntry.getValue();
                    if(game != null) {
                        games.add(game);
                    }
                }

                HostedGamesRequestEncoder encoder = new HostedGamesRequestEncoder();
                try {
                    byte[] message = encoder.encode(new HostGamesRequest(games));
                    System.out.println(LocalDateTime.now() + ": Sending " + games.size() + " game/s");
                    for(ClientAddress address: clientsAddresses) {
                        System.out.println("Sending to " + address.getIp());
                        serverSocket.send(new DatagramPacket(message, message.length, InetAddress.getByName(address.getIp()), 4941));
                    }
                } catch (InvalidMessageException | IOException e) {
                    e.printStackTrace();
                }
            }
        }, period, period);
    }

    /**
     * Flushes the match table at a requested interval
     * @param period interval to flush table
     */
    private void scheduleFlush(int period) {
        scheduler.scheduleAtFixedRate(new TimerTask() {
            @Override
            public void run() {
                System.out.println("flush");
                matchTable.getMatchTable().clear();
            }
        }, period, period);
    }

    private void run() throws IOException{
        while(true) {
            DatagramPacket receivePacket = new DatagramPacket(receiveData, receiveData.length);
            serverSocket.receive(receivePacket);

            BinaryMessageDecoder messageDecoder = new BinaryMessageDecoder(receivePacket.getData());
            switch (MessageType.fromByte(messageDecoder.getHeaderMessageType())){
                case HOST_GAME:
                    //decode and update table
                    HostGameMessageDecoder decoder = new HostGameMessageDecoder();
                    HostGame newKnownGame;
                    try{
                        newKnownGame = (HostGame) decoder.decode(messageDecoder.getMessageBody());
                        newKnownGame.setIp(receivePacket.getAddress().getHostAddress());
                        this.matchTable.addEntry(new ClientAddress(receivePacket.getAddress().getHostAddress(), receivePacket.getPort()), newKnownGame);

                    }catch (InvalidMessageException e){
                        System.out.println(e);
                        System.err.println("Message received that is not a hostedGame packet");
                    }
                    break;
                case HOSTED_GAMES_REQUEST:
                    //update known clients
                    HostedGamesRequestDecoder decoder2 = new HostedGamesRequestDecoder();
                    HostGamesRequest newKnownGames;
                    try{
                        newKnownGames = (HostGamesRequest) decoder2.decode(messageDecoder.getMessageBody());
                        if (newKnownGames.getKnownGames().size() == 0){
                            //this is just an alert message with no content
                            clientsAddresses.add(new ClientAddress(receivePacket.getAddress().getHostAddress(), receivePacket.getPort()));
                        }
                    }catch (InvalidMessageException e){
                        System.out.println(e);
                        System.err.println("Message received that is not a hostedGamesRequest packet");
                    }
                    break;
            }
        }
    }
}
