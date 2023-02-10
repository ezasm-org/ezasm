package com.ezasm.instructions.impl;

import com.ezasm.Conversion;
import com.ezasm.instructions.Instruction;
import com.ezasm.instructions.targets.input.IAbstractInput;
import com.ezasm.simulation.ISimulator;
import com.ezasm.simulation.Registers;
import com.ezasm.simulation.exception.SimulationException;

import java.util.function.BiFunction;

/**
 * An implementation of standard comparison instructions for the simulation.
 */
public class BranchInstructions {

    private final ISimulator simulator;

    /**
     * Some instructions require access to the Simulator directly, so that is provided.
     *
     * @param simulator the provided Simulator.
     */
    public BranchInstructions(ISimulator simulator) {
        this.simulator = simulator;
    }

    /**
     * Template branch operation.
     *
     * @param op     operation to apply to the arguments.
     * @param label  the destination label.
     * @param input1 the left-hand side of the operation.
     * @param input2 the right-hand side of the operation.
     */
    private void branch(BiFunction<Long, Long, Boolean> op, IAbstractInput label, IAbstractInput input1,
                        IAbstractInput input2) throws SimulationException {

        boolean res = op.apply(Conversion.bytesToLong(input1.get(simulator)),
                Conversion.bytesToLong(input2.get(simulator)));
        if (res) simulator.getRegisters().getRegister(Registers.PC).setBytes(label.get(simulator));
    }

    /**
     * The standard branch if equals operation.
     *
     * @param label  the destination label
     * @param input1 the left-hand side of the equals operation.
     * @param input2 the right-hand side of the equals operation.
     * @throws SimulationException if there is an error in accessing the simulation.
     */
    @Instruction
    public void beq(IAbstractInput label, IAbstractInput input1, IAbstractInput input2) throws SimulationException {
        branch(Long::equals, label, input1, input2);
    }

    /**
     * The standard branch if not equals operation.
     *
     * @param label  the destination label
     * @param input1 the left-hand side of the not equals operation.
     * @param input2 the right-hand side of the not equals operation.
     * @throws SimulationException if there is an error in accessing the simulation.
     */
    @Instruction
    public void bne(IAbstractInput label, IAbstractInput input1, IAbstractInput input2) throws SimulationException {
        branch((l, r) -> l.longValue() != r.longValue(), label, input1, input2);
    }

    /**
     * The standard branch if less than operation.
     *
     * @param label  the destination label
     * @param input1 the left-hand side of the less than operation.
     * @param input2 the right-hand side of the less than operation.
     * @throws SimulationException if there is an error in accessing the simulation.
     */
    @Instruction
    public void blt(IAbstractInput label, IAbstractInput input1, IAbstractInput input2) throws SimulationException {
        branch((l, r) -> l < r, label, input1, input2);
    }

    /**
     * The standard branch if less than or equal to operation.
     *
     * @param label  the destination label
     * @param input1 the left-hand side of the less than or equal to operation.
     * @param input2 the right-hand side of the less than or equal to operation.
     * @throws SimulationException if there is an error in accessing the simulation.
     */
    @Instruction
    public void ble(IAbstractInput label, IAbstractInput input1, IAbstractInput input2) throws SimulationException {
        branch((l, r) -> l <= r, label, input1, input2);
    }

    /**
     * The standard branch if greater than operation.
     *
     * @param label  the destination label
     * @param input1 the left-hand side of the greater than operation.
     * @param input2 the right-hand side of the greater than operation.
     * @throws SimulationException if there is an error in accessing the simulation.
     */
    @Instruction
    public void bgt(IAbstractInput label, IAbstractInput input1, IAbstractInput input2) throws SimulationException {
        branch((l, r) -> l > r, label, input1, input2);
    }

    /**
     * The standard branch if greater than or equal to operation.
     *
     * @param label  the destination label
     * @param input1 the left-hand side of the greater than or equal to operation.
     * @param input2 the right-hand side of the greater than or equal to operation.
     * @throws SimulationException if there is an error in accessing the simulation.
     */
    @Instruction
    public void bge(IAbstractInput label, IAbstractInput input1, IAbstractInput input2) throws SimulationException {
        branch((l, r) -> l >= r, label, input1, input2);
    }
}