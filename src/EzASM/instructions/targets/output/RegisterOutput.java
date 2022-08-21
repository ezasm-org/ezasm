package EzASM.instructions.targets.output;

import EzASM.simulation.Simulator;

public class RegisterOutput extends AbstractOutput {

    private final int register;

    public RegisterOutput(int register) {
        this.register = register;
    }

    @Override
    public void set(Simulator simulator, byte[] value) {
        simulator.getRegister(register).setBytes(value);
    }
}
