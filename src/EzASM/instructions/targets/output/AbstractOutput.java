package EzASM.instructions.targets.output;

import EzASM.Simulator;

public abstract class AbstractOutput {

    public abstract void set(Simulator simulator, byte[] value);

}
