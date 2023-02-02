package com.ezasm.simulation;

import com.ezasm.Conversion;

import java.util.Arrays;

/**
 * The representation of an individual register within the system's registers.
 * Stores the register's own reference number and the data corresponding to it.
 */
public class Register {

    private final long number;
    private final byte[] data;

    /**
     * Constructs a register given a reference number and the system word size.
     *
     * @param number   the register's reference number.
     * @param wordSize the system word size in bytes.
     */
    public Register(long number, int wordSize) {
        this.number = number;
        this.data = new byte[wordSize];
    }

    /**
     * Gets the register's reference number.
     *
     * @return the register's reference number.
     */
    public long getNumber() {
        return number;
    }

    /**
     * Gets a copy of the bytes stored in the register.
     *
     * @return a copy of the bytes stored in the register.
     */
    public byte[] getBytes() {
        return Arrays.copyOf(data, data.length);
    }

    /**
     * Gets the long interpretation of the data stored within the register.
     *
     * @return the long interpretation of the data stored within the register.
     */
    public long getLong() {
        return Conversion.bytesToLong(data);
    }

    /**
     * Gets the double interpretation of the data stored within the register.
     *
     * @return the double interpretation of the data stored within the register.
     */
    public double getDouble() {
        return Conversion.bytesToDouble(data);
    }

    /**
     * Writes the data within the given array to the register.
     *
     * @param data the new data to write.
     */
    public void setBytes(byte[] data) {
        if (number != 0)
            System.arraycopy(data, 0, this.data, 0, this.data.length);
    }

    /**
     * Writes the byte representation of the given long to the register.
     *
     * @param data the long to write.
     */
    public void setLong(long data) {
        if (number != 0)
            setBytes(Conversion.longToBytes(data));
    }

    /**
     * Writes the byte representation of the given double to the register.
     *
     * @param data the double to write.
     */
    public void setDouble(double data) {
        if (number != 0)
            setBytes(Conversion.doubleToBytes(data));
    }

    /**
     * Gets a String containing the register number and the data it represents as a
     * hexadecimal number.
     *
     * @return a String containing the register number and the data it represents as
     *         a hexadecimal number.
     */
    @Override
    public String toString() {
        return String.format("%2d: 0x%016X", number, getLong());
    }

    /**
     * Gets a String containing the register number and the data it represents as a
     * decimal number.
     *
     * @return a String containing the register number and the data it represents as
     *         a decimal number.
     */
    public String toDecimalString() {
        return String.format("%2d: %d", number, getLong());
    }
}
