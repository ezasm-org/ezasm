package com.ezasm.instructions.implementation;

import com.ezasm.instructions.Instruction;
import com.ezasm.instructions.targets.input.StringInput;
import com.ezasm.parsing.ParseException;
import com.ezasm.simulation.Simulator;
import com.ezasm.simulation.exception.SimulationException;
import com.ezasm.simulation.transform.TransformationSequence;

/**
 * Represents instructions that involve importing code from other files.
 */
public class ImportInstructions {

    private final Simulator simulator;

    /**
     * Some instructions require access to the Simulator directly, so that is provided.
     *
     * @param simulator the provided Simulator.
     */
    public ImportInstructions(Simulator simulator) {
        this.simulator = simulator;
    }

    /**
     * Imports code from the file at the given relative path. Undoing and redoing an import would be difficult and
     * unnecessary, so the transformation sequence for this operation is empty.
     *
     * @param input the relative path to another source code file.
     * @throws SimulationException if there is an error reading or importing the file.
     */
    @Instruction
    public TransformationSequence _import(StringInput input) throws SimulationException {
        try {
            simulator.importLinesFromFile(input.getString());
        } catch (ParseException e) {
            throw new SimulationException(String.format("Error importing %s: %s", input.getString(), e.getMessage()));
        }
        return new TransformationSequence();
    }

}
