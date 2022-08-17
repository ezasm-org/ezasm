package EzASM;

import java.nio.ByteBuffer;

public class Conversion {

    public static byte[] longToBytes(long data) {
        return ByteBuffer.wrap(new byte[8]).putLong(data).array();
    }

    public static long bytesToLong(byte[] data) {
        return ByteBuffer.wrap(data).getLong();
    }

    public static byte[] doubleToBytes(double data) {
        return ByteBuffer.wrap(new byte[8]).putDouble(data).array();
    }

    public static double bytesToDouble(byte[] data) {
        return ByteBuffer.wrap(data).getDouble();
    }

    public static byte[] stringToBytes(String data) {
        return data.getBytes();
    }

    public static String bytesToString(byte[] data) {
        return new String(data);
    }

}
