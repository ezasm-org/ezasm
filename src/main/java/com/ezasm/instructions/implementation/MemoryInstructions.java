package com.ezasm.instructions.implementation;

import com.ezasm.instructions.Instruction;
import com.ezasm.instructions.targets.input.IAbstractInput;
import com.ezasm.instructions.targets.inputoutput.DereferenceInputOutput;
import com.ezasm.instructions.targets.inputoutput.IAbstractInputOutput;
import com.ezasm.instructions.targets.inputoutput.RegisterInputOutput;
import com.ezasm.simulation.*;
import com.ezasm.simulation.exception.SimulationException;
import com.ezasm.simulation.transform.Transformation;
import com.ezasm.simulation.transform.TransformationSequence;
import com.ezasm.simulation.transform.transformable.HeapPointerTransformable;
import com.ezasm.simulation.transform.transformable.InputOutputTransformable;
import com.ezasm.simulation.transform.transformable.MemoryTransformable;
import com.ezasm.util.RawData;

/**
 * An implementation of memory manipulation instructions for the simulation.
 */
public class MemoryInstructions {

    private final Simulator simulator;

    /**
     * Some instructions require access to the Simulator directly, so that is provided.
     *
     * @param simulator the provided Simulator.
     */
    public MemoryInstructions(Simulator simulator) {
        this.simulator = simulator;
    }

    /**
     * Pushes a value to the stack, moving the stack pointer to allow for room on the stack to store it.
     *
     * @param input the value to be pushed onto the stack.
     * @throws SimulationException if there is an error in accessing the simulation.
     */
    @Instruction
    public TransformationSequence push(IAbstractInput input) throws SimulationException {
        return consecutivePush(input, 0);
    }

    /**
     * Represents a consecutive push made in a transformation after any initial push.
     *
     * @param input the value to be stored on the stack.
     * @param times the number of times before this that something has been pushed.
     * @return the transformation sequence for this push.
     * @throws SimulationException if there is an error in accessing the simulation.
     */
    public TransformationSequence consecutivePush(IAbstractInput input, int times) throws SimulationException {
        int offset = times * simulator.getMemory().wordSize;
        RegisterInputOutput sp = new RegisterInputOutput(Registers.SP);
        Transformation t1 = new Transformation(new InputOutputTransformable(simulator, sp), sp.get(simulator),
                new RawData(sp.get(simulator).intValue() - simulator.getMemory().wordSize - offset));
        MemoryTransformable m = new MemoryTransformable(simulator, t1.to().intValue());
        Transformation t2 = m.transformation(input.get(simulator));
        return new TransformationSequence(t1, t2);
    }

    /**
     * Pops a value from the stack, moving the stack pointer to deallocate it.
     *
     * @param output the place to store the obtained value.
     * @throws SimulationException if there is an error in accessing the simulation.
     */
    @Instruction
    public TransformationSequence pop(IAbstractInputOutput output) throws SimulationException {
        return consecutivePop(output, 0);
    }

    /**
     * Represents a consecutive pop made in a transformation after any initial pop.
     *
     * @param output the place to store the obtained value.
     * @param times  the number of times before this that something has been popped.
     * @throws SimulationException if there is an error in accessing the simulation.
     */
    @Instruction
    public TransformationSequence consecutivePop(IAbstractInputOutput output, int times) throws SimulationException {
        int offset = times * simulator.getMemory().wordSize;
        RegisterInputOutput sp = new RegisterInputOutput(Registers.SP);
        InputOutputTransformable io = new InputOutputTransformable(simulator, output);
        Transformation t1 = io.transformation(simulator.getMemory().read((int) sp.get(simulator).intValue() + offset));
        Transformation t2 = (new InputOutputTransformable(simulator, sp)
                .transformation(new RawData(sp.get(simulator).intValue() + simulator.getMemory().wordSize + offset)));
        return new TransformationSequence(t1, t2);
    }

    @Instruction
    public TransformationSequence load(IAbstractInputOutput output, DereferenceInputOutput input)
            throws SimulationException {
        InputOutputTransformable io = new InputOutputTransformable(simulator, output);
        return new TransformationSequence(io.transformation(input.get(simulator)));
    }

    @Instruction
    public TransformationSequence store(IAbstractInput input, DereferenceInputOutput output)
            throws SimulationException {
        InputOutputTransformable io = new InputOutputTransformable(simulator, output);
        return new TransformationSequence(io.transformation(input.get(simulator)));
    }

    @Instruction
    public TransformationSequence alloc(IAbstractInputOutput output, IAbstractInput input) throws SimulationException {
        HeapPointerTransformable h = new HeapPointerTransformable(simulator);
        InputOutputTransformable io = new InputOutputTransformable(simulator, output);
        Transformation t1 = new Transformation(h, h.get(),
                new RawData(h.get().intValue() + input.get(simulator).intValue()));
        Transformation t2 = io.transformation(t1.from());
        return new TransformationSequence(t1, t2);
    }

    @Instruction
    public TransformationSequence move(IAbstractInputOutput output, IAbstractInput input) throws SimulationException {
        InputOutputTransformable io = new InputOutputTransformable(simulator, output);
        return new TransformationSequence(io.transformation(input.get(simulator)));
    }

}
