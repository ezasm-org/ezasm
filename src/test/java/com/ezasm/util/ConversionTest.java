package com.ezasm.util;

import static org.junit.jupiter.api.Assertions.assertEquals;
import org.junit.jupiter.api.Test;

class ConversionTest {

    private final byte[] byte1 = { 0, 0, 0, 0 };
    private final byte[] byte2 = { 0, 0, 0, 1 };
    private final byte[] byte3 = { 7, 91, -51, 21 };

    private final long long1 = 0L;
    private final long long2 = 1L;
    private final long long3 = 123456789L;

    private final double double1 = 0.0;
    private final double double2 = 1.0;
    private final double double3 = 123456789.0;

    @Test
    void longToBytes() {
        byte[] data1 = Conversion.longToBytes(long1);
        byte[] data2 = Conversion.longToBytes(long2);
        byte[] data3 = Conversion.longToBytes(long3);
        for (byte b : data1)
            assertEquals(0, b);
        for (int i = 0; i < data2.length; i++) {
            assertEquals(byte2[i], data2[i]);
        }
        for (int i = 0; i < data3.length; i++) {
            assertEquals(byte3[i], data3[i]);
        }
    }

    @Test
    void bytesToLong() {
        long value1 = Conversion.bytesToLong(byte1);
        long value2 = Conversion.bytesToLong(byte2);
        long value3 = Conversion.bytesToLong(byte3);
        assertEquals(long1, value1);
        assertEquals(long2, value2);
        assertEquals(long3, value3);
    }

    @Test
    void doubleToBytes() {
        byte[] data1 = Conversion.doubleToBytes(double1);
        byte[] data2 = Conversion.doubleToBytes(double2);
        byte[] data3 = Conversion.doubleToBytes(double3);
        for (byte b : data1)
            assertEquals(0, b);
    }

    @Test
    void bytesToDouble() {
        double data1 = Conversion.bytesToDouble(byte1);
        double data2 = Conversion.bytesToDouble(byte2);
        double data3 = Conversion.bytesToDouble(byte3);
        assertEquals(double1, data1);
    }
}
