package com.ezasm.simulation.transform.transformable;

import com.ezasm.instructions.targets.inputoutput.IAbstractInputOutput;
import com.ezasm.simulation.ISimulator;
import com.ezasm.simulation.exception.SimulationException;
import com.ezasm.simulation.transform.Transformation;
import com.ezasm.util.RawData;

public class InputOutputTransformable extends AbstractTransformableInput {

    private final IAbstractInputOutput inputOutput;

    public InputOutputTransformable(ISimulator simulator, IAbstractInputOutput inputOutput) {
        super(simulator);
        this.inputOutput = inputOutput;
    }

    @Override
    public RawData get() throws SimulationException {
        return inputOutput.get(simulator);
    }

    @Override
    public void set(RawData data) throws SimulationException {
        inputOutput.set(simulator, data);
    }

    @Override
    public Transformation transformation(RawData value) throws SimulationException {
        return new Transformation(this, get(), value);
    }
}
