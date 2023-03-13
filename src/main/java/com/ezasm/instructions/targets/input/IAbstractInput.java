package com.ezasm.instructions.targets.input;

import com.ezasm.simulation.Simulator;
import com.ezasm.instructions.targets.IAbstractTarget;
import com.ezasm.simulation.exception.SimulationException;
import com.ezasm.util.RawData;

/**
 * Represents an input to an operation. Requires implementing a "get" operation to serve as the output.
 */
public interface IAbstractInput extends IAbstractTarget {

    /**
     * Gets the value pertaining to this input.
     *
     * @param simulator the program simulator.
     * @return the obtained value.
     */
    public RawData get(Simulator simulator) throws SimulationException;

}
