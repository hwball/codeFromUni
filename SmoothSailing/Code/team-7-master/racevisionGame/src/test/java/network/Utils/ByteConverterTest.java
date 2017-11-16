package network.Utils;

import org.junit.Test;

import java.nio.ByteOrder;

import static org.junit.Assert.assertTrue;


/**
 * Created by fwy13 on 25/04/17.
 */
public class ByteConverterTest {

    @Test
    public void testLargerIntToByte(){
        int int1 = 1532158456; //100 in bytes
        byte[] bytes1 = {(byte)0xF8, (byte)0xE1, 0x52, 0x5B};//this is in little endian
        assertTrue(testArrayContents(ByteConverter.intToBytes(int1), bytes1));
        byte[] bytes2 = {0x5B, 0x52, (byte)0xE1, (byte)0xF8};// this is big endian
        assertTrue(testArrayContents(ByteConverter.intToBytes(int1, ByteConverter.IntegerSize, ByteOrder.BIG_ENDIAN), bytes2));
        //test chopping
        byte[] chopped1 = ByteConverter.intToBytes(int1, 3, ByteOrder.LITTLE_ENDIAN);
        byte[] bytes3 = {(byte)0xF8, (byte)0xE1, 0x52};
        assertTrue(testArrayContents(chopped1, bytes3));
        byte[] chopped2 = ByteConverter.intToBytes(int1, 2, ByteOrder.LITTLE_ENDIAN);
        byte[] bytes4 = {(byte)0xF8, (byte)0xE1};
        assertTrue(testArrayContents(chopped2, bytes4));
        byte[] chopped3 = ByteConverter.intToBytes(int1, 1, ByteOrder.LITTLE_ENDIAN);
        byte[] bytes5 = {(byte)0xF8};
        assertTrue(testArrayContents(chopped3, bytes5));

        byte[] chopped4 = ByteConverter.intToBytes(int1, 3, ByteOrder.BIG_ENDIAN);
        byte[] bytes6 = {0x52, (byte)0xE1, (byte)0xF8};
        assertTrue(testArrayContents(chopped4, bytes6));
        byte[] chopped5 = ByteConverter.intToBytes(int1, 2, ByteOrder.BIG_ENDIAN);
        byte[] bytes7 = {(byte)0xE1, (byte)0xF8};
        assertTrue(testArrayContents(chopped5, bytes7));
        byte[] chopped6 = ByteConverter.intToBytes(int1, 1, ByteOrder.BIG_ENDIAN);
        byte[] bytes8 = {(byte)0xF8};
        assertTrue(testArrayContents(chopped6, bytes8));
    }

    @Test
    public void testByteToInt(){
        int int1 = 100; //100 in bytes
        byte[] bytes1 = {100, 0, 0, 0};//this is in little endian
        assertTrue(ByteConverter.bytesToInt(bytes1) == int1);
        assertTrue(ByteConverter.bytesToInt(bytes1, ByteOrder.LITTLE_ENDIAN) == int1);
        byte[] bytes2 = {0, 0, 0, 100};// this is big endian
        assertTrue(ByteConverter.bytesToInt(bytes2, ByteOrder.BIG_ENDIAN) == int1);
        //check single bytes to integers
        assertTrue(ByteConverter.bytesToInt((byte)100) == int1);
    }

    @Test
    public void testByteToLong(){
        long lng1 = 15; //100 in bytes
        byte[] bytes1 = {15, 0, 0, 0,   0, 0, 0, 0};//this is in little endian
        assertTrue(ByteConverter.bytesToLong(bytes1) == lng1);
        assertTrue(ByteConverter.bytesToLong(bytes1, ByteOrder.LITTLE_ENDIAN) == lng1);
        byte[] bytes2 = {0, 0, 0, 0,   0, 0, 0, 15};// this is big endian
        assertTrue(ByteConverter.bytesToLong(bytes2, ByteOrder.BIG_ENDIAN) == lng1);
        //check single bytes to integers
        assertTrue(ByteConverter.bytesToLong((byte)15) == lng1);
    }

    @Test
    public void testByteToShort(){
        short short1 = 20; //100 in bytes
        byte[] bytes1 = {20, 0};//this is in little endian
        assertTrue(ByteConverter.bytesToShort(bytes1) == short1);
        assertTrue(ByteConverter.bytesToShort(bytes1, ByteOrder.LITTLE_ENDIAN) == short1);
        byte[] bytes2 = {0, 20};// this is big endian
        assertTrue(ByteConverter.bytesToShort(bytes2, ByteOrder.BIG_ENDIAN) == short1);
        //check single bytes to integers
        assertTrue(ByteConverter.bytesToShort((byte)20) == short1);
    }

