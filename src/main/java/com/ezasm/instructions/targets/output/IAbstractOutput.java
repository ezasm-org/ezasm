package com.ezasm.instructions.targets.output;

import com.ezasm.simulation.ISimulator;
import com.ezasm.instructions.targets.IAbstractTarget;
import com.ezasm.simulation.SimulationException;

/**
 * The abstract form of an input to an operation. Requires implementing a "set" operation to serve
 * as the output.
 */
public interface IAbstractOutput extends IAbstractTarget {

    /**
     * Gets the value pertaining to this output.
     *
     * @param simulator the program simulator.
     * @param value     the value to set.
     */
    public void set(ISimulator simulator, byte[] value) throws SimulationException;

}
