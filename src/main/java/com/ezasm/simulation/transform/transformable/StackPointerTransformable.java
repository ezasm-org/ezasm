package com.ezasm.simulation.transform.transformable;

import com.ezasm.simulation.Simulator;
import com.ezasm.simulation.exception.MisalignedStackPointerException;
import com.ezasm.simulation.exception.SimulationStackOverflowException;
import com.ezasm.util.RawData;

public class StackPointerTransformable extends AbstractTransformableInput {
    /**
     * Returns a representation of the transformable stack pointer.
     *
     * @param simulator the simulator to use.
     */
    public StackPointerTransformable(Simulator simulator) {
        super(simulator);
    }

    /**
     * Gets the stack pointer's value as raw bytes.
     *
     * @return the stack pointer's value as raw bytes.
     */
    @Override
    public RawData get() {
        return new RawData(simulator.getRegisters().getRegister("SP").getLong());
    }

    /**
     * Sets the stack pointer's value.
     *
     * @param value the stack pointer's new value.
     */
    @Override
    public void set(RawData value) throws MisalignedStackPointerException, SimulationStackOverflowException {
        if (value.intValue() % simulator.getMemory().wordSize != 0) {
            throw new MisalignedStackPointerException(value.intValue());
        } else if (value.intValue() <= simulator.getMemory().currentHeapPointer()) {
            throw new SimulationStackOverflowException(value.intValue());
        }
        simulator.getRegisters().getRegister("SP").setDataWithGuiCallback(new RawData((int) value.intValue()));
    }
}
