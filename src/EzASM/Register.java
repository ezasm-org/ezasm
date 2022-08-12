package EzASM;

public class Register {

    private final int number;
    private final int size;
    private final byte[] data;

    public Register(int number) {
        this.number = number;
        this.size = Memory.WORD_SIZE;
        this.data = new byte[this.size];
    }

    public Register(int number, int size) {
        this.number = number;
        this.size = size;
        this.data = new byte[this.size];
    }

    public int getNumber() {
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
        System.arraycopy(data, 0, this.data, 0, Memory.WORD_SIZE);
    }

    public void setLong(long data) {
        setBytes(Conversion.longToBytes(data));
    }

    public void setDouble(double data) {
        setBytes(Conversion.doubleToBytes(data));
    }

}
