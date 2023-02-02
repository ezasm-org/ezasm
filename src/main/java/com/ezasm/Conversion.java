package com.ezasm;

import java.nio.ByteBuffer;

/**
 * Utility class which provides functions for converting to and from byte
 * arrays.
 */
public class Conversion {

    /**
     * Converts a long into its corresponding bytes.
     *
     * @param data the long to convert.
     * @return the byte data representation of the long.
     */
    public static byte[] longToBytes(long data) {
        return ByteBuffer.wrap(new byte[8]).putLong(data).array();
    }

    /**
     * Converts an array of bytes into the corresponding long.
     *
     * @param data the array of bytes to convert.
     * @return the long representation of that data.
     */
    public static long bytesToLong(byte[] data) {
        return ByteBuffer.wrap(data).getLong();
    }

    /**
     * Converts a double into its corresponding bytes.
     *
     * @param data the double to convert.
     * @return the byte data representation of the double.
     */
    public static byte[] doubleToBytes(double data) {
        return ByteBuffer.wrap(new byte[8]).putDouble(data).array();
    }

    /**
     * Converts an array of bytes into the corresponding double.
     *
     * @param data the array of bytes to convert.
     * @return the double representation of that data.
     */
    public static double bytesToDouble(byte[] data) {
        return ByteBuffer.wrap(data).getDouble();
    }

    /**
     * Converts a String into its corresponding bytes.
     *
     * @param data the String to convert.
     * @return the byte data representation of the String.
     */
    public static byte[] stringToBytes(String data) {
        return data.getBytes();
    }

    /**
     * Converts an array of bytes into the corresponding double.
     *
     * @param data the array of bytes to convert.
     * @return the double representation of that data.
     */
    public static String bytesToString(byte[] data) {
        return new String(data);
    }

}
