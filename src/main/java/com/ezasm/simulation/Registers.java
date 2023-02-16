package com.ezasm.simulation;

import java.util.*;

/**
 * Represents all system registers within an array. Provides access to them by name and by reference number.
 */
public class Registers {

    private static final int REGISTERS_COUNT = 32;
    private static final int FP_REGISTERS_COUNT = 22;
    private static final int TOTAL_REGISTERS = REGISTERS_COUNT + FP_REGISTERS_COUNT;

    private final Register[] registers;

    // Base registers
    public static final String ZERO = "ZERO";
    public static final String PC = "PC";
    public static final String SP = "SP";
    public static final String RA = "RA";
    public static final String A0 = "A0";
    public static final String A1 = "A1";
    public static final String A2 = "A2";
    public static final String R0 = "R0";
    public static final String R1 = "R1";
    public static final String R2 = "R2";

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

    public static Map<String, Integer> registerByString;
    public static Map<Integer, String> registerByInt;
    private static final int FLOAT_OFFSET = REGISTERS_COUNT;

    /**
     * Initialization function for the registers. Creates both mapping of register number to String and vice-versa.
     */
    private static void initialize() {
        registerByString = new HashMap<>(TOTAL_REGISTERS);
        registerByInt = new HashMap<>(TOTAL_REGISTERS);
        addRegister(ZERO, 0);
        addRegister(PC, 1);
        addRegister(SP, 2);
        addRegister(RA, 3);
        addRegister(A0, 4);
        addRegister(A1, 5);
        addRegister(A2, 6);
        addRegister(R0, 7);
        addRegister(R1, 8);
        addRegister(R2, 9);

        addRegister(S0, 10);
        addRegister(S1, 11);
        addRegister(S2, 12);
        addRegister(S3, 13);
        addRegister(S4, 14);
        addRegister(S5, 15);
        addRegister(S6, 16);
        addRegister(S7, 17);
        addRegister(S8, 18);
        addRegister(S9, 19);

        addRegister(T0, 20);
        addRegister(T1, 21);
        addRegister(T2, 22);
        addRegister(T3, 23);
        addRegister(T4, 24);
        addRegister(T5, 25);
        addRegister(T6, 26);
        addRegister(T7, 27);
        addRegister(T8, 28);
        addRegister(T9, 29);

        addRegister(LO, 30);
        addRegister(HI, 31);

        addRegister(FS0, 0 + FLOAT_OFFSET);
        addRegister(FS1, 1 + FLOAT_OFFSET);
        addRegister(FS2, 2 + FLOAT_OFFSET);
        addRegister(FS3, 3 + FLOAT_OFFSET);
        addRegister(FS4, 4 + FLOAT_OFFSET);
        addRegister(FS5, 5 + FLOAT_OFFSET);
        addRegister(FS6, 6 + FLOAT_OFFSET);
        addRegister(FS7, 7 + FLOAT_OFFSET);
        addRegister(FS8, 8 + FLOAT_OFFSET);
        addRegister(FS9, 9 + FLOAT_OFFSET);

        addRegister(FT0, 10 + FLOAT_OFFSET);
        addRegister(FT1, 11 + FLOAT_OFFSET);
        addRegister(FT2, 12 + FLOAT_OFFSET);
        addRegister(FT3, 13 + FLOAT_OFFSET);
        addRegister(FT4, 14 + FLOAT_OFFSET);
        addRegister(FT5, 15 + FLOAT_OFFSET);
        addRegister(FT6, 16 + FLOAT_OFFSET);
        addRegister(FT7, 17 + FLOAT_OFFSET);
        addRegister(FT8, 18 + FLOAT_OFFSET);
        addRegister(FT9, 19 + FLOAT_OFFSET);

        addRegister(FLO, 20 + FLOAT_OFFSET);
        addRegister(FHI, 21 + FLOAT_OFFSET);
    }

    /*
     * Initializes the registers and mappings in a static context whenever this code is loaded. This prevents the
     * register mappings from ever being null.
     */
    static {
        initialize();
    }

