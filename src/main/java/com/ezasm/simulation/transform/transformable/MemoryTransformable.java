package com.ezasm.simulation.transform.transformable;

import com.ezasm.simulation.ISimulator;
import com.ezasm.simulation.exception.SimulationException;
import com.ezasm.simulation.transform.Transformation;
import com.ezasm.util.RawData;

public class MemoryTransformable extends AbstractTransformableInput {

    private final long address;

    public MemoryTransformable(ISimulator simulator, long address) {
        super(simulator);
        this.address = address;
    }

    @Override
    public RawData get() throws SimulationException {
        return simulator.getMemory().read((int) address);
    }

    @Override
    public void set(RawData value) throws SimulationException {
        simulator.getMemory().write((int) address, value);
    }

    @Override
    public Transformation transformation(RawData value) throws SimulationException {
        return new Transformation(this, get(), value);
    }
}
