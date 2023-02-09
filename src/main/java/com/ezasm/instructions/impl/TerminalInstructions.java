package com.ezasm.instructions.impl;

import java.util.Scanner;

import com.ezasm.Conversion;
import com.ezasm.instructions.Instruction;
import com.ezasm.instructions.targets.input.IAbstractInput;
import com.ezasm.instructions.targets.output.IAbstractOutput;
import com.ezasm.simulation.ISimulator;
import com.ezasm.simulation.exception.SimulationException;

/**
 * An implementation of standard terminal I/O instructions for simulation.
 */
public class TerminalInstructions {
    private final ISimulator simulator;
    private Scanner stdin;

    public TerminalInstructions(ISimulator simulator) {
        this.simulator = simulator;
        stdin = new Scanner(System.in);
    }

    @Instruction
    public void printi(IAbstractInput input) throws SimulationException {
        System.out.print(Conversion.bytesToLong(input.get(simulator)));
    }

    @Instruction
    public void printf(IAbstractInput input) throws SimulationException {
        System.out.print(Conversion.bytesToDouble(input.get(simulator)));
    }

    @Instruction
    public void printc(IAbstractInput input) throws SimulationException {

        System.out.print((char) Conversion.bytesToLong(input.get(simulator)));
    }

    @Instruction
    public void prints(IAbstractInput input1, IAbstractInput input2) throws SimulationException {
        int address = (int) Conversion.bytesToLong(input1.get(simulator));
        int maxSize = (int) Conversion.bytesToLong(input2.get(simulator));
        String s = simulator.getMemory().readString(address, maxSize);
        System.out.print(s);
    }

    @Instruction
    public void readi(IAbstractOutput output) throws SimulationException {
        output.set(simulator, Conversion.longToBytes(stdin.nextLong()));
    }

    @Instruction
    public void readf(IAbstractOutput output) throws SimulationException {
        output.set(simulator, Conversion.doubleToBytes(stdin.nextDouble()));
    }

    @Instruction
    public void readc(IAbstractOutput output) throws SimulationException {
        output.set(simulator, Conversion.longToBytes(stdin.next().charAt(0)));
    }

    @Instruction
    public void reads(IAbstractInput input1, IAbstractInput input2) throws SimulationException {
        int maxSize = (int) Conversion.bytesToLong(input2.get(simulator));
        int address = (int) Conversion.bytesToLong(input1.get(simulator));
        simulator.getMemory().writeString(address, stdin.next(), maxSize);
    }
}
