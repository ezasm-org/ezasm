package EzASM;

public class Register {

    private final long number;
    private final byte[] data;

    public Register(long number, int wordSize) {
        this.number = number;
        this.data = new byte[wordSize];
    }

    public long getNumber() {
        return number;
    }

    public byte[] getBytes() {
        return data;
    }

    public long getLong() {
        return Conversion.bytesToLong(data);
    }

    public double getDouble() {
        return Conversion.bytesToDouble(data);
    }

    public void setBytes(byte[] data) {
        if(number != 0) System.arraycopy(data, 0, this.data, 0, this.data.length);
    }

    public void setLong(long data) {
        if(number != 0) setBytes(Conversion.longToBytes(data));
    }

    public void setDouble(double data) {
        if(number != 0) setBytes(Conversion.doubleToBytes(data));
    }

    @Override
    public String toString() {
        return String.format("%2d: 0x%016X", number, getLong());
    }

    public String toDecimalString() {
        return String.format("%2d: %d", number, getLong());
    }
}
