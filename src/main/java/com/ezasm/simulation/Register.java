package com.ezasm.simulation;

import com.ezasm.gui.Window;
import com.ezasm.util.Conversion;
import com.ezasm.util.RawData;

/**
 * The representation of an individual register within the system's registers. Stores the register's own reference
 * number and the data corresponding to it.
 */
public class Register {

    private final long number;
    private final RawData data;

    /**
     * Constructs a register given a reference number and the system word size.
     *
     * @param number   the register's reference number.
     * @param wordSize the system word size in bytes.
     */
    public Register(long number, int wordSize) {
        this.number = number;
        this.data = RawData.emptyBytes(wordSize);
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
    public RawData getData() {
        return data.copy();
    }

    /**
     * Gets the long interpretation of the data stored within the register.
     *
     * @return the long interpretation of the data stored within the register.
     */
    public long getLong() {
        return data.intValue();
    }

    /**
     * Gets the double interpretation of the data stored within the register.
     *
     * @return the double interpretation of the data stored within the register.
     */
    public double getDouble() {
        return data.floatValue();
    }

    /**
     * Writes the data within the given array to the register.
     *
     * @param data the new data to write.
     */
    public void setData(RawData data) {
        if (number != 0) {
            setData(data.data());
        }
    }

    /**
     * Writes the data within the given array to the register. Also performs any necessary corresponding GUI updates.
     *
     * @param data the new data to write.
     */
    public void setDataWithGuiCallback(RawData data) {
        setData(data);
        if (Window.hasInstance()) { // GUI callback
            Window.getInstance().getRegisterTable().addHighlightValue((int) this.number);
        }
    }

    public void setData(byte[] data) {
        if (number != 0)
            System.arraycopy(data, 0, this.data.data(), 0, data.length);
    }

    /**
     * Writes the byte representation of the given long to the register.
     *
     * @param data the long to write.
     */
    public void setLong(long data) {
        if (number != 0) {
            setData(Conversion.longToBytes(data));
        }
    }

    /**
     * Writes the byte representation of the given double to the register.
     *
     * @param data the double to write.
     */
    public void setDouble(double data) {
        if (number != 0) {
            setData(Conversion.doubleToBytes(data));
        }
    }

    /**
     * Gets a String containing the register number and the data it represents as a hexadecimal number.
     *
     * @return a String containing the register number and the data it represents as a hexadecimal number.
     */
    @Override
    public String toString() {
        return String.format("%2d: 0x%08X", number, getLong());
    }

    /**
     * Gets a String containing the register number and the data it represents as a decimal number.
     *
     * @return a String containing the register number and the data it represents as a decimal number.
     */
    public String toDecimalString() {
        return String.format("%2d: %d", number, getLong());
    }
}
