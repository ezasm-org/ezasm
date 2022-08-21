package EzASM.instructions.targets.input;

import EzASM.simulation.Simulator;

public class ImmediateInput extends AbstractInput {

    private final byte[] value;
    public ImmediateInput(byte[] value) {
        this.value = value;
    }

    @Override
    public byte[] get(Simulator simulator) {
        return value;
    }
}
