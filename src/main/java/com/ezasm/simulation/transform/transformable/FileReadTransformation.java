package com.ezasm.simulation.transform.transformable;

import com.ezasm.instructions.implementation.TerminalInstructions;
import com.ezasm.simulation.ISimulator;
import com.ezasm.simulation.exception.SimulationException;
import com.ezasm.simulation.transform.Transformation;
import com.ezasm.util.RawData;

public class FileReadTransformation extends AbstractTransformableInput {

    private final long cursor;

    public FileReadTransformation(ISimulator simulator, long cursor) {
        super(simulator);
        this.cursor = cursor;
    }

    @Override
    public RawData get() throws SimulationException {
        return new RawData(cursor);
    }

    @Override
    public void set(RawData value) throws SimulationException {
        TerminalInstructions.streams().moveCursor(value.intValue());
    }

    @Override
    public Transformation transformation(RawData value) throws SimulationException {
        return new Transformation(this, get(), value);
    }

}
