package com.ezasm.simulation.transform.transformable;

import com.ezasm.simulation.Simulator;
import com.ezasm.simulation.exception.SimulationException;
import com.ezasm.simulation.transform.Transformation;
import com.ezasm.util.RawData;

/**
 * Represents a possible transformable value aspect of the simulated computer.
 */
public abstract class AbstractTransformable {

    final Simulator simulator;

    protected AbstractTransformable(Simulator simulator) {
        this.simulator = simulator;
    }

    /**
     * This function sets the corresponding data in the simulation to the given data.
     *
     * @param data the data to set to.
     * @throws SimulationException if there is an error in setting the data.
     */
    public abstract void set(RawData data) throws SimulationException;

    /**
     * Creates a transformation based on the current state of the transformable to the new value for the transformable.
     *
     * @param value the new value for the transformable.
     * @return the created transformation.
     * @throws SimulationException if there is an error in creating the transformation.
     */
    public abstract Transformation transformation(RawData value) throws SimulationException;
}
