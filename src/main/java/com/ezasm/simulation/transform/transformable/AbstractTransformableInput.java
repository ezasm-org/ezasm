package com.ezasm.simulation.transform.transformable;

import com.ezasm.simulation.Simulator;
import com.ezasm.simulation.exception.SimulationException;
import com.ezasm.simulation.transform.Transformation;
import com.ezasm.util.RawData;

/**
 * Represents a possible transformable aspect which has data you are able to read.
 */
public abstract class AbstractTransformableInput extends AbstractTransformable {

    protected AbstractTransformableInput(Simulator simulator) {
        super(simulator);
    }

    /**
     * Gets the data stored in the transformable field.
     *
     * @return the data stored in the transformable field.
     * @throws SimulationException if there is an error getting the data.
     */
    public abstract RawData get() throws SimulationException;

    /**
     * Creates a transformation based on the value that the transformable field is going to be set to.
     *
     * @param value the new value for the transformable.
     * @return the created transformation.
     * @throws SimulationException if there is an error in creating the transformation.
     */
    public Transformation transformation(RawData value) throws SimulationException {
        return new Transformation(this, get(), value);
    }
}
