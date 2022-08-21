package EzASM.instructions.targets.input;

import EzASM.simulation.Simulator;

import java.util.Arrays;
import java.util.function.Function;

public class RegisterInput extends AbstractInput {

    private final int register;

    public RegisterInput(int register) {
        this.register = register;
    }

    @Override
    public byte[] get(Simulator simulator) {
        byte[] val = simulator.getRegister(register).getBytes();
        return Arrays.copyOf(val, val.length);
    }

    public void mutate(Simulator simulator, Function<byte[], byte[]> mutator) {
        byte[] val = simulator.getRegister(register).getBytes();
        byte[] mutate = mutator.apply(val);
        simulator.getRegister(register).setBytes(mutate);
    }

}
