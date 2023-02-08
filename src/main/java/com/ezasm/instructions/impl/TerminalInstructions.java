package com.ezasm.instructions.impl;

import java.util.Scanner;

import com.ezasm.Conversion;
import com.ezasm.instructions.Instruction;
import com.ezasm.instructions.targets.input.IAbstractInput;
import com.ezasm.instructions.targets.output.IAbstractOutput;
import com.ezasm.simulation.Simulator;
import com.ezasm.simulation.exception.SimulationException;

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

    @Instruction
    public void printi(IAbstractInput input) throws SimulationException {
        byte[] bytes = input.get(simulator);
        long out = Conversion.bytesToLong(bytes);
        System.out.print(out);
    }

    @Instruction
    public void printf(IAbstractInput input) throws SimulationException {
        byte[] bytes = input.get(simulator);
        double out = Conversion.bytesToDouble(bytes);
        System.out.print(out);
    }

    @Instruction
    public void printc(IAbstractInput input) throws SimulationException {
        byte[] bytes = input.get(simulator);
        char out = (char) Conversion.bytesToLong(bytes);
        System.out.print(out);
    }

    @Instruction
    public void prints(IAbstractInput input, IAbstractInput input2) throws SimulationException {
        byte[] bytes = input.get(simulator);
        int addr = (int) Conversion.bytesToLong(bytes);
        bytes = input2.get(simulator);
        int maxSize = (int) Conversion.bytesToLong(bytes);
        String s = simulator.getMemory().readString(addr, maxSize);
        System.out.print(s);
    }

    @Instruction
    public void readi(IAbstractOutput output) throws SimulationException {
        byte[] result = Conversion.longToBytes(stdin.nextLong());
        output.set(simulator, result);
    }

    @Instruction
    public void readf(IAbstractOutput output) throws SimulationException {
        byte[] result = Conversion.doubleToBytes(stdin.nextDouble());
        output.set(simulator, result);
    }

    @Instruction
    public void readc(IAbstractOutput output) throws SimulationException {
        char c = stdin.next().charAt(0);
        byte[] result = Conversion.longToBytes(c);
        output.set(simulator, result);
    }

    @Instruction
    public void reads(IAbstractInput input1, IAbstractInput input2) throws SimulationException {
        int maxSize = (int) Conversion.bytesToLong(input2.get(simulator));
        String result = stdin.next();
        int address = (int) Conversion.bytesToLong(input1.get(simulator));
        simulator.getMemory().writeString(address, result, maxSize);
    }
}
