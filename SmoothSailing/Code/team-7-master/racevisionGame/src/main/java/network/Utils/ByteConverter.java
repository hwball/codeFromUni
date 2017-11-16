package network.Utils;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.util.Arrays;

/**
 * Created by fwy13 on 25/04/17.
 */
public class ByteConverter {

    public static int IntegerSize = 4;
    public static int LongSize = 8;
    public static int CharSize = 2;
    public static int ShortSize = 2;


    //default for AC35 is Little Endian therefore all overloads will be done with Little_Endian unless told else wise

    //////////////////////////////////////////////////
    //Bytes[] to number conversions
    //////////////////////////////////////////////////

    //////////////////////////////////////////////////
    //Integer
    //////////////////////////////////////////////////

    /**
     * @param bite bite to convert
     * @return int
     */
    public static int bytesToInt(byte bite){
        byte[] bytes = {bite};
        return bytesToInt(bytes, ByteOrder.LITTLE_ENDIAN);
    }

    /**
     * @param bytes bytes to convert
     * @return int
     */
    public static int bytesToInt(byte[] bytes){
        return bytesToInt(bytes, ByteOrder.LITTLE_ENDIAN);
    }

    /**
     * @param bytes bytes to convert
     * @param byteOrder order of the bytes
     * @return int
     */
    public static int bytesToInt(byte[] bytes, ByteOrder byteOrder){
        byte[] bites = convertBytesToNum(bytes,byteOrder, IntegerSize);
        return ByteBuffer.wrap(bites).order(byteOrder).getInt();
    }

    //////////////////////////////////////////////////
    //Long
    //////////////////////////////////////////////////

    public static long bytesToLong(byte bite){
        byte[] bytes = {bite};
        return bytesToLong(bytes, ByteOrder.LITTLE_ENDIAN);
    }

    public static long bytesToLong(byte[] bytes){
        return bytesToLong(bytes, ByteOrder.LITTLE_ENDIAN);
    }

    public static long bytesToLong(byte[] bytes, ByteOrder byteOrder){
        byte[] bites = convertBytesToNum(bytes,byteOrder, LongSize);
        return ByteBuffer.wrap(bites).order(byteOrder).getLong();
    }

    //////////////////////////////////////////////////
    //Short
    //////////////////////////////////////////////////

    public static short bytesToShort(byte bite){
        byte[] bytes = {bite};
        return bytesToShort(bytes, ByteOrder.LITTLE_ENDIAN);
    }

    public static short bytesToShort(byte[] bytes){
        return bytesToShort(bytes, ByteOrder.LITTLE_ENDIAN);
    }

    public static short bytesToShort(byte[] bytes, ByteOrder byteOrder){
        byte[] bites = convertBytesToNum(bytes,byteOrder, ShortSize);
        return ByteBuffer.wrap(bites).order(byteOrder).getShort();
    }

    //////////////////////////////////////////////////
    //Char
    //////////////////////////////////////////////////

    public static char bytesToChar(byte bite){
        byte[] bytes = {bite};
        return bytesToChar(bytes, ByteOrder.LITTLE_ENDIAN);
    }

    public static char bytesToChar(byte[] bytes){
        return bytesToChar(bytes, ByteOrder.LITTLE_ENDIAN);
    }

    public static char bytesToChar(byte[] bytes, ByteOrder byteOrder){
        byte[] bites = convertBytesToNum(bytes,byteOrder, CharSize);
        return ByteBuffer.wrap(bites).order(byteOrder).getChar();
    }

    //////////////////////////////////////////////////
    //Conversion Function
    //////////////////////////////////////////////////

    private static byte[] convertBytesToNum(byte[] bytes, ByteOrder byteOrder, int maxSize){
        byte[] bites = new byte[maxSize];
        if (byteOrder == ByteOrder.LITTLE_ENDIAN) {
            for (int i = 0; i < bytes.length; i++) {
                if (i > maxSize){//break if over hte limit
                    break;
                }
                bites[i] = bytes[i];
            }
            for (int i = bytes.length; i < maxSize; i++) {
                bites[i] = 0b0;
            }
        }else{//if big endian
            for (int i = 0; i <  maxSize - bytes.length; i++) {
                bites[i] = 0b0;
            }
            for (int i = maxSize - bytes.length; i < maxSize; i++) {
                if (i > maxSize){//break if over the limit
                    break;
                }
                bites[i] = bytes[i - maxSize + bytes.length];
            }
        }
        return bites;
    }

