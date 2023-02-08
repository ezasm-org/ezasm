package com.ezasm.instructions.impl;

import java.util.Scanner;

import com.ezasm.Conversion;
import com.ezasm.instructions.Instruction;
import com.ezasm.instructions.targets.input.IAbstractInput;
import com.ezasm.instructions.targets.output.IAbstractOutput;
import com.ezasm.simulation.Simulator;

/**
 * An implementatino of standard terminal I/O instructions for simulation.
 */
public class TerminalInstructions {
    private final Simulator simulator;
    private Scanner stdin; // TODO figure out a good way to close stdin when the whole app closes

    public TerminalInstructions(Simulator sim) {
        simulator = sim;
        stdin = new Scanner(System.in);
    }

    /**
     * Print a given abstractinput as an Integer
     *
     * @param input the input to print
     */
    @Instruction
    public void printi(IAbstractInput input) {
        byte[] bytes = input.get(simulator);
        long out = Conversion.bytesToLong(bytes);
        System.out.print(out);
    }

    /**
     * Print a given abstractinput as a Float
     *
     * @param input the input to print
     */
    @Instruction
    public void printf(IAbstractInput input) {
        byte[] bytes = input.get(simulator);
        double out = Conversion.bytesToDouble(bytes);
        System.out.print(out);
    }

    /**
     * Print a given abstractinput as a Character
     *
     * @param input the input to print
     */
    @Instruction
    public void printc(IAbstractInput input) {
        byte[] bytes = input.get(simulator);
        char out = (char) Conversion.bytesToLong(bytes);
        System.out.print(out);
    }

    /**
     * Print a given abstractinput as a String
     *
     * @param input the input to print
     */
    @Instruction
    public void prints(IAbstractInput input, IAbstractInput input2) {
        byte[] bytes = input.get(simulator);
        int addr = (int) Conversion.bytesToLong(bytes);
        bytes = input2.get(simulator);
        int maxSize = (int) Conversion.bytesToLong(bytes);
        String s = simulator.getMemory().readString(addr, maxSize);
        System.out.print(s);
    }

    /**
     * Read an integer from the terminal to a register
     *
     * @param input the input to print
     */
    @Instruction
    public void readi(IAbstractOutput output) {
        byte[] result = Conversion.longToBytes(stdin.nextLong());
        output.set(simulator, result);
    }

    /**
     * Read a float from the terminal to a register
     *
     * @param input the input to print
     */
    @Instruction
    public void readf(IAbstractOutput output) {
        byte[] result = Conversion.doubleToBytes(stdin.nextDouble());
        output.set(simulator, result);
    }

    /**
     * Read a character from the terminal to a register
     *
     * @param input the input to print
     */
    @Instruction
    public void readc(IAbstractOutput output) {
        char c = stdin.next().charAt(0);
        byte[] result = Conversion.longToBytes(c);
        output.set(simulator, result);
    }

    /**
     * Read a string from the terminal of a given maximum size and write it to memory
     *
     * @param input1 the address to write the string to
     * @param input2 maximum size of the string to read
     */
    @Instruction
    public void reads(IAbstractInput input1, IAbstractInput input2) {
        int maxSize = (int) Conversion.bytesToLong(input2.get(simulator));
        String result = stdin.next();
        int address = (int) Conversion.bytesToLong(input1.get(simulator));
        simulator.getMemory().writeString(address, result, maxSize);
    }
}
