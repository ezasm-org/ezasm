package com.ezasm.instructions.targets.output;

import com.ezasm.simulation.Simulator;
import com.ezasm.instructions.targets.IAbstractTarget;
import com.ezasm.simulation.exception.SimulationException;
import com.ezasm.util.RawData;

/**
 * Represents an output to an operation. Requires implementing a "set" operation to serve as the output.
 */
public interface IAbstractOutput extends IAbstractTarget {

    /**
     * Gets the value pertaining to this output.
     *
     * @param simulator the program simulator.
     * @param value     the value to set.
     */
    public void set(Simulator simulator, RawData value) throws SimulationException;

}
