package com.ezasm.util;

import static org.junit.jupiter.api.Assertions.assertEquals;
import org.junit.jupiter.api.Test;

class ConversionTest {

    @Test
    void longToBytes() {
        long value1 = 1L;
        byte[] data1 = Conversion.longToBytes(value1);
        assertEquals(4, data1.length);
    }

    @Test
    void bytesToLong() {
        byte[] data = { 0, 0, 0, 0, };
        long value = Conversion.bytesToLong(data);
        assertEquals(0L, value);
    }

    @Test
    void doubleToBytes() {
        double value = 1.0;
        byte[] data = Conversion.doubleToBytes(value);
        assertEquals(4, data.length);
    }

    @Test
    void bytesToDouble() {
        byte[] data = { 0, 0, 0, 0, };
        double value = Conversion.bytesToDouble(data);
        assertEquals(0.0, value);
    }
}
