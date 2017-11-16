package network.PacketDump;


import network.BinaryMessageDecoder;
import network.Exceptions.InvalidMessageException;
import network.Messages.AC35Data;

import java.io.IOException;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.ByteBuffer;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;

/**
 * Created by fwy13 on 25/04/17.
 */
public class AC35DumpReader {

    private byte[] dump;
    private ArrayList<AC35Packet> packets;

    public AC35DumpReader(String url) throws IOException, URISyntaxException {

        URL uri = getClass().getClassLoader().getResource(url);
        Path path = Paths.get(uri.toURI());
        dump = Files.readAllBytes(path);

        packets = new ArrayList<>();

        readAllPackets();
    }

    private void readAllPackets(){
        int pointer = 0;
        while(pointer < dump.length){
            byte[] messLen = new byte[2];
            messLen[1] = dump[pointer + 13];
            messLen[0] = dump[pointer + 14];
            int messageLength = ByteBuffer.wrap(messLen).getShort();

            packets.add(new AC35Packet(Arrays.copyOfRange(dump, pointer, pointer + messageLength + 19)));

            pointer += 19 + messageLength;
        }
        for (AC35Packet pack: packets){
            BinaryMessageDecoder decoder = new BinaryMessageDecoder(pack.getData());

            try {
                AC35Data data = decoder.decode();
            }
            catch (InvalidMessageException e) {
                System.out.println(e.getMessage());
            }
        }

    }

    public static void main(String[] args){
        try {
            AC35DumpReader ac35DumpReader = new AC35DumpReader("dataDumps/ac35.bin");
        } catch (IOException e) {
            e.printStackTrace();
        } catch (URISyntaxException e) {
            e.printStackTrace();
        }
    }
}
