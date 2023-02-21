
package com.ezasm.instructions.implementation;

import com.ezasm.instructions.Instruction;
import com.ezasm.instructions.targets.input.IAbstractInput;
import com.ezasm.instructions.targets.inputoutput.DereferenceInputOutput;
import com.ezasm.instructions.targets.inputoutput.IAbstractInputOutput;
import com.ezasm.instructions.targets.inputoutput.RegisterInputOutput;
import com.ezasm.instructions.targets.inputoutput.mock.HeapPointerInputOutput;
import com.ezasm.instructions.targets.inputoutput.mock.MemoryInputOutput;
import com.ezasm.simulation.*;
import com.ezasm.simulation.exception.SimulationException;
import com.ezasm.util.Conversion;
import com.ezasm.util.Operations;

/**
 * An implementation of memory manipulation instructions for the simulation.
 */
public class MemoryInstructions {

    private final ISimulator simulator;

    /**
     * Some instructions require access to the Simulator directly, so that is provided.
     *
     * @param simulator the provided Simulator.
     */
    public MemoryInstructions(ISimulator simulator) {
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

        RegisterInputOutput sp = new RegisterInputOutput(Registers.SP);
        Transformation t1 = new Transformation(sp, sp.get(simulator), Operations.addIntegerBytes(sp.get(simulator),
                Conversion.longToBytes(-simulator.getMemory().WORD_SIZE)));
        MemoryInputOutput memory = new MemoryInputOutput(Conversion.bytesToLong(t1.to()));
        Transformation t2 = memory.transformation(simulator, input.get(simulator));
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
        RegisterInputOutput sp = new RegisterInputOutput(Registers.SP);
        MemoryInputOutput memory = new MemoryInputOutput(Conversion.bytesToLong(sp.get(simulator)));
        Transformation t1 = new Transformation(output, output.get(simulator), memory.get(simulator));
        Transformation t2 = new Transformation(sp, sp.get(simulator),
                Operations.addIntegerBytes(sp.get(simulator), Conversion.longToBytes(simulator.getMemory().WORD_SIZE)));

        return new TransformationSequence(t1, t2);
    }

    @Instruction
    public TransformationSequence load(IAbstractInputOutput output, DereferenceInputOutput input)
            throws SimulationException {
        return new TransformationSequence(output.transformation(simulator, input.get(simulator)));
    }

    @Instruction
    public TransformationSequence store(IAbstractInput input, DereferenceInputOutput output)
            throws SimulationException {
        return new TransformationSequence(output.transformation(simulator, input.get(simulator)));
    }

    @Instruction
    public TransformationSequence alloc(IAbstractInputOutput output, IAbstractInput input) throws SimulationException {
        HeapPointerInputOutput h = new HeapPointerInputOutput();
        Transformation t1 = new Transformation(h, h.get(simulator),
                Operations.addIntegerBytes(h.get(simulator), input.get(simulator)));
        Transformation t2 = output.transformation(simulator, t1.from());
        return new TransformationSequence(t1, t2);
    }

    @Instruction
    public TransformationSequence move(IAbstractInputOutput output, IAbstractInput input) throws SimulationException {
        return new TransformationSequence(output.transformation(simulator, input.get(simulator)));
    }

}
