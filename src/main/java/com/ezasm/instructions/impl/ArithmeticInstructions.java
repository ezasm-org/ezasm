package com.ezasm.instructions.impl;

import com.ezasm.Conversion;
import com.ezasm.simulation.Simulator;
import com.ezasm.instructions.Instruction;
import com.ezasm.instructions.exception.IllegalArgumentException;
import com.ezasm.instructions.targets.input.AbstractInput;
import com.ezasm.instructions.targets.input.ImmediateInput;
import com.ezasm.instructions.targets.input.RegisterInput;
import com.ezasm.instructions.targets.output.AbstractOutput;
import com.ezasm.instructions.targets.output.RegisterOutput;

import java.util.function.BinaryOperator;

/**
 * An implementation of standard arithmetic instructions for the simulation.
 */
public class ArithmeticInstructions {

    private final Simulator simulator;

    /**
     * Some instructions require access to the Simulator directly, so that is provided.
     * @param simulator the provided Simulator.
     */
    public ArithmeticInstructions(Simulator simulator) {
        this.simulator = simulator;
    }

    /**
     * Template arithmetic operation.
     * @param op operation to apply to the arguments.
     * @param input1 the left-hand side of the operation.
     * @param input2 the right-hand side of the operation.
     * @param output the output of the operation.
     */
    private void arithmetic(BinaryOperator<Long> op, AbstractInput input1, AbstractInput input2, AbstractOutput output) {

        long res = op.apply(Conversion.bytesToLong(input1.get(simulator)), Conversion.bytesToLong(input2.get(simulator)));
        output.set(this.simulator, Conversion.longToBytes(res));
    }

    /**
     * The standard add operation.
     * @param input1 the left-hand side of the addition operation.
     * @param input2 the right-hand side of the addition operation.
     * @param output the output of the operation.
     */
    @Instruction
    public void add(AbstractInput input1, AbstractInput input2, AbstractOutput output) {
        arithmetic(Long::sum, input1, input2, output);
    }

    /**
     * The standard subtract operation.
     * @param input1 the left-hand side of the subtraction operation.
     * @param input2 the right-hand side of the subtraction operation.
     * @param output the output of the operation.
     */
    @Instruction
    public void sub(AbstractInput input1, AbstractInput input2, AbstractOutput output) {
        arithmetic((a, b) -> a - b, input1, input2, output);
    }

    /**
     * The standard multiply operation.
     * @param input1 the left-hand side of the multiply operation.
     * @param input2 the right-hand side of the multiply operation.
     * @param output the output of the operation.
     */
    @Instruction
    public void mul(AbstractInput input1, AbstractInput input2, AbstractOutput output) {
        arithmetic((a, b) -> a * b, input1, input2, output);
    }

    /**
     * The standard divide operation.
     * @param input1 the left-hand side of the divide operation.
     * @param input2 the right-hand side of the divide operation.
     * @param output the output of the operation.
     */
    @Instruction
    public void div(AbstractInput input1, AbstractInput input2, AbstractOutput output) {
        if(Conversion.bytesToLong(input2.get(simulator)) == 0) {
            throw new IllegalArgumentException(-1);
        }
        arithmetic((a, b) -> a / b, input1, input2, output);
    }

    /**
     * The standard and operation.
     * @param input1 the left-hand side of the and operation.
     * @param input2 the right-hand side of the and operation.
     * @param output the output of the operation.
     */
    @Instruction
    public void and(AbstractInput input1, AbstractInput input2, AbstractOutput output) {
        arithmetic((a, b) -> a & b, input1, input2, output);
    }

    /**
     * The standard or operation.
     * @param input1 the left-hand side of the or operation.
     * @param input2 the right-hand side of the or operation.
     * @param output the output of the operation.
     */
    @Instruction
    public void or(AbstractInput input1, AbstractInput input2, AbstractOutput output) {
        arithmetic((a, b) -> a | b, input1, input2, output);
    }

    /**
     * The standard not operation. Inverts all bits in the input.
     * @param input the input of the not operation.
     * @param output the output of the operation.
     */
    @Instruction
    public void not(AbstractInput input, AbstractOutput output) {
        byte[] bytes = input.get(this.simulator);
        long val = Conversion.bytesToLong(bytes);
        val = ~val;
        output.set(this.simulator, Conversion.longToBytes(val));
    }

    /**
     * The standard "shift left logical" operation.
     * @param input1 the left-hand side of the sll operation.
     * @param input2 the right-hand side of the sll operation.
     * @param output the output of the operation.
     */
    @Instruction
    public void sll(AbstractInput input1, AbstractInput input2, AbstractOutput output) {
        arithmetic((a, b) -> a << b, input1, input2, output);
    }

    /**
     * The standard "shift right logical" operation.
     * @param input1 the left-hand side of the srl operation.
     * @param input2 the right-hand side of the srl operation.
     * @param output the output of the operation.
     */
    @Instruction
    public void srl(AbstractInput input1, AbstractInput input2, AbstractOutput output) {
        arithmetic((a, b) -> a >> b, input1, input2, output);
    }

    /**
     * The standard increment operation. Adds one to the register's data.
     * @param input the register to be modified.
     */
    @Instruction
    public void inc(RegisterInput input) {
        input.mutate(this.simulator, v -> Conversion.longToBytes(Conversion.bytesToLong(v) + 1));
    }

    /**
     * The standard increment operation. Adds one to the register's data.
     * @param output the output register of the increment.
     * @param input1 the input of the increment.
     */
    @Instruction
    public void inc(RegisterOutput output, ImmediateInput input1) {
        output.set(this.simulator, Conversion.longToBytes(Conversion.bytesToLong(input1.get(this.simulator)) + 1));
    }
}
