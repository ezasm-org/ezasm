package com.ezasm.instructions.implementation;

import com.ezasm.simulation.transform.TransformationSequence;
import com.ezasm.simulation.transform.transformable.InputOutputTransformable;
import com.ezasm.instructions.targets.input.IAbstractInput;
import com.ezasm.instructions.targets.inputoutput.IAbstractInputOutput;
import com.ezasm.simulation.Simulator;
import com.ezasm.instructions.Instruction;
import com.ezasm.instructions.exception.IllegalArgumentException;
import com.ezasm.simulation.exception.SimulationException;
import com.ezasm.util.RawData;

import java.util.function.BinaryOperator;
import java.util.function.UnaryOperator;

/**
 * An implementation of standard arithmetic instructions for the simulation.
 */
public class ArithmeticInstructions {

    private final Simulator simulator;

    /**
     * Some instructions require access to the Simulator directly, so that is provided.
     *
     * @param simulator the provided Simulator.
     */
    public ArithmeticInstructions(Simulator simulator) {
        this.simulator = simulator;
    }

    /**
     * Template arithmetic operation.
     *
     * @param op     operation to apply to the arguments.
     * @param output the output of the operation.
     * @param input1 the left-hand side of the operation.
     * @param input2 the right-hand side of the operation.
     */
    private TransformationSequence arithmetic(BinaryOperator<Long> op, IAbstractInputOutput output,
            IAbstractInput input1, IAbstractInput input2) throws SimulationException {

        long res = op.apply(input1.get(simulator).intValue(), input2.get(simulator).intValue());
        InputOutputTransformable io = new InputOutputTransformable(simulator, output);
        return new TransformationSequence(io.transformation(new RawData(res)));
    }

    /**
     * Template unary operation.
     *
     * @param op     operation to apply to the arguments.
     * @param output the output of the operation.
     * @param input  the input of the operation.
     */
    private TransformationSequence unaryOperation(UnaryOperator<Long> op, IAbstractInputOutput output,
            IAbstractInput input) throws SimulationException {

        long res = op.apply(input.get(simulator).intValue());
        InputOutputTransformable io = new InputOutputTransformable(simulator, output);
        return new TransformationSequence(io.transformation(new RawData(res)));
    }

    /**
     * The standard add operation.
     *
     * @param output the output of the operation.
     * @param input1 the left-hand side of the addition operation.
     * @param input2 the right-hand side of the addition operation.
     * @throws SimulationException if there is an error in accessing the simulation.
     */
    @Instruction
    public TransformationSequence add(IAbstractInputOutput output, IAbstractInput input1, IAbstractInput input2)
            throws SimulationException {
        return arithmetic(Long::sum, output, input1, input2);
    }

    /**
     * The standard subtract operation.
     *
     * @param output the output of the operation.
     * @param input1 the left-hand side of the subtraction operation.
     * @param input2 the right-hand side of the subtraction operation.
     * @throws SimulationException if there is an error in accessing the simulation.
     */
    @Instruction
    public TransformationSequence sub(IAbstractInputOutput output, IAbstractInput input1, IAbstractInput input2)
            throws SimulationException {
        return arithmetic((a, b) -> a - b, output, input1, input2);
    }

    /**
     * The standard multiply operation.
     *
     * @param output the output of the operation.
     * @param input1 the left-hand side of the multiply operation.
     * @param input2 the right-hand side of the multiply operation.
     * @throws SimulationException if there is an error in accessing the simulation.
     */
    @Instruction
    public TransformationSequence mul(IAbstractInputOutput output, IAbstractInput input1, IAbstractInput input2)
            throws SimulationException {
        return arithmetic((a, b) -> a * b, output, input1, input2);
    }

    /**
     * The standard divide operation.
     *
     * @param output the output of the operation.
     * @param input1 the left-hand side of the divide operation.
     * @param input2 the right-hand side of the divide operation.
     * @throws SimulationException if there is an error in accessing the simulation.
     */
    @Instruction
    public TransformationSequence div(IAbstractInputOutput output, IAbstractInput input1, IAbstractInput input2)
            throws SimulationException {
        if (input2.get(simulator).intValue() == 0) {
            throw new IllegalArgumentException(-1);
        }
        return arithmetic((a, b) -> a / b, output, input1, input2);
    }

