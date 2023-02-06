package com.ezasm.instructions.impl;

import java.util.Scanner;

import com.ezasm.Conversion;
import com.ezasm.instructions.Instruction;
import com.ezasm.instructions.targets.input.IAbstractInput;
import com.ezasm.instructions.targets.inputoutput.IAbstractInputOutput;
import com.ezasm.instructions.targets.output.IAbstractOutput;
import com.ezasm.simulation.Simulator;

/**
 * An implementatino of standard terminal I/O instructions for simulation.
 */
public class TerminalInstructions {
    private final Simulator simulator;

    public TerminalInstructions(Simulator sim) {
        simulator = sim;
    }

    @Instruction
    public void printi(IAbstractInput input) {
        byte[] bytes = input.get(simulator);
        long out = Conversion.bytesToLong(bytes);
        System.out.print(out);
    }

    @Instruction
    public void printf(IAbstractInput input) {
        byte[] bytes = input.get(simulator);
        double out = Conversion.bytesToDouble(bytes);
        System.out.print(out);
    }

    @Instruction
    public void printc(IAbstractInput input) {
        byte[] bytes = input.get(simulator);
        char out = (char) Conversion.bytesToLong(bytes);
        System.out.print(out);
    }

    @Instruction
    public void prints(IAbstractInput input, IAbstractInput input2) {
        byte[] bytes = input.get(simulator);
        int addr = (int) Conversion.bytesToLong(bytes);
        bytes = input2.get(simulator);
        int maxSize = (int) Conversion.bytesToLong(bytes);
        String s = simulator.getMemory().readString(addr, maxSize);
        System.out.print(s);
    }

    @Instruction
    public void readi(IAbstractOutput output) {
        Scanner stdin = new Scanner(System.in);
        byte[] result = Conversion.longToBytes(stdin.nextLong());
        output.set(simulator, result);
        stdin.close();
    }

    @Instruction
    public void readf(IAbstractOutput output) {
        Scanner stdin = new Scanner(System.in);
        byte[] result = Conversion.doubleToBytes(stdin.nextDouble());
        output.set(simulator, result);
        stdin.close();
    }

    @Instruction
    public void readc(IAbstractOutput output) {
        Scanner stdin = new Scanner(System.in);
        byte[] result = Conversion.longToBytes(stdin.nextByte());
        output.set(simulator, result);
        stdin.close();
    }

    @Instruction
    public void reads(IAbstractInput input1, IAbstractInput input2) {
        Scanner stdin = new Scanner(System.in);
        int maxSize = (int) Conversion.bytesToLong(input2.get(simulator));
        String result = stdin.next();
        int address = (int) Conversion.bytesToLong(input1.get(simulator));
        simulator.getMemory().writeString(address, result, maxSize);
        stdin.close();
    }
}
