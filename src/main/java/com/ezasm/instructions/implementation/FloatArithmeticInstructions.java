package com.ezasm.instructions.implementation;

import com.ezasm.simulation.TransformationSequence;
import com.ezasm.util.Conversion;
import com.ezasm.instructions.targets.input.IAbstractInput;
import com.ezasm.instructions.targets.inputoutput.IAbstractInputOutput;
import com.ezasm.simulation.ISimulator;
import com.ezasm.instructions.Instruction;
import com.ezasm.instructions.exception.IllegalArgumentException;
import com.ezasm.simulation.exception.SimulationException;
import com.ezasm.util.Operations;

import java.util.function.BinaryOperator;
import java.util.function.UnaryOperator;

/**
 * An implementation of standard arithmetic instructions for the simulation.
 */
public class FloatArithmeticInstructions {

    private final ISimulator simulator;

    /**
     * Some instructions require access to the Simulator directly, so that is provided.
     *
     * @param simulator the provided Simulator.
     */
    public FloatArithmeticInstructions(ISimulator simulator) {
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
    private TransformationSequence floatArithmetic(BinaryOperator<Double> op, IAbstractInputOutput output,
            IAbstractInput input1, IAbstractInput input2) throws SimulationException {

        byte[] res = Operations.applyToFloatBytes(op, input1.get(simulator), input2.get(simulator));
        return new TransformationSequence(output.transformation(simulator, res));
    }

    /**
     * Template unary operation.
     *
     * @param op     operation to apply to the arguments.
     * @param output the output of the operation.
     * @param input  the input of the operation.
     */
    private TransformationSequence unaryFloatOperation(UnaryOperator<Double> op, IAbstractInputOutput output,
            IAbstractInput input) throws SimulationException {
        double res = op.apply(Conversion.bytesToDouble(input.get(simulator)));
        return new TransformationSequence(output.transformation(simulator, Conversion.doubleToBytes(res)));
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
    public TransformationSequence addf(IAbstractInputOutput output, IAbstractInput input1, IAbstractInput input2)
            throws SimulationException {
        return floatArithmetic(Double::sum, output, input1, input2);
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
    public TransformationSequence subf(IAbstractInputOutput output, IAbstractInput input1, IAbstractInput input2)
            throws SimulationException {
        return floatArithmetic((a, b) -> a - b, output, input1, input2);
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
    public TransformationSequence mulf(IAbstractInputOutput output, IAbstractInput input1, IAbstractInput input2)
            throws SimulationException {
        return floatArithmetic((a, b) -> a * b, output, input1, input2);
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
    public TransformationSequence divf(IAbstractInputOutput output, IAbstractInput input1, IAbstractInput input2)
            throws SimulationException {
        double f = Conversion.bytesToDouble(input2.get(simulator));
        // Ensure the number is not "NaN" "infinity" or extremely close to zero (probably zero with rounding error)
        if (Double.isNaN(f) || Double.isInfinite(f) || (f >= -1e-15 || f <= 1e-15)) {
            throw new IllegalArgumentException(-1);
        }
        return floatArithmetic((a, b) -> a / b, output, input1, input2);
    }

    /**
     * The standard decrement operation. Subtracts one from the given data.
     *
     * @param output the output of the operation.
     * @param input  the input of the operation.
     * @throws SimulationException if there is an error in accessing the simulation.
     */
    @Instruction
    public TransformationSequence decf(IAbstractInputOutput output, IAbstractInput input) throws SimulationException {
        return unaryFloatOperation((a) -> a - 1, output, input);
    }

    /**
     * The standard increment operation. Adds one to the given data.
     *
     * @param output the output of the operation.
     * @param input  the input of the operation.
     * @throws SimulationException if there is an error in accessing the simulation.
     */
    @Instruction
    public TransformationSequence incf(IAbstractInputOutput output, IAbstractInput input) throws SimulationException {
        return unaryFloatOperation((a) -> a + 1, output, input);
    }

    /**
     * Takes an input containing a long and changes the representation of it to a double
     *
     * @param output the output of the operation.
     * @param input  the input of the operation.
     * @throws SimulationException if there is an error in accessing the simulation.
     */
    @Instruction
    public TransformationSequence itof(IAbstractInputOutput output, IAbstractInput input) throws SimulationException {
        double f = (double) Conversion.bytesToLong(input.get(simulator));
        return new TransformationSequence(output.transformation(simulator, Conversion.doubleToBytes(f)));
    }

    /**
     * Takes an input containing a double and changes the representation of it to a floored long
     *
     * @param output the output of the operation.
     * @param input  the input of the operation.
     * @throws SimulationException if there is an error in accessing the simulation.
     */
    @Instruction
    public TransformationSequence ftoi(IAbstractInputOutput output, IAbstractInput input) throws SimulationException {
        long i = (long) Conversion.bytesToDouble(input.get(simulator));
        return new TransformationSequence(output.transformation(simulator, Conversion.longToBytes(i)));
    }
}
