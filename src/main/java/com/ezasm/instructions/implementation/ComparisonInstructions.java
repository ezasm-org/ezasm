package com.ezasm.instructions.implementation;

import com.ezasm.instructions.targets.inputoutput.IAbstractInputOutput;
import com.ezasm.instructions.targets.inputoutput.RegisterInputOutput;
import com.ezasm.simulation.Registers;
import com.ezasm.simulation.transform.TransformationSequence;
import com.ezasm.simulation.transform.transformable.InputOutputTransformable;
import com.ezasm.util.Conversion;
import com.ezasm.instructions.Instruction;
import com.ezasm.instructions.targets.input.IAbstractInput;
import com.ezasm.simulation.ISimulator;
import com.ezasm.simulation.exception.SimulationException;
import com.ezasm.util.RawData;

import java.util.function.BiFunction;

/**
 * An implementation of standard comparison instructions for the simulation.
 */
public class ComparisonInstructions {

    private final ISimulator simulator;

    /**
     * Some instructions require access to the Simulator directly, so that is provided.
     *
     * @param simulator the provided Simulator.
     */
    public ComparisonInstructions(ISimulator simulator) {
        this.simulator = simulator;
    }

    /**
     * Template comparison operation.
     *
     * @param op     operation to apply to the arguments.
     * @param output the output of the operation.
     * @param input1 the left-hand side of the operation.
     * @param input2 the right-hand side of the operation.
     */
    private TransformationSequence compare(BiFunction<Long, Long, Boolean> op, IAbstractInputOutput output,
            IAbstractInput input1, IAbstractInput input2) throws SimulationException {

        boolean res = op.apply(input1.get(simulator).intValue(), input2.get(simulator).intValue());
        InputOutputTransformable io = new InputOutputTransformable(simulator, output);
        return new TransformationSequence(io.transformation(new RawData(res ? 1 : 0)));
    }

    /**
     * The standard equals operation.
     *
     * @param output the output of the operation.
     * @param input1 the left-hand side of the equals operation.
     * @param input2 the right-hand side of the equals operation.
     * @throws SimulationException if there is an error in accessing the simulation.
     */
    @Instruction
    public TransformationSequence seq(IAbstractInputOutput output, IAbstractInput input1, IAbstractInput input2)
            throws SimulationException {
        return compare(Long::equals, output, input1, input2);
    }

    /**
     * The standard not equals operation.
     *
     * @param output the output of the operation.
     * @param input1 the left-hand side of the not equals operation.
     * @param input2 the right-hand side of the not equals operation.
     * @throws SimulationException if there is an error in accessing the simulation.
     */
    @Instruction
    public TransformationSequence sne(IAbstractInputOutput output, IAbstractInput input1, IAbstractInput input2)
            throws SimulationException {
        return compare((l, r) -> l.longValue() != r.longValue(), output, input1, input2);
    }

    /**
     * The standard less than operation.
     *
     * @param output the output of the operation.
     * @param input1 the left-hand side of the less than operation.
     * @param input2 the right-hand side of the less than operation.
     * @throws SimulationException if there is an error in accessing the simulation.
     */
    @Instruction
    public TransformationSequence slt(IAbstractInputOutput output, IAbstractInput input1, IAbstractInput input2)
            throws SimulationException {
        return compare((l, r) -> l < r, output, input1, input2);
    }

    /**
     * The standard less than or equal to operation.
     *
     * @param output the output of the operation.
     * @param input1 the left-hand side of the less than or equal to operation.
     * @param input2 the right-hand side of the less than or equal to operation.
     * @throws SimulationException if there is an error in accessing the simulation.
     */
    @Instruction
    public TransformationSequence sle(IAbstractInputOutput output, IAbstractInput input1, IAbstractInput input2)
            throws SimulationException {
        return compare((l, r) -> l <= r, output, input1, input2);
    }

    /**
     * The standard greater than operation.
     *
     * @param output the output of the operation.
     * @param input1 the left-hand side of the greater than operation.
     * @param input2 the right-hand side of the greater than operation.
     * @throws SimulationException if there is an error in accessing the simulation.
     */
    @Instruction
    public TransformationSequence sgt(IAbstractInputOutput output, IAbstractInput input1, IAbstractInput input2)
            throws SimulationException {
        return compare((l, r) -> l > r, output, input1, input2);
    }

    /**
     * The standard greater than or equal to operation.
     *
     * @param output the output of the operation.
     * @param input1 the left-hand side of the greater than or equal to operation.
     * @param input2 the right-hand side of the greater than or equal to operation.
     * @throws SimulationException if there is an error in accessing the simulation.
     */
    @Instruction
    public TransformationSequence sge(IAbstractInputOutput output, IAbstractInput input1, IAbstractInput input2)
            throws SimulationException {
        return compare((l, r) -> l >= r, output, input1, input2);
    }
}
