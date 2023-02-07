package com.ezasm.instructions.targets.input;

import com.ezasm.Conversion;
import com.ezasm.simulation.ISimulator;
import com.ezasm.simulation.SimulationException;

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
    public byte[] get(ISimulator simulator) throws SimulationException {
        try {
            return Conversion.longToBytes(simulator.getLabels().get(label));
        } catch (NullPointerException e) {
            throw new SimulationException(String.format("Label '%s' does not exist", label));
        }
    }
}