    @Test
    public void testByteToChar(){
        char char1 = 20; //100 in bytes
        byte[] bytes1 = {20, 0};//this is in little endian
        assertTrue(ByteConverter.bytesToChar(bytes1) == char1);
        assertTrue(ByteConverter.bytesToChar(bytes1, ByteOrder.LITTLE_ENDIAN) == char1);
        byte[] bytes2 = {0, 20};// this is big endian
        assertTrue(ByteConverter.bytesToChar(bytes2, ByteOrder.BIG_ENDIAN) == char1);
        //check single bytes to integers
        assertTrue(ByteConverter.bytesToChar((byte)20) == char1);
    }

    @Test
    public void testIntToByte(){
        int int1 = 100; //100 in bytes
        byte[] bytes1 = {100, 0, 0, 0};//this is in little endian
        assertTrue(testArrayContents(ByteConverter.intToBytes(int1), bytes1));
        byte[] bytes2 = {0, 0, 0, 100};// this is big endian
        assertTrue(testArrayContents(ByteConverter.intToBytes(int1, ByteConverter.IntegerSize, ByteOrder.BIG_ENDIAN), bytes2));
        //test chopping
        byte[] chopped1 = ByteConverter.intToBytes(int1, 3, ByteOrder.LITTLE_ENDIAN);
        byte[] bytes3 = {100, 0, 0};
        assertTrue(testArrayContents(chopped1, bytes3));
        byte[] chopped2 = ByteConverter.intToBytes(int1, 2, ByteOrder.LITTLE_ENDIAN);
        byte[] bytes4 = {100, 0};
        assertTrue(testArrayContents(chopped2, bytes4));
        byte[] chopped3 = ByteConverter.intToBytes(int1, 1, ByteOrder.LITTLE_ENDIAN);
        byte[] bytes5 = {100};
        assertTrue(testArrayContents(chopped3, bytes5));

        byte[] chopped4 = ByteConverter.intToBytes(int1, 3, ByteOrder.BIG_ENDIAN);
        byte[] bytes6 = {0, 0, 100};
        assertTrue(testArrayContents(chopped4, bytes6));
        byte[] chopped5 = ByteConverter.intToBytes(int1, 2, ByteOrder.BIG_ENDIAN);
        byte[] bytes7 = {0, 100};
        assertTrue(testArrayContents(chopped5, bytes7));
        byte[] chopped6 = ByteConverter.intToBytes(int1, 1, ByteOrder.BIG_ENDIAN);
        byte[] bytes8 = {100};
        assertTrue(testArrayContents(chopped6, bytes8));
    }

    @Test
    public void testLongToBytes(){
        long lng1 = 15; //100 in bytes
        byte[] bytes1 = {15, 0, 0, 0,   0, 0, 0, 0};//this is in little endian
        assertTrue(testArrayContents(ByteConverter.longToBytes(lng1), bytes1));
        byte[] bytes2 = {0, 0, 0, 0,   0, 0, 0, 15};// this is big endian
        assertTrue(testArrayContents(ByteConverter.longToBytes(lng1, ByteConverter.LongSize, ByteOrder.BIG_ENDIAN), bytes2));
        //test chopping
        byte[] chopped1 = ByteConverter.longToBytes(lng1, 3, ByteOrder.LITTLE_ENDIAN);
        byte[] bytes3 = {15, 0, 0};
        assertTrue(testArrayContents(chopped1, bytes3));
        byte[] chopped2 = ByteConverter.longToBytes(lng1, 2, ByteOrder.LITTLE_ENDIAN);
        byte[] bytes4 = {15, 0};
        assertTrue(testArrayContents(chopped2, bytes4));
        byte[] chopped3 = ByteConverter.longToBytes(lng1, 1, ByteOrder.LITTLE_ENDIAN);
        byte[] bytes5 = {15};
        assertTrue(testArrayContents(chopped3, bytes5));

        byte[] chopped4 = ByteConverter.longToBytes(lng1, 3, ByteOrder.BIG_ENDIAN);
        byte[] bytes6 = {0, 0, 15};
        assertTrue(testArrayContents(chopped4, bytes6));
        byte[] chopped5 = ByteConverter.longToBytes(lng1, 2, ByteOrder.BIG_ENDIAN);
        byte[] bytes7 = {0, 15};
        assertTrue(testArrayContents(chopped5, bytes7));
        byte[] chopped6 = ByteConverter.longToBytes(lng1, 1, ByteOrder.BIG_ENDIAN);
        byte[] bytes8 = {15};
        assertTrue(testArrayContents(chopped6, bytes8));
    }

    public boolean testArrayContents(byte[] bytes1, byte[] bytes2){
        if (bytes1.length != bytes2.length){
            return false;
        }
        for (int i = 0; i < bytes1.length; i++){
            if (bytes1[i] != bytes2[i]){
                return false;
            }
        }
        return true;
    }

}
