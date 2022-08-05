package EzASM;

public class Register {

    private final int size;
    private final byte[] data;

    public Register() {
        this.size = Memory.WORD_SIZE;
        this.data = new byte[this.size];
    }

    public Register(int size) {
        this.size = size;
        this.data = new byte[this.size];
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
        System.arraycopy(data, 0, this.data, 0, Memory.WORD_SIZE);
    }

    public void setLong(long data) {
        setBytes(Conversion.longToBytes(data));
    }

    public void setDouble(double data) {
        setBytes(Conversion.doubleToBytes(data));
    }

}
