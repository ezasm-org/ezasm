package com.ezasm.simulation.transform.transformable;

import com.ezasm.simulation.ISimulator;
import com.ezasm.simulation.exception.SimulationException;
import com.ezasm.simulation.transform.Transformation;
import com.ezasm.util.RawData;

public abstract class AbstractTransformable {

    final ISimulator simulator;

    protected AbstractTransformable(ISimulator simulator) {
        this.simulator = simulator;
    }

    public abstract void set(RawData data) throws SimulationException;

    public abstract Transformation transformation(RawData value) throws SimulationException;
}
