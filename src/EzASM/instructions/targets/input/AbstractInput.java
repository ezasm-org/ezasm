package EzASM.instructions.targets.input;

import EzASM.simulation.Simulator;

public abstract class AbstractInput {

    public abstract byte[] get(Simulator simulator);

}
