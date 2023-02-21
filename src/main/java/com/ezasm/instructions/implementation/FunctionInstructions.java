package com.ezasm.instructions.implementation;

import com.ezasm.instructions.Instruction;
import com.ezasm.instructions.targets.input.IAbstractInput;
import com.ezasm.instructions.targets.inputoutput.RegisterInputOutput;
import com.ezasm.simulation.*;
import com.ezasm.simulation.exception.SimulationException;
import com.ezasm.util.Conversion;

/**
 * An implementation of standard function call instructions for the simulation.
 */
public class FunctionInstructions {

    private final ISimulator simulator;
    private final MemoryInstructions memoryInstructions;

    /**
     * Some instructions require access to the Simulator directly, so that is provided.
     *
     * @param simulator the provided Simulator.
     */
    public FunctionInstructions(ISimulator simulator) {
        this.simulator = simulator;
        this.memoryInstructions = new MemoryInstructions(simulator);
    }

    /**
     * The standard jump operation: sets the PC to the given line number.
     *
     * @param input the line to jump to.
     * @throws SimulationException if there is an error in accessing the simulation.
     */
    @Instruction
    public TransformationSequence jump(IAbstractInput input) throws SimulationException {
        RegisterInputOutput pc = new RegisterInputOutput(Registers.PC);
        return new TransformationSequence(pc, pc.get(simulator), input.get(simulator));
    }

    /**
     * The standard jump operation: sets the PC to the given line number.
     *
     * @param input the line to jump to.
     * @throws SimulationException if there is an error in accessing the simulation.
     */
    @Instruction
    public TransformationSequence j(IAbstractInput input) throws SimulationException {
        return jump(input);
    }

    /**
     * The jump and link operation: sets the PC to the given line number and stores the return address. Stores the
     * previous return address onto the stack.
     *
     * @param input the line to jump to.
     * @throws SimulationException if there is an error in accessing the simulation.
     */
    @Instruction
    public TransformationSequence call(IAbstractInput input) throws SimulationException {
        RegisterInputOutput ra = new RegisterInputOutput(Registers.RA);
        Register pc = simulator.getRegisters().getRegister(Registers.PC);
        TransformationSequence t = new TransformationSequence();
        t = t.concatenate(memoryInstructions.push(ra));
        t = t.concatenate(new TransformationSequence(ra, ra.get(simulator), pc.getBytes()));
        t = t.concatenate(jump(input));
        return t;
    }

    /**
     * The jump and link operation: sets the PC to the given line number and stores the return address. Stores the
     * previous return address onto the stack.
     *
     * @param input the line to jump to.
     * @throws SimulationException if there is an error in accessing the simulation.
     */
    @Instruction
    public TransformationSequence jal(IAbstractInput input) throws SimulationException {
        return call(input);
    }

    /**
     * Return to the current return address and then pops the next return address off of the stack.
     *
     * @throws SimulationException if there is an error in accessing the simulation.
     */
    @Instruction
    public TransformationSequence _return() throws SimulationException {
        TransformationSequence t = new TransformationSequence();
        t = t.concatenate(jump(new RegisterInputOutput(Registers.RA)));
        t = t.concatenate(memoryInstructions.pop(new RegisterInputOutput(Registers.RA)));
        return t;
    }

    /**
     * Exit the program naturally and sets the exit code to the given value.
     *
     * @param input the status code.
     *
     * @throws SimulationException if there is an error in accessing the simulation.
     */
    @Instruction
    public TransformationSequence exit(IAbstractInput input) throws SimulationException {
        RegisterInputOutput r0 = new RegisterInputOutput(Registers.R0);
        RegisterInputOutput pc = new RegisterInputOutput(Registers.PC);
        Transformation t1 = new Transformation(r0, r0.get(simulator), input.get(simulator));
        Transformation t2 = new Transformation(pc, pc.get(simulator), Conversion.longToBytes(simulator.endPC()));
        return new TransformationSequence(t1, t2);
    }

}
