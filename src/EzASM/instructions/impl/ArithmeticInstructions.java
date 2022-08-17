package EzASM.instructions.impl;

import EzASM.Conversion;
import EzASM.Simulator;
import EzASM.instructions.Instruction;
import EzASM.instructions.targets.input.AbstractInput;
import EzASM.instructions.targets.input.ImmediateInput;
import EzASM.instructions.targets.input.RegisterInput;
import EzASM.instructions.targets.output.AbstractOutput;
import EzASM.instructions.targets.output.RegisterOutput;

import java.util.function.BinaryOperator;
import java.util.stream.Stream;

public class ArithmeticInstructions {

    private final Simulator simulator;
    public ArithmeticInstructions(Simulator simulator) {
        this.simulator = simulator;
    }

    private void arithmetic(BinaryOperator<Long> op, AbstractInput input1, AbstractInput input2, AbstractOutput output) {
        long res = Stream.of(input1, input2)
                .map((AbstractInput r) -> r.get(this.simulator))
                .map(Conversion::bytesToLong)
                .reduce(0L, op);
        output.set(this.simulator, Conversion.longToBytes(res));
    }

    @Instruction
    public void add(AbstractInput input1, AbstractInput input2, AbstractOutput output) {
        arithmetic(Long::sum, input1, input2, output);
    }

    @Instruction
    public void sub(AbstractInput input1, AbstractInput input2, AbstractOutput output) {
        arithmetic((a, b) -> a - b, input1, input2, output);
    }

    @Instruction
    public void mul(AbstractInput input1, AbstractInput input2, AbstractOutput output) {
        arithmetic((a, b) -> a * b, input1, input2, output);
    }

    @Instruction
    public void div(AbstractInput input1, AbstractInput input2, AbstractOutput output) {
        arithmetic((a, b) -> a / b, input1, input2, output);
    }

    @Instruction
    public void and(AbstractInput input1, AbstractInput input2, AbstractOutput output) {
        arithmetic((a, b) -> a & b, input1, input2, output);
    }

    @Instruction
    public void or(AbstractInput input1, AbstractInput input2, AbstractOutput output) {
        arithmetic((a, b) -> a | b, input1, input2, output);
    }

    @Instruction
    public void not(AbstractInput input, AbstractOutput output) {
        byte[] bytes = input.get(this.simulator);
        long val = Conversion.bytesToLong(bytes);
        val = ~val;
        output.set(this.simulator, Conversion.longToBytes(val));
    }

    @Instruction
    public void sll(AbstractInput input1, AbstractInput input2, AbstractOutput output) {
        arithmetic((a, b) -> a << b, input1, input2, output);
    }

    @Instruction
    public void slr(AbstractInput input1, AbstractInput input2, AbstractOutput output) {
        arithmetic((a, b) -> a >> b, input1, input2, output);
    }

    @Instruction
    public void inc(RegisterInput input) {
        input.mutate(this.simulator, v -> Conversion.longToBytes(Conversion.bytesToLong(v) + 1));
    }

    @Instruction
    public void inc(RegisterOutput output, ImmediateInput input2) {
        output.set(this.simulator, Conversion.longToBytes(Conversion.bytesToLong(input2.get(this.simulator)) + 1));
    }
}
