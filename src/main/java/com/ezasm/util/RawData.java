package com.ezasm.util;

import java.util.Arrays;

/**
 * Represents raw byte data. Allows for simpler conversions to and from, as well as handling.
 */
public final class RawData {

    private final byte[] data;

    public static RawData emptyBytes(int size) {
        return new RawData(new byte[size]);
    }

    public RawData(byte[] data) {
        this.data = data;
    }

    public RawData(long l) {
        data = Conversion.longToBytes(l);
    }

    public RawData(double d) {
        data = Conversion.doubleToBytes(d);
    }

    public long intValue() {
        return Conversion.bytesToLong(data);
    }

    public double floatValue() {
        return Conversion.bytesToDouble(data);
    }

    public byte[] data() {
        return data;
    }

    public RawData copy() {
        return new RawData(Arrays.copyOf(data, data.length));
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
