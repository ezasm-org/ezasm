package com.ezasm.simulation.transform.transformable;

import com.ezasm.simulation.ISimulator;
import com.ezasm.simulation.exception.SimulationException;
import com.ezasm.simulation.transform.Transformation;
import com.ezasm.util.RawData;

public class HeapPointerTransformation extends AbstractTransformableInput {

    public HeapPointerTransformation(ISimulator simulator) {
        super(simulator);
    }

    @Override
    public RawData get() throws SimulationException {
        return new RawData(simulator.getMemory().currentHeapPointer());
    }

    @Override
    public void set(RawData value) throws SimulationException {
        simulator.getMemory().setHeapPointer((int) value.intValue());
    }

    @Override
    public Transformation transformation(RawData value) throws SimulationException {
        return new Transformation(this, get(), value);
    }

}
