package com.ezasm.simulation.transform.transformable;

import com.ezasm.simulation.Simulator;
import com.ezasm.simulation.exception.SimulationException;
import com.ezasm.util.RawData;

public class FileTransformable extends AbstractTransformableInput {

    private final String to;
    boolean fromToTo;

    public FileTransformable(Simulator simulator, String to) {
        super(simulator);
        this.to = to;
        this.fromToTo = true;
    }

    // Zero means that we go from-to-to, one means that we go to-to-from
    @Override
    public void set(RawData data) throws SimulationException {
        if (data.intValue() == 0) { // switch to the "to" file
            simulator.pushFileSwitch(to);
            fromToTo = false;
        } else { // switch to the "from" file
            simulator.popFileSwitch();
            fromToTo = true;
        }
    }

    @Override
    public RawData get() throws SimulationException {
        if (fromToTo) {
            return new RawData(1);
        } else {
            return new RawData(0);
        }
    }
}
