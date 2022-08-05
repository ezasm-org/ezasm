package EzASM;

import java.nio.ByteBuffer;

public class Conversion {

    private static final ByteBuffer longBuffer = ByteBuffer.allocate(Long.BYTES);

    public static byte[] longToBytes(long data) {
        longBuffer.putLong(data);
        byte[] out = longBuffer.array();
        longBuffer.clear();
        return out;
    }

    public static long bytesToLong(byte[] data) {
        longBuffer.put(data);
        longBuffer.flip();//need flip
        long out = longBuffer.getLong();
        longBuffer.clear();
        return out;
    }

    public static byte[] doubleToBytes(double data) {
        longBuffer.putDouble(data);
        byte[] out = longBuffer.array();
        longBuffer.clear();
        return out;
    }

    public static double bytesToDouble(byte[] data) {
        longBuffer.put(data);
        longBuffer.flip();//need flip
        double out = longBuffer.getDouble();
        longBuffer.clear();
        return out;
    }

    public static byte[] stringToBytes(String data) {
        return data.getBytes();
    }

    public static String bytesToString(byte[] data) {
        return new String(data);
    }

}
