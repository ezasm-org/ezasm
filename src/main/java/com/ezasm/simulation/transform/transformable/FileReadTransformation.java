package com.ezasm.simulation.transform.transformable;

import com.ezasm.instructions.implementation.TerminalInstructions;
import com.ezasm.simulation.ISimulator;
import com.ezasm.simulation.exception.SimulationException;
import com.ezasm.util.RawData;

/**
 * Represents the transformation of reading from a file. The main transformation is to the "cursor" of the file or the
 * position which data will be read from the file.
 */
public class FileReadTransformation extends AbstractTransformableInput {

    private final long cursor;

    /**
     * Creates a transformation representing a reading from a file.
     *
     * @param simulator the simulator to use.
     * @param cursor    the current position of the file's cursor.
     */
    public FileReadTransformation(ISimulator simulator, long cursor) {
        super(simulator);
        this.cursor = cursor;
    }

    /**
     * Gets the cursor's initial value as raw bytes.
     *
     * @return the cursor's initial value as raw bytes.
     */
    @Override
    public RawData get() {
        return new RawData(cursor);
    }

    /**
     * Sets the cursor's position to the given value interpreted as an integer.
     *
     * @param value the new cursor's position.
     * @throws SimulationException if an error occurs moving the file cursor position.
     */
    @Override
    public void set(RawData value) throws SimulationException {
        TerminalInstructions.streams().moveCursor(value.intValue());
    }
}
