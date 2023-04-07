package com.ezasm.util;

import com.ezasm.simulation.Memory;

import java.util.Arrays;

/**
 * Represents raw byte data. Allows for simpler conversions to and from, as well as handling. This implementation is to
 * replace the act of passing a byte array around.
 */
public final class RawData {

    private final byte[] data;

    /**
     * Creates a data storage object of the given size.
     *
     * @param size the number of bytes to represent.
     * @return the newly constructed data storage object.
     */
    public static RawData emptyBytes(int size) {
        return new RawData(new byte[size]);
    }

    /**
     * Construct an instance of the data representation given a raw byte array.
     *
     * @param data the raw byte array to use.
     */
    public RawData(byte[] data) {
        this.data = data;
    }

    /**
     * Construct an instance of the data representation given an integer to store in it.
     *
     * @param l the integer to base the data on.
     */
    public RawData(long l) {
        data = Conversion.longToBytes(l);
    }

    /**
     * Construct an instance of the data representation given float to store in it.
     *
     * @param d the float to base the data on.
     */
    public RawData(double d) {
        data = Conversion.doubleToBytes(d);
    }

    /**
     * Reads the data in this object as a long integer.
     *
     * @return the long representing the data inside this.
     */
    public long intValue() {
        return Conversion.bytesToLong(data);
    }

    /**
     * Reads the data in this object as a double size float.
     *
     * @return the float representing the data inside this.
     */
    public double floatValue() {
        return Conversion.bytesToDouble(data);
    }

    /**
     * Retrieve the representation of the data itself. This should only be used by internal simulator functions.
     *
     * @return the representation of the data.
     */
    public byte[] data() {
        return data;
    }

    /**
     * Returns a copy of this object such that this.equals(this.copy()) but this != this.copy().
     *
     * @return the copy of this object.
     */
    public RawData copy() {
        return new RawData(Arrays.copyOf(data, data.length));
    }

    /**
     * Converts the integer value contained in the data to a 0-padded hexadecimal string that will be (5 * memory size /
     * 4 + 3) characters long. The value representing 31 in an 8-byte word would turn into 0x0000_0000_0000_001F.
     *
     * @return the hexadecimal formatted string.
     */
    public String toHexString() {
        String output = String.format("%0" + (Memory.wordSize() * 2) + 'x', intValue()).toUpperCase();
        StringBuilder sb = new StringBuilder("0x");
        for (int i = 0; i < output.length(); ++i) {
            sb.append(output.charAt(i));
            if (i % 4 == 3 && i != output.length() - 1) {
                sb.append('_');
            }
        }
        return sb.toString();
    }

    @Override
    public String toString() {
        return Long.toString(intValue());
    }

    @Override
    public boolean equals(Object o) {
        if (this == o)
            return true;
        if (o == null || getClass() != o.getClass())
            return false;
        RawData rawData = (RawData) o;
        return Arrays.equals(data, rawData.data);
    }

    @Override
    public int hashCode() {
        return Arrays.hashCode(data);
    }
}
