package com.ezasm.instructions.targets.inputoutput.mock;

import com.ezasm.instructions.targets.inputoutput.IAbstractInputOutput;
import com.ezasm.simulation.ISimulator;
import com.ezasm.simulation.Transformation;
import com.ezasm.simulation.exception.SimulationException;

public class MemoryInputOutput implements IAbstractInputOutput {

    private final long address;

    public MemoryInputOutput(long address) {
        this.address = address;
    }

    @Override
    public byte[] get(ISimulator simulator) throws SimulationException {
        return simulator.getMemory().read((int) address);
    }

    @Override
    public void set(ISimulator simulator, byte[] value) throws SimulationException {
        simulator.getMemory().write((int) address, value);
    }

    @Override
    public Transformation transformation(ISimulator simulator, byte[] value) throws SimulationException {
        return new Transformation(this, get(simulator), value);
    }
}
