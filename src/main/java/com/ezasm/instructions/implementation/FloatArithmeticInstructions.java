package com.ezasm.instructions.implementation;

import com.ezasm.util.Conversion;
import com.ezasm.instructions.targets.input.IAbstractInput;
import com.ezasm.instructions.targets.inputoutput.IAbstractInputOutput;
import com.ezasm.instructions.targets.output.IAbstractOutput;
import com.ezasm.simulation.ISimulator;
import com.ezasm.instructions.Instruction;
import com.ezasm.instructions.exception.IllegalArgumentException;
import com.ezasm.simulation.exception.SimulationException;

import java.util.function.BinaryOperator;

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
    private void floatArithmetic(BinaryOperator<Double> op, IAbstractOutput output, IAbstractInput input1,
            IAbstractInput input2) throws SimulationException {

        double res = op.apply(Conversion.bytesToDouble(input1.get(simulator)),
                Conversion.bytesToDouble(input2.get(simulator)));
        output.set(this.simulator, Conversion.doubleToBytes(res));
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
    public void addf(IAbstractOutput output, IAbstractInput input1, IAbstractInput input2) throws SimulationException {
        floatArithmetic(Double::sum, output, input1, input2);
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
    public void subf(IAbstractOutput output, IAbstractInput input1, IAbstractInput input2) throws SimulationException {
        floatArithmetic((a, b) -> a - b, output, input1, input2);
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
    public void mulf(IAbstractOutput output, IAbstractInput input1, IAbstractInput input2) throws SimulationException {
        floatArithmetic((a, b) -> a * b, output, input1, input2);
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
    public void divf(IAbstractOutput output, IAbstractInput input1, IAbstractInput input2) throws SimulationException {
        if (Conversion.bytesToLong(input2.get(simulator)) == 0) {
            throw new IllegalArgumentException(-1);
        }
        floatArithmetic((a, b) -> a / b, output, input1, input2);
    }

    /**
     * The standard decrement operation. Subtracts one from the given data.
     *
     * @param input the input/output to be modified.
     * @throws SimulationException if there is an error in accessing the simulation.
     */
    @Instruction
    public void decf(IAbstractInputOutput input) throws SimulationException {
        input.set(this.simulator, Conversion.longToBytes(Conversion.bytesToLong(input.get(this.simulator)) - 1));
    }

    /**
     * The standard increment operation. Adds one to the given data.
     *
     * @param input the input/output to be modified.
     * @throws SimulationException if there is an error in accessing the simulation.
     */
    @Instruction
    public void incf(IAbstractInputOutput input) throws SimulationException {
        input.set(this.simulator, Conversion.longToBytes(Conversion.bytesToLong(input.get(this.simulator)) + 1));
    }

    /**
     * Takes an input containing a long and changes the representation of it to a double
     *
     * @param input the input/output to be modified
     * @throws SimulationException if there is an error in accessing the simulation.
     */
    @Instruction
    public void intof(IAbstractInputOutput input) throws SimulationException {
        long i = Conversion.bytesToLong(input.get(simulator));
        input.set(simulator, Conversion.doubleToBytes((double) i));
    }

    /**
     * Takes an input containing a double and changes the representation of it to a floored long
     *
     * @param input the input/output to be modified
     * @throws SimulationException if there is an error in accessing the simulation.
     */
    @Instruction
    public void floor(IAbstractInputOutput input) throws SimulationException {
        long i = (long) Math.floor(Conversion.bytesToDouble(input.get(simulator)));
        input.set(simulator, Conversion.longToBytes(i));
    }
}
