package com.ezasm.simulation.transform.transformable;

import com.ezasm.instructions.targets.output.IAbstractOutput;
import com.ezasm.simulation.ISimulator;
import com.ezasm.simulation.exception.SimulationException;
import com.ezasm.simulation.transform.Transformation;
import com.ezasm.util.RawData;

/**
 * Represents a transformable output already implemented as a <code>IAbstractOutput</code>.
 */
public class OutputTransformable extends AbstractTransformable {

    private final IAbstractOutput output;
    private final RawData from;

    /**
     * Constructs a transformable representation of an already implemented <code>IAbstractOutput</code>.
     *
     * @param simulator the simulator to use.
     * @param output    the output to write to.
     * @param from      the data that was previously stored in that output.
     */
    public OutputTransformable(ISimulator simulator, IAbstractOutput output, RawData from) {
        super(simulator);
        this.output = output;
        this.from = from;
    }

    /**
     * Sets the data in the output to the given data.
     *
     * @param data the data to set to.
     * @throws SimulationException if there is an error in setting the output.
     */
    @Override
    public void set(RawData data) throws SimulationException {
        output.set(simulator, data);
    }

    /**
     * Creates a transformation based on the value that the given output is going to be set to.
     *
     * @param value the new value for the output.
     * @return the created transformation.
     * @throws SimulationException if there is an error in creating the transformation.
     */
    @Override
    public Transformation transformation(RawData value) throws SimulationException {
        return new Transformation(this, from, value);
    }
}
