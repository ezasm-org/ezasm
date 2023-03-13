package com.ezasm.instructions.implementation;

import com.ezasm.instructions.Instruction;
import com.ezasm.instructions.targets.input.IAbstractInput;
import com.ezasm.instructions.targets.input.StringInput;
import com.ezasm.instructions.targets.inputoutput.RegisterInputOutput;
import com.ezasm.simulation.ISimulator;
import com.ezasm.simulation.Registers;
import com.ezasm.simulation.exception.SimulationException;
import com.ezasm.simulation.transform.TransformationSequence;
import com.ezasm.simulation.transform.transformable.InputOutputTransformable;

/**
 * Represents instructions that involve importing code from other files.
 */
public class ImportInstructions {

    private final ISimulator simulator;

    /**
     * Some instructions require access to the Simulator directly, so that is provided.
     *
     * @param simulator the provided Simulator.
     */
    public ImportInstructions(ISimulator simulator) {
        this.simulator = simulator;
    }


    /**
     * The standard jump operation: sets the PC to the given line number.
     *
     * @param input the line to jump to.
     * @throws SimulationException if there is an error in accessing the simulation.
     */
    @Instruction
    public TransformationSequence _import(StringInput input) throws SimulationException {
        InputOutputTransformable pc = new InputOutputTransformable(simulator, new RegisterInputOutput(Registers.PC));
        return new TransformationSequence(pc.transformation(input.get(simulator)));
    }

}
