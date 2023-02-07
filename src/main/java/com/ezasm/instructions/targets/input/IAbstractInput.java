package com.ezasm.instructions.targets.input;

import com.ezasm.simulation.ISimulator;
import com.ezasm.instructions.targets.IAbstractTarget;
import com.ezasm.simulation.SimulationException;

/**
 * The abstract form of an input to an operation. Requires implementing a "get" operation to serve
 * as the input.
 */
public interface IAbstractInput extends IAbstractTarget {

    /**
     * Gets the value pertaining to this input.
     *
     * @param simulator the program simulator.
     * @return the obtained value.
     */
    public byte[] get(ISimulator simulator) throws SimulationException;

}
