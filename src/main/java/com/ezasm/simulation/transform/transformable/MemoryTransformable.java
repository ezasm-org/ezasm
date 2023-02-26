package com.ezasm.simulation.transform.transformable;

import com.ezasm.simulation.ISimulator;
import com.ezasm.simulation.exception.SimulationException;
import com.ezasm.util.RawData;

/**
 * Represents a word of memory as a transformable field.
 */
public class MemoryTransformable extends AbstractTransformableInput {

    private final long address;

    /**
     * Constructs a representation of memory as a transformable field.
     *
     * @param simulator the simulator we are acting on.
     * @param address   the address of memory at which the word resides.
     */
    public MemoryTransformable(ISimulator simulator, long address) {
        super(simulator);
        this.address = address;
    }

    /**
     * Gets the word at the given position in memory
     *
     * @return the word at the given position in memory
     * @throws SimulationException if there is an error reading from the memory.
     */
    @Override
    public RawData get() throws SimulationException {
        return simulator.getMemory().read((int) address);
    }

    /**
     * Sets the word at the given position in memory.
     *
     * @param value the word to store in memory.
     * @throws SimulationException if there is an error writing to the memory.
     */
    @Override
    public void set(RawData value) throws SimulationException {
        simulator.getMemory().write((int) address, value);
    }
}
