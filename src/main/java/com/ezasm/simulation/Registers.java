package com.ezasm.simulation;

import java.util.*;

/**
 * Represents all system registers within an array. Provides access to them by name and by reference number.
 */
public class Registers {

    private static int REGISTERS_COUNT = 54;

    private final Register[] registers;

    // Base registers
    public static final String ZERO = "ZERO"; // The number zero
    public static final String PID = "PID"; // Program identifier
    public static final String FID = "FID"; // File Identifier
    public static final String PC = "PC"; // Program counter
    public static final String SP = "SP"; // Stack pointer
    public static final String RA = "RA"; // Return address
    public static final String A0 = "A0"; // Argument 0
    public static final String A1 = "A1"; // Argument 1
    public static final String A2 = "A2"; // Argument 2
    public static final String R0 = "R0"; // Return 0
    public static final String R1 = "R1"; // Return 1
    public static final String R2 = "R2"; // Return 2

    // Saved registers
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

    // Temporary registers
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

    // Saved float registers
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

    // Temporary float registers
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

    public static final String LO = "LO"; // Special "LOW" register to store the lower part of a multiplication
    public static final String HI = "HI"; // Special "HIGH" register to store the higher part of a multiplication

    public static Map<String, Integer> registerByString;
    public static Map<Integer, String> registerByInt;

    /*
     * Initializes the registers and mappings in a static context whenever this code is loaded. This prevents the
     * register mappings from ever being null.
     */
    static {
        initialize();
        REGISTERS_COUNT = registerByInt.size();
    }

    /**
     * Initialization function for the registers. Creates both mapping of register number to String and vice-versa.
     */
    private static void initialize() {
        registerByString = new HashMap<>();
        registerByInt = new HashMap<>();
        addRegisters(ZERO, PID, FID, PC, SP, RA, A0, A1, A2, R0, R1, R2); // Add base registers
        addRegisters(LO, HI); // Add special registers
        addRegisters(S0, S1, S2, S3, S4, S5, S6, S7, S8, S9); // Add saved registers
        addRegisters(T0, T1, T2, T3, T4, T5, T6, T7, T8, T9); // Add temporary registers
        addRegisters(FS0, FS1, FS2, FS3, FS4, FS5, FS6, FS7, FS8, FS9); // Add float saved registers
        addRegisters(FT0, FT1, FT2, FT3, FT4, FT5, FT6, FT7, FT8, FT9); // Add float temporary registers
    }

    /**
     * Adds a given register to the static register mappings.
     *
     * @param names the names of all the register to add.
     */
    private static void addRegisters(String... names) {
        int base = registerByInt.size();
        for (int i = base; i < base + names.length; ++i) {
            registerByString.put(names[i - base].toLowerCase(), i);
            registerByInt.put(i, names[i - base].toLowerCase());
        }
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
            System.out.println(register);
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
        registers = new Register[REGISTERS_COUNT];
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
        if (register < 0 || register > REGISTERS_COUNT) {
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
