package com.ezasm.util;

import com.ezasm.simulation.Memory;

import java.nio.ByteBuffer;

/**
 * Utility class which provides functions for converting to and from byte arrays.
 */
public class Conversion {

    /**
     * Converts a long into its corresponding bytes.
     *
     * @param data the long to convert.
     * @return the byte data representation of the long.
     */
    public static byte[] longToBytes(long data) {
        if (Memory.getWordSize() == 4) {
            return ByteBuffer.wrap(new byte[Memory.getWordSize()]).putInt((int) data).array();
        } else if (Memory.getWordSize() == 8) {
            return ByteBuffer.wrap(new byte[Memory.getWordSize()]).putLong(data).array();
        }
        return null;
    }

    /**
     * Converts an array of bytes into the corresponding long.
     *
     * @param data the array of bytes to convert.
     * @return the long representation of that data.
     */
    public static long bytesToLong(byte[] data) {
        if (data.length == 4) {
            return ByteBuffer.wrap(data).getInt();
        } else if (data.length == 8) {
            return ByteBuffer.wrap(data).getLong();
        }
        return 0;
    }

    /**
     * Converts a double into its corresponding bytes.
     *
     * @param data the double to convert.
     * @return the byte data representation of the double.
     */
    public static byte[] doubleToBytes(double data) {
        if (Memory.getWordSize() == 4) {
            return ByteBuffer.wrap(new byte[Memory.getWordSize()]).putFloat((float) data).array();
        } else if (Memory.getWordSize() == 8) {
            return ByteBuffer.wrap(new byte[Memory.getWordSize()]).putDouble(data).array();
        }
        return null;
    }

    /**
     * Converts an array of bytes into the corresponding double.
     *
     * @param data the array of bytes to convert.
     * @return the double representation of that data.
     */
    public static double bytesToDouble(byte[] data) {
        if (data.length == 4) {
            return ByteBuffer.wrap(data).getFloat();
        } else if (data.length == 8) {
            return ByteBuffer.wrap(data).getLong();
        }
        return 0;
    }
}
