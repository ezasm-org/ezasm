package com.ezasm.instructions.impl;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;

import com.ezasm.instructions.Instruction;
import com.ezasm.instructions.targets.input.AbstractInput;
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
    public void printi(AbstractInput input) {
        byte[] b1 = input.get(simulator);
        int out = ByteBuffer.wrap(b1).order(ByteOrder.BIG_ENDIAN).getInt();
        System.out.print(out);
    }
}
