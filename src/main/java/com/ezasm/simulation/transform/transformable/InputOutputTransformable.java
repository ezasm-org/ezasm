package com.ezasm.simulation.transform.transformable;

import com.ezasm.instructions.targets.inputoutput.IAbstractInputOutput;
import com.ezasm.simulation.Simulator;
import com.ezasm.simulation.exception.SimulationException;
import com.ezasm.util.RawData;

/**
 * Represents a transformable output already implemented as a <code>IAbstractInputOutput</code>.
 */
public class InputOutputTransformable extends AbstractTransformableInput {

    private final IAbstractInputOutput inputOutput;

    /**
     * Constructs a transformable representation of an already implemented <code>IAbstractInputOutput</code>.
     *
     * @param simulator   the simulator to use.
     * @param inputOutput the input/output to write to.
     */
    public InputOutputTransformable(Simulator simulator, IAbstractInputOutput inputOutput) {
        super(simulator);
        this.inputOutput = inputOutput;
    }

    /**
     * Gets the data stored in input/output.
     *
     * @return the data stored in the input/output.
     * @throws SimulationException if there is an error getting the data.
     */
    @Override
    public RawData get() throws SimulationException {
        return inputOutput.get(simulator);
    }

    /**
     * Sets the data in the input/output to the given data.
     *
     * @param data the data to set to.
     * @throws SimulationException if there is an error in setting the input/output.
     */
    @Override
    public void set(RawData data) throws SimulationException {
        inputOutput.set(simulator, data);
    }
}
