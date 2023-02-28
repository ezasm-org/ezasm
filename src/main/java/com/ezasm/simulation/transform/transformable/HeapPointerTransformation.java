package com.ezasm.simulation.transform.transformable;

import com.ezasm.simulation.ISimulator;
import com.ezasm.simulation.exception.SimulationException;
import com.ezasm.util.RawData;

/**
 * Represents the transformable internal memory heap pointer.
 */
public class HeapPointerTransformation extends AbstractTransformableInput {

    /**
     * Returns a representation of the transformable internal memory heap pointer.
     *
     * @param simulator the simulator to use.
     */
    public HeapPointerTransformation(ISimulator simulator) {
        super(simulator);
    }

    /**
     * Gets the heap pointer's value as raw bytes.
     *
     * @return the heap pointer's value as raw bytes.
     */
    @Override
    public RawData get() {
        return new RawData(simulator.getMemory().currentHeapPointer());
    }

    /**
     * Sets the heap pointer's value.
     *
     * @param value the heap pointer's new value.
     */
    @Override
    public void set(RawData value) {
        simulator.getMemory().setHeapPointer((int) value.intValue());
    }
}
