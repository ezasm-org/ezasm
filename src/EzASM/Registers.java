package EzASM;

import java.util.HashMap;
import java.util.Map;

public class Registers {

    private static final int REGISTERS_COUNT = 32;
    private static final int FP_REGISTERS_COUNT = 22;

    private final Register[] registers;
    private final Register[] fp_registers;

    // Base registers
    public static final String ZERO = "0";
    public static final String PC = "PC";
    public static final String SP = "SP";
    public static final String RA = "RA";
    public static final String ARG1 = "ARG1";
    public static final String ARG2 = "ARG2";
    public static final String ARG3 = "ARG3";
    public static final String RETURN1 = "R1";
    public static final String RETURN2 = "R2";
    public static final String RETURN3 = "R3";

    public static final String S0 = "S0";
    public static final String S1 = "S1";
    public static final String S2 = "S2";
    public static final String S3 = "S3";
    public static final String S4 = "S4";
    public static final String S5 = "S5";
    public static final String S6 = "S6";
    public static final String S7 = "S7";
    public static final String S8 = "S8";
    public static final String S9 = "S9";

    public static final String T0 = "T0";
    public static final String T1 = "T1";
    public static final String T2 = "T2";
    public static final String T3 = "T3";
    public static final String T4 = "T4";
    public static final String T5 = "T5";
    public static final String T6 = "T6";
    public static final String T7 = "T7";
    public static final String T8 = "T8";
    public static final String T9 = "T9";

    public static final String LO = "LO";
    public static final String HI = "HI";

    // Floating point registers
    public static final String FS0 = "FS0";
    public static final String FS1 = "FS1";
    public static final String FS2 = "FS2";
    public static final String FS3 = "FS3";
    public static final String FS4 = "FS4";
    public static final String FS5 = "FS5";
    public static final String FS6 = "FS6";
    public static final String FS7 = "FS7";
    public static final String FS8 = "FS8";
    public static final String FS9 = "FS9";

    public static final String FT0 = "FT0";
    public static final String FT1 = "FT1";
    public static final String FT2 = "FT2";
    public static final String FT3 = "FT3";
    public static final String FT4 = "FT4";
    public static final String FT5 = "FT5";
    public static final String FT6 = "FT6";
    public static final String FT7 = "FT7";
    public static final String FT8 = "FT8";
    public static final String FT9 = "FT9";

    public static final String FLO = "FLO";
    public static final String FHI = "FHI";

    private static Map<String, Register> reg = new HashMap<>();

    private static void init() {
        reg = new HashMap<>();
        reg.put(ZERO, new Register(0));
        reg.put(PC, new Register(1));
        reg.put(SP, new Register(2));
        reg.put(RA, new Register(3));
        reg.put(ARG1, new Register(4));
        reg.put(ARG2, new Register(5));
        reg.put(ARG3, new Register(6));
        reg.put(RETURN1, new Register(7));
        reg.put(RETURN2, new Register(8));
        reg.put(RETURN3, new Register(9));
        reg.put(S0, new Register(10));
        reg.put(S1, new Register(11));
        reg.put(S2, new Register(12));
        reg.put(S3, new Register(13));
        reg.put(S4, new Register(14));
        reg.put(S5, new Register(15));
        reg.put(S6, new Register(16));
        reg.put(S0, new Register(17));
        reg.put(S0, new Register(18));
        reg.put(S0, new Register(19));

    }

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
