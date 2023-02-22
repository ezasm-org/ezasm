package com.ezasm.simulation.transform.transformable;

import com.ezasm.instructions.targets.output.IAbstractOutput;
import com.ezasm.simulation.ISimulator;
import com.ezasm.simulation.exception.SimulationException;
import com.ezasm.simulation.transform.Transformation;
import com.ezasm.util.RawData;

public class OutputTransformable extends AbstractTransformable {

    private final IAbstractOutput output;
    private final RawData from;

    public OutputTransformable(ISimulator simulator, IAbstractOutput output, RawData from) {
        super(simulator);
        this.output = output;
        this.from = from;
    }

    @Override
    public void set(RawData data) throws SimulationException {
        output.set(simulator, data);
    }

    @Override
    public Transformation transformation(RawData value) {
        return new Transformation(this, from, value);
    }
}
