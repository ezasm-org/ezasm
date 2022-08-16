package EzASM.instructions.targets.input;

import EzASM.Registers;
import EzASM.Simulator;

import java.util.Arrays;
import java.util.function.Function;

public class RegisterInput extends AbstractInput {

    private final int register;

    public RegisterInput(int register) {
        this.register = register;
    }

    @Override
    public byte[] get(Simulator simulator) {
        var val = simulator.getRegister(register).getBytes();
        return Arrays.copyOf(val, val.length);
    }

    public void mutate(Simulator simulator, Function<byte[], byte[]> mutator) {
        var val = simulator.getRegister(register).getBytes();
        var mutate = mutator.apply(val);
        simulator.getRegister(register).setBytes(mutate);
    }

}
