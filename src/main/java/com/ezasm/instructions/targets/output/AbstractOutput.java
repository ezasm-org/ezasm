package com.ezasm.instructions.targets.output;

import com.ezasm.simulation.Simulator;

/**
 * The abstract form of an input to an operation. Requires implementing a "set" operation to serve
 * as the output.
 */
public abstract class AbstractOutput {

    /**
     * Gets the value pertaining to this output.
     *
     * @param simulator the program simulator.
     * @param value     the value to set.
     */
    public abstract void set(Simulator simulator, byte[] value);

}
