package EzASM.instructions.targets.input;

import EzASM.simulation.Simulator;

/**
 * The abstract form of an input to an operation.
 * Requires implementing a "get" operation to serve as the input.
 */
public abstract class AbstractInput {

    /**
     * Gets the value pertaining to this input.
     * @param simulator the program simulator.
     * @return the obtained value.
     */
    public abstract byte[] get(Simulator simulator);

}