    //////////////////////////////////////////////////////////
    //Number to Byte[] conversions
    //////////////////////////////////////////////////////////

    //////////////////////////////////////////////////
    //Integer
    //////////////////////////////////////////////////

    public static byte[] intToBytes(int i){
        return intToBytes(i, 4, ByteOrder.LITTLE_ENDIAN);
    }

    public static byte[] intToBytes(int i ,int size){
        return intToBytes(i, size, ByteOrder.LITTLE_ENDIAN);
    }

    /**
     * Converts an Integer to a Byte Array
     * @param i the integer to be converted
     * @param size Size that the byte array should be
     * @param byteOrder the order that the bytes should be ie Big Endian
     * @return bytes array
     */
    public static byte[] intToBytes(int i ,int size, ByteOrder byteOrder){
        ByteBuffer buffer = ByteBuffer.allocate(IntegerSize);
        buffer.order(byteOrder);
        buffer.putInt(i);
        byte[] copy = buffer.array();
        return convertNumtoBytes(copy, size, byteOrder, IntegerSize);
    }

    //////////////////////////////////////////////////
    //Long
    //////////////////////////////////////////////////

    public static byte[] longToBytes(long i){
        return longToBytes(i, LongSize, ByteOrder.LITTLE_ENDIAN);
    }

    public static byte[] longToBytes(long i ,int size){
        return longToBytes(i, size, ByteOrder.LITTLE_ENDIAN);
    }

    /**
     * Converts an Long to a Byte Array
     * @param i the Long to be converted
     * @param size Size that the byte array should be
     * @param byteOrder the order that the bytes should be ie Big Endian
     * @return byte array
     */
    public static byte[] longToBytes(long i ,int size, ByteOrder byteOrder){
        ByteBuffer buffer = ByteBuffer.allocate(LongSize);
        buffer.order(byteOrder);
        buffer.putLong(i);
        byte[] copy = buffer.array();
        return convertNumtoBytes(copy, size, byteOrder, LongSize);
    }

    //////////////////////////////////////////////////
    //Short
    //////////////////////////////////////////////////

    public static byte[] shortToBytes(short i){
        return shortToBytes(i, ShortSize, ByteOrder.LITTLE_ENDIAN);
    }

    public static byte[] shortToBytes(short i ,int size){
        return shortToBytes(i, size, ByteOrder.LITTLE_ENDIAN);
    }

    /**
     * Converts an Short to a Byte Array
     * @param i the Short to be converted
     * @param size Size that the byte array should be
     * @param byteOrder the order that the bytes should be ie Big Endian
     * @return byte array
     */
    public static byte[] shortToBytes(short i ,int size, ByteOrder byteOrder){
        ByteBuffer buffer = ByteBuffer.allocate(ShortSize);
        buffer.order(byteOrder);
        buffer.putShort(i);
        byte[] copy = buffer.array();
        return convertNumtoBytes(copy, size, byteOrder, ShortSize);
    }

    //////////////////////////////////////////////////
    //Char
    //////////////////////////////////////////////////

    public static byte[] charToBytes(char i){
        return charToBytes(i, CharSize, ByteOrder.LITTLE_ENDIAN);
    }

    public static byte[] charToBytes(char i ,int size){
        return charToBytes(i, size, ByteOrder.LITTLE_ENDIAN);
    }

    /**
     * Converts an Char to a Byte Array
     * @param i the Char to be converted
     * @param size Size that the byte array should be
     * @param byteOrder the order that the bytes should be ie Big Endian
     * @return byte array
     */
    public static byte[] charToBytes(char i ,int size, ByteOrder byteOrder){
        ByteBuffer buffer = ByteBuffer.allocate(CharSize);
        buffer.order(byteOrder);
        buffer.putChar(i);
        byte[] copy = buffer.array();
        return convertNumtoBytes(copy, size, byteOrder, CharSize);
    }

    //////////////////////////////////////////////////
    //Conversion Function
    //////////////////////////////////////////////////

    private static byte[] convertNumtoBytes(byte[] copy ,int size, ByteOrder byteOrder, int fullsize){
        byte[] bytes = new byte[size];
        if (byteOrder == ByteOrder.LITTLE_ENDIAN){
            bytes = Arrays.copyOfRange(copy, 0, size);
        }else{// if it is Big Endian
            bytes = Arrays.copyOfRange(copy, fullsize - size, fullsize);
        }
        return bytes;
    }


}
