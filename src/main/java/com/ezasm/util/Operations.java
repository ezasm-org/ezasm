package com.ezasm.util;

import java.util.function.BinaryOperator;

public class Operations {

    public static byte[] applyToIntegerBytes(BinaryOperator<Long> op, byte[] input1, byte[] input2) {
        return Conversion.longToBytes(op.apply(Conversion.bytesToLong(input1), Conversion.bytesToLong(input2)));
    }

    public static byte[] applyToFloatBytes(BinaryOperator<Double> op, byte[] input1, byte[] input2) {
        return Conversion.doubleToBytes(op.apply(Conversion.bytesToDouble(input1), Conversion.bytesToDouble(input2)));
    }

    public static byte[] addIntegerBytes(byte[] input1, byte[] input2) {
        return applyToIntegerBytes(Long::sum, input1, input2);
    }

    public static byte[] addFloatBytes(byte[] input1, byte[] input2) {
        return applyToFloatBytes(Double::sum, input1, input2);
    }

}
