package com.ezasm.instructions.targets.inputoutput.mock;

import com.ezasm.instructions.implementation.TerminalInstructions;
import com.ezasm.instructions.targets.inputoutput.IAbstractInputOutput;
import com.ezasm.simulation.ISimulator;
import com.ezasm.simulation.Transformation;
import com.ezasm.simulation.exception.SimulationException;
import com.ezasm.util.Conversion;

public class FileReadInputOutput implements IAbstractInputOutput {

    private final long cursor;

    public FileReadInputOutput(long cursor) {
        this.cursor = cursor;
    }

    @Override
    public byte[] get(ISimulator simulator) throws SimulationException {
        return Conversion.longToBytes(cursor);
    }

    @Override
    public void set(ISimulator simulator, byte[] value) throws SimulationException {
        TerminalInstructions.streams().moveCursor(Conversion.bytesToLong(value));
    }

    @Override
    public Transformation transformation(ISimulator simulator, byte[] value) throws SimulationException {
        return new Transformation(this, get(simulator), value);
    }

}