    /**
     * The standard and operation.
     *
     * @param output the output of the operation.
     * @param input1 the left-hand side of the and operation.
     * @param input2 the right-hand side of the and operation.
     * @throws SimulationException if there is an error in accessing the simulation.
     */
    @Instruction
    public TransformationSequence and(IAbstractInputOutput output, IAbstractInput input1, IAbstractInput input2)
            throws SimulationException {
        return arithmetic((a, b) -> a & b, output, input1, input2);
    }

    /**
     * The standard or operation.
     *
     * @param output the output of the operation.
     * @param input1 the left-hand side of the or operation.
     * @param input2 the right-hand side of the or operation.
     * @throws SimulationException if there is an error in accessing the simulation.
     */
    @Instruction
    public TransformationSequence or(IAbstractInputOutput output, IAbstractInput input1, IAbstractInput input2)
            throws SimulationException {
        return arithmetic((a, b) -> a | b, output, input1, input2);
    }

    /**
     * The standard xor operation.
     *
     * @param output the output of the operation.
     * @param input1 the left-hand side of the xor operation.
     * @param input2 the right-hand side of the xor operation.
     * @throws SimulationException if there is an error in accessing the simulation.
     */
    @Instruction
    public TransformationSequence xor(IAbstractInputOutput output, IAbstractInput input1, IAbstractInput input2)
            throws SimulationException {
        return arithmetic((a, b) -> a ^ b, output, input1, input2);
    }

    /**
     * The standard modulus operation.
     *
     * @param output the output of the operation.
     * @param input1 the left-hand side of the modullus operation.
     * @param input2 the right-hand side of the modulus operation.
     * @throws SimulationException if there is an error in accessing the simulation.
     */
    @Instruction
    public TransformationSequence mod(IAbstractInputOutput output, IAbstractInput input1, IAbstractInput input2)
            throws SimulationException {
        return arithmetic((a, b) -> a % b, output, input1, input2);
    }

    /**
     * The standard not operation. Inverts all bits in the input.
     *
     * @param output the output of the operation.
     * @param input  the input of the not operation.
     * @throws SimulationException if there is an error in accessing the simulation.
     */
    @Instruction
    public TransformationSequence not(IAbstractInputOutput output, IAbstractInput input) throws SimulationException {
        return unaryOperation((a) -> ~a, output, input);
    }

    /**
     * The standard "shift left logical" operation.
     *
     * @param output the output of the operation.
     * @param input1 the left-hand side of the sll operation.
     * @param input2 the right-hand side of the sll operation.
     * @throws SimulationException if there is an error in accessing the simulation.
     */
    @Instruction
    public TransformationSequence sll(IAbstractInputOutput output, IAbstractInput input1, IAbstractInput input2)
            throws SimulationException {
        return arithmetic((a, b) -> a << b, output, input1, input2);
    }

    /**
     * The standard "shift right logical" operation.
     *
     * @param output the output of the operation.
     * @param input1 the left-hand side of the srl operation.
     * @param input2 the right-hand side of the srl operation.
     * @throws SimulationException if there is an error in accessing the simulation.
     */
    @Instruction
    public TransformationSequence srl(IAbstractInputOutput output, IAbstractInput input1, IAbstractInput input2)
            throws SimulationException {
        return arithmetic((a, b) -> a >> b, output, input1, input2);
    }

    /**
     * The standard decrement operation. Subtracts one from the given data.
     *
     * @param output the output of the operation.
     * @param input  the input of the operation.
     * @throws SimulationException if there is an error in accessing the simulation.
     */
    @Instruction
    public TransformationSequence dec(IAbstractInputOutput output, IAbstractInput input) throws SimulationException {
        return unaryOperation((a) -> a - 1, output, input);
    }

    /**
     * The standard increment operation. Adds one to the given data.
     *
     * @param output the output of the operation.
     * @param input  the input of the operation.
     * @throws SimulationException if there is an error in accessing the simulation.
     */
    @Instruction
    public TransformationSequence inc(IAbstractInputOutput output, IAbstractInput input) throws SimulationException {
        return unaryOperation((a) -> a + 1, output, input);
    }
}
