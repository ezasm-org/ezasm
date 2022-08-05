package EzASM;

public class Registers {

    private static final int REGISTERS_COUNT = 32;
    private static final int FP_REGISTERS_COUNT = 22;

    private final Register[] registers;
    private final Register[] fp_registers;

    // Base registers
    public static final int ZERO = 0;
    public static final int PC = 1;
    public static final int SP = 2;
    public static final int RA = 3;
    public static final int ARG1 = 4;
    public static final int ARG2 = 5;
    public static final int ARG3 = 6;
    public static final int RETURN1 = 7;
    public static final int RETURN2 = 8;
    public static final int RETURN3 = 9;

    public static final int S0 = 10;
    public static final int S1 = 11;
    public static final int S2 = 12;
    public static final int S3 = 13;
    public static final int S4 = 14;
    public static final int S5 = 15;
    public static final int S6 = 16;
    public static final int S7 = 17;
    public static final int S8 = 18;
    public static final int S9 = 19;

    public static final int T0 = 20;
    public static final int T1 = 21;
    public static final int T2 = 22;
    public static final int T3 = 23;
    public static final int T4 = 24;
    public static final int T5 = 25;
    public static final int T6 = 26;
    public static final int T7 = 27;
    public static final int T8 = 28;
    public static final int T9 = 29;

    public static final int LO = 30;
    public static final int HI = 31;

    // Floating point registers
    public static final int FS0 = 0;
    public static final int FS1 = 1;
    public static final int FS2 = 2;
    public static final int FS3 = 3;
    public static final int FS4 = 4;
    public static final int FS5 = 5;
    public static final int FS6 = 6;
    public static final int FS7 = 7;
    public static final int FS8 = 8;
    public static final int FS9 = 9;

    public static final int FT0 = 10;
    public static final int FT1 = 11;
    public static final int FT2 = 12;
    public static final int FT3 = 13;
    public static final int FT4 = 14;
    public static final int FT5 = 15;
    public static final int FT6 = 16;
    public static final int FT7 = 17;
    public static final int FT8 = 18;
    public static final int FT9 = 19;

    public static final int FLO = 20;
    public static final int FHI = 21;


    public Registers() {
        registers = new Register[REGISTERS_COUNT];
        fp_registers = new Register[FP_REGISTERS_COUNT];
    }

    public byte[] getBytes(int register) {
        if(register < 0 || register > REGISTERS_COUNT) {
            // Error: no such register
            return null;
        }
        return registers[register].getBytes();
    }

    public long getLong(int register) {
        if(register < 0 || register > REGISTERS_COUNT) {
            // Error: no such register
            return 0;
        }
        return registers[register].getLong();
    }

    public double getDouble(int register) {
        if(register < 0 || register > FP_REGISTERS_COUNT) {
            // Error: no such register
            return 0;
        }
        return fp_registers[register].getDouble();
    }

    public void setBytes(int register, byte[] data) {
        if(register < 0 || register > REGISTERS_COUNT) {
            // Error: no such register
            return;
        }
        registers[register].setBytes(data);
    }

    public void setLong(int register, long data) {
        if(register < 0 || register > REGISTERS_COUNT) {
            // Error: no such register
            return;
        }
        registers[register].setLong(data);
    }

    public void setDouble(int register, double data) {
        if(register < 0 || register > FP_REGISTERS_COUNT) {
            // Error: no such register
            return;
        }
        fp_registers[register].setDouble(data);
    }

}
