package com.ezasm.simulation.transform.transformable;

import com.ezasm.simulation.ISimulator;
import com.ezasm.simulation.exception.SimulationException;
import com.ezasm.simulation.transform.Transformation;
import com.ezasm.util.RawData;

public abstract class AbstractTransformableInput extends AbstractTransformable {

    protected AbstractTransformableInput(ISimulator simulator) {
        super(simulator);
    }

    public abstract RawData get() throws SimulationException;

    public abstract void set(RawData data) throws SimulationException;

    public abstract Transformation transformation(RawData value) throws SimulationException;
}