    /**
     * Adds a given register to the static register mappings.
     *
     * @param name   the name of the register to add.
     * @param number the reference number of the register to add.
     */
    private static void addRegister(String name, int number) {
        assert number < TOTAL_REGISTERS && number >= 0;
        registerByString.put(name.toLowerCase(), number);
        registerByInt.put(number, name.toLowerCase());
    }

    /**
     * Checks if a given String matches any of the registers. Can be the named register notation or the numerical
     * register notation to check. The register attempt given should not have the '$' at the start of the String.
     *
     * @param register the String representing the register.
     * @return true if the register is valid, false otherwise.
     */
    public static boolean isRegister(String register) {
        if (register == null || register.length() < 1)
            return false;
        if (register.charAt(0) == '$')
            register = register.substring(1);
        if (registerByString.containsKey(register.toLowerCase()))
            return true;
        try {
            int attempt = Integer.parseInt(register);
            return isRegister(attempt);
        } catch (Exception ignored) {
        }
        return false;
    }

    /**
     * Checks if a given register reference number matches any of the registers.
     *
     * @param register the number reference of to check.
     * @return true if the register is valid, false otherwise.
     */
    public static boolean isRegister(int register) {
        return registerByInt.containsKey(register);
    }

    /**
     * Gets a register number based on the register's name or a text representation of its number.
     *
     * @param register the register to seek.
     * @return the register number found.
     */
    public static int getRegisterNumber(String register) {
        if (register.charAt(0) == '$')
            register = register.substring(1);
        register = register.toLowerCase();
        if (!isRegister(register)) {
            // TODO add appropriate exception
            throw new RuntimeException();
        }
        try {
            int attempt = Integer.parseInt(register);
            if (isRegister(attempt)) {
                return attempt;
            } else {
                // TODO add appropriate exception
                throw new RuntimeException();
            }
        } catch (Exception ignored) {
        }
        return registerByString.get(register);
    }

    /**
     * Gets a register's name based on its reference number.
     *
     * @param register the register reference number to search.
     * @return the register name found.
     */
    public static String getRegisterName(int register) {
        if (!isRegister(register)) {
            // TODO add appropriate exception
            throw new RuntimeException();
        }
        return registerByInt.get(register);
    }

    /**
     * Constructs the system registers based on a given word size.
     *
     * @param wordSize the given word size in bytes.
     */
    public Registers(int wordSize) {
        registers = new Register[TOTAL_REGISTERS];
        for (Integer i : registerByInt.keySet()) {
            registers[i] = new Register(i, wordSize);
        }
    }

    /**
     * Sets the values of all bytes of all registers to zero.
     */
    public void reset() {
        for (Integer i : registerByInt.keySet()) {
            registers[i].setLong(0);
        }
    }

    /**
     * Gets the register corresponding to the reference number.
     *
     * @param register the reference number of the register to get.
     * @return the register obtained.
     */
    public Register getRegister(int register) {
        if (register < 0 || register > TOTAL_REGISTERS) {
            // TODO add appropriate exception
            throw new RuntimeException();
        }
        return registers[register];
    }

    /**
     * Gets the register corresponding to the given name.
     *
     * @param register the name of the register to get.
     * @return the register obtained.
     */
    public Register getRegister(String register) {
        return getRegister(getRegisterNumber(register));
    }

    /**
     * Gets the list of all registers.
     *
     * @return the list of all registers.
     */
    public Register[] getRegisters() {
        return registers;
    }

    /**
     * Gets a string representing the current state of the system registers.
     *
     * @return a string representing the current state of the system registers.
     */
    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < registers.length - 1; ++i) {
            sb.append(registers[i]);
            if (i % 9 == 8) {
                sb.append('\n');
            } else {
                sb.append(' ');
            }
        }
        if (registers.length > 0) {
            sb.append(registers[registers.length - 1]);
        }
        return sb.toString();
    }
}
