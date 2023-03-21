package com.ezasm.instructions.targets.input;

import com.ezasm.simulation.Simulator;
import com.ezasm.simulation.exception.SimulationException;
import com.ezasm.util.RawData;

import java.util.Objects;

/**
 * The implementation of a "label" reference to be used as a reference to the line following it.
 */
public class LabelReferenceInput implements IAbstractInput {

    private final String label;

    public LabelReferenceInput(String label) {
        this.label = label;
    }

    /**
     * Gets the line number referred to by the label.
     *
     * @param simulator the program simulator.
     * @return the constant value.
     */
    @Override
    public RawData get(Simulator simulator) throws SimulationException {
        try {
            return new RawData(simulator.getLabelToFileIdAndLineNumber().get(label).getRight());
        } catch (NullPointerException e) {
            throw new SimulationException(String.format("Label '%s' does not exist", label));
        }
    }

    /**
     * Gets the file id referred to by the label.
     *
     * @param simulator the program simulator.
     * @return the file id referred to by the label.
     */
    public RawData getLabelFileId(Simulator simulator) throws SimulationException {
        try {
            return new RawData(simulator.getLabelToFileIdAndLineNumber().get(label).getLeft());
        } catch (NullPointerException e) {
            throw new SimulationException(String.format("Label '%s' does not exist", label));
        }
    }

    @Override
    public boolean equals(Object o) {
        if (this == o)
            return true;
        if (o == null || getClass() != o.getClass())
            return false;
        LabelReferenceInput that = (LabelReferenceInput) o;
        return label.equals(that.label);
    }

    @Override
    public int hashCode() {
        return Objects.hash(label);
    }
}
