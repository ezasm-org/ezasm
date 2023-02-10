package com.ezasm.instructions.impl;

import com.ezasm.Conversion;
import com.ezasm.instructions.Instruction;
import com.ezasm.instructions.targets.input.IAbstractInput;
import com.ezasm.instructions.targets.output.IAbstractOutput;
import com.ezasm.simulation.ISimulator;
import com.ezasm.simulation.exception.SimulationException;

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
    private void compare(BiFunction<Long, Long, Boolean> op, IAbstractOutput output, IAbstractInput input1,
            IAbstractInput input2) throws SimulationException {

        boolean res = op.apply(Conversion.bytesToLong(input1.get(simulator)),
                Conversion.bytesToLong(input2.get(simulator)));
        output.set(this.simulator, Conversion.longToBytes(res ? 1 : 0));
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
    public void seq(IAbstractOutput output, IAbstractInput input1, IAbstractInput input2) throws SimulationException {
        compare(Long::equals, output, input1, input2);
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
    public void sne(IAbstractOutput output, IAbstractInput input1, IAbstractInput input2) throws SimulationException {
        compare((l, r) -> l.longValue() != r.longValue(), output, input1, input2);
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
    public void slt(IAbstractOutput output, IAbstractInput input1, IAbstractInput input2) throws SimulationException {
        compare((l, r) -> l < r, output, input1, input2);
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
    public void sle(IAbstractOutput output, IAbstractInput input1, IAbstractInput input2) throws SimulationException {
        compare((l, r) -> l <= r, output, input1, input2);
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
    public void sgt(IAbstractOutput output, IAbstractInput input1, IAbstractInput input2) throws SimulationException {
        compare((l, r) -> l > r, output, input1, input2);
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
    public void sge(IAbstractOutput output, IAbstractInput input1, IAbstractInput input2) throws SimulationException {
        compare((l, r) -> l >= r, output, input1, input2);
    }
}
