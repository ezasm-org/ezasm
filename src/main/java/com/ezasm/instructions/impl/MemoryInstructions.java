package com.ezasm.instructions.impl;

import com.ezasm.instructions.Instruction;
import com.ezasm.instructions.targets.input.IAbstractInput;
import com.ezasm.instructions.targets.output.IAbstractOutput;
import com.ezasm.simulation.ISimulator;
import com.ezasm.simulation.Registers;
import com.ezasm.simulation.SimulationException;

public class MemoryInstructions {

    private final ISimulator simulator;

    /**
     * Some instructions require access to the Simulator directly, so that is provided.
     *
     * @param simulator the provided Simulator.
     */
    public MemoryInstructions(ISimulator simulator) {
        this.simulator = simulator;
    }

    @Instruction
    public void push(IAbstractInput input) throws SimulationException {
        long sp = simulator.getRegisters().getRegister(Registers.SP).getLong() - simulator.getMemory().WORD_SIZE;
        simulator.getRegisters().getRegister(Registers.SP).setLong(sp);
        simulator.getMemory().write((int) sp, input.get(simulator));
    }

    @Instruction
    public void pop(IAbstractOutput output) throws SimulationException {
        long sp = simulator.getRegisters().getRegister(Registers.SP).getLong();
        output.set(simulator, simulator.getMemory().read((int) sp, simulator.getMemory().WORD_SIZE));
        simulator.getRegisters().getRegister(Registers.SP).setLong(sp + simulator.getMemory().WORD_SIZE);
    }

}
