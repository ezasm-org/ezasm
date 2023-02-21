package com.ezasm.instructions.targets.inputoutput.mock;

import com.ezasm.instructions.targets.inputoutput.IAbstractInputOutput;
import com.ezasm.simulation.ISimulator;
import com.ezasm.simulation.Transformation;
import com.ezasm.simulation.exception.SimulationException;
import com.ezasm.util.Conversion;

/**
 * Utility to allow for direct access to the heap pointer in memory. Not meant for use as an actual input
 */
public class HeapPointerInputOutput implements IAbstractInputOutput {

    public HeapPointerInputOutput() {
    }

    @Override
    public byte[] get(ISimulator simulator) throws SimulationException {
        return Conversion.longToBytes(simulator.getMemory().currentHeapPointer());
    }

    @Override
    public void set(ISimulator simulator, byte[] value) throws SimulationException {
        simulator.getMemory().setHeapPointer((int) Conversion.bytesToLong(value));
    }

    @Override
    public Transformation transformation(ISimulator simulator, byte[] value) throws SimulationException {
        return new Transformation(this, get(simulator), value);
    }

}
