package com.ezasm.instructions.impl;

import com.ezasm.Conversion;
import com.ezasm.instructions.targets.input.IAbstractInput;
import com.ezasm.instructions.targets.inputoutput.IAbstractInputOutput;
import com.ezasm.instructions.targets.output.IAbstractOutput;
import com.ezasm.simulation.Simulator;
import com.ezasm.instructions.Instruction;
import com.ezasm.instructions.exception.IllegalArgumentException;
import com.ezasm.instructions.targets.input.ImmediateInput;

import java.util.function.BinaryOperator;

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
    private void arithmetic(BinaryOperator<Long> op, IAbstractOutput output, IAbstractInput input1,
            IAbstractInput input2) {

        long res = op.apply(Conversion.bytesToLong(input1.get(simulator)),
                Conversion.bytesToLong(input2.get(simulator)));
        output.set(this.simulator, Conversion.longToBytes(res));
    }

    /**
     * The standard add operation.
     *
     * @param output the output of the operation.
     * @param input1 the left-hand side of the addition operation.
     * @param input2 the right-hand side of the addition operation.
     */
    @Instruction
    public void add(IAbstractOutput output, IAbstractInput input1, IAbstractInput input2) {
        arithmetic(Long::sum, output, input1, input2);
    }

    /**
     * The standard subtract operation.
     *
     * @param output the output of the operation.
     * @param input1 the left-hand side of the subtraction operation.
     * @param input2 the right-hand side of the subtraction operation.
     */
    @Instruction
    public void sub(IAbstractOutput output, IAbstractInput input1, IAbstractInput input2) {
        arithmetic((a, b) -> a - b, output, input1, input2);
    }

    /**
     * The standard multiply operation.
     *
     * @param output the output of the operation.
     * @param input1 the left-hand side of the multiply operation.
     * @param input2 the right-hand side of the multiply operation.
     */
    @Instruction
    public void mul(IAbstractOutput output, IAbstractInput input1, IAbstractInput input2) {
        arithmetic((a, b) -> a * b, output, input1, input2);
    }

    /**
     * The standard divide operation.
     *
     * @param output the output of the operation.
     * @param input1 the left-hand side of the divide operation.
     * @param input2 the right-hand side of the divide operation.
     */
    @Instruction
    public void div(IAbstractOutput output, IAbstractInput input1, IAbstractInput input2) {
        if (Conversion.bytesToLong(input2.get(simulator)) == 0) {
            throw new IllegalArgumentException(-1);
        }
        arithmetic((a, b) -> a / b, output, input1, input2);
    }

    /**
     * The standard and operation.
     *
     * @param output the output of the operation.
     * @param input1 the left-hand side of the and operation.
     * @param input2 the right-hand side of the and operation.
     */
    @Instruction
    public void and(IAbstractOutput output, IAbstractInput input1, IAbstractInput input2) {
        arithmetic((a, b) -> a & b, output, input1, input2);
    }

    /**
     * The standard or operation.
     *
     * @param output the output of the operation.
     * @param input1 the left-hand side of the or operation.
     * @param input2 the right-hand side of the or operation.
     */
    @Instruction
    public void or(IAbstractOutput output, IAbstractInput input1, IAbstractInput input2) {
        arithmetic((a, b) -> a | b, output, input1, input2);
    }

    /**
     * The standard xor operation.
     *
     * @param input1 the left-hand side of the xor operation.
     * @param input2 the right-hand side of the xor operation.
     * @param output the output of the operation.
     */
    @Instruction
    public void xor(IAbstractOutput output, IAbstractInput input1, IAbstractInput input2) {
        arithmetic((a, b) -> a ^ b, output, input1, input2);
    }

    /**
     * The standard not operation. Inverts all bits in the input.
     *
     * @param output the output of the operation.
     * @param input  the input of the not operation.
     */
    @Instruction
    public void not(IAbstractOutput output, IAbstractInput input) {
        byte[] bytes = input.get(this.simulator);
        long val = Conversion.bytesToLong(bytes);
        val = ~val;
        output.set(this.simulator, Conversion.longToBytes(val));
    }

    /**
     * The standard "shift left logical" operation.
     *
     * @param output the output of the operation.
     * @param input1 the left-hand side of the sll operation.
     * @param input2 the right-hand side of the sll operation.
     */
    @Instruction
    public void sll(IAbstractOutput output, IAbstractInput input1, IAbstractInput input2) {
        arithmetic((a, b) -> a << b, output, input1, input2);
    }

    /**
     * The standard "shift right logical" operation.
     *
     * @param output the output of the operation.
     * @param input1 the left-hand side of the srl operation.
     * @param input2 the right-hand side of the srl operation.
     */
    @Instruction
    public void srl(IAbstractOutput output, IAbstractInput input1, IAbstractInput input2) {
        arithmetic((a, b) -> a >> b, output, input1, input2);
    }

    /**
     * The standard decrement operation. Subtracts one to the register's data.
     *
     * @param input the register to be modified.
     */
    @Instruction
    public void dec(IAbstractInputOutput input) {
        input.set(this.simulator, Conversion.longToBytes(Conversion.bytesToLong(input.get(this.simulator)) - 1));
    }

    /**
     * The standard increment operation. Adds one to the register's data.
     *
     * @param input the input/output to be modified.
     */
    @Instruction
    public void inc(IAbstractInputOutput input) {
        input.set(this.simulator, Conversion.longToBytes(Conversion.bytesToLong(input.get(this.simulator)) + 1));
    }

    /**
     * The standard increment operation. Adds one to the register's data.
     *
     * @param output the output register of the increment.
     * @param input  the input of the increment.
     */
    @Instruction
    public void inc(IAbstractOutput output, IAbstractInput input) {
        output.set(this.simulator, Conversion.longToBytes(Conversion.bytesToLong(input.get(this.simulator)) + 1));
    }
}
