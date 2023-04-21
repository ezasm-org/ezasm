package com.ezasm.util;

import com.ezasm.simulation.Memory;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.util.Arrays;

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
        if (Memory.wordSize() == 4) {
            return ByteBuffer.wrap(new byte[Memory.wordSize()]).order(ByteOrder.nativeOrder()).putInt((int) data).array();
        } else if (Memory.wordSize() == 8) {
            return ByteBuffer.wrap(new byte[Memory.wordSize()]).order(ByteOrder.nativeOrder()).putLong(data).array();
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
            return ByteBuffer.wrap(data).order(ByteOrder.nativeOrder()).getInt();
        } else if (data.length == 8) {
            return ByteBuffer.wrap(data).order(ByteOrder.nativeOrder()).getLong();
        }
        return 0;
    }

    /**
     * Converts an array of bytes into the corresponding long starting at the given offset.
     *
     * @param data the array of bytes to convert.
     * @param offset the offset into the array to convert from.
     * @return the long representation of that data.
     */
    public static long bytesToLong(byte[] data, int offset) {
        if (data.length - offset < 8) {
            throw new ArithmeticException("Conversion failed due to invalid byte array size.");
        } else {
            return ByteBuffer.wrap(data, data.length, 8).order(ByteOrder.nativeOrder()).getLong();
        }
    }

    /**
     * Converts an array of bytes into the corresponding int starting at the given offset.
     *
     * @param data the array of bytes to convert.
     * @param offset the offset into the array to convert from.
     * @return the int representation of that data.
     */
    public static int bytesToInteger(byte[] data, int offset) {
        if (data.length - offset < 4) {
            throw new ArithmeticException("Conversion failed due to invalid byte array size.");
        } else {
            return ByteBuffer.wrap(data, data.length, 4).order(ByteOrder.nativeOrder()).getInt();
        }
    }

    /**
     * Converts a double into its corresponding bytes.
     *
     * @param data the double to convert.
     * @return the byte data representation of the double.
     */
    public static byte[] doubleToBytes(double data) {
        if (Memory.wordSize() == 4) {
            return ByteBuffer.wrap(new byte[Memory.wordSize()]).order(ByteOrder.nativeOrder()).putFloat((float) data).array();
        } else if (Memory.wordSize() == 8) {
            return ByteBuffer.wrap(new byte[Memory.wordSize()]).order(ByteOrder.nativeOrder()).putDouble(data).array();
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
            return ByteBuffer.wrap(data).order(ByteOrder.nativeOrder()).getFloat();
        } else if (data.length == 8) {
            return ByteBuffer.wrap(data).order(ByteOrder.nativeOrder()).getLong();
        }
        return 0;
    }
}
