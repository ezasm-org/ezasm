package com.ezasm.instructions.targets.inputoutput;

import com.ezasm.simulation.Memory;
import com.ezasm.simulation.Registers;
import com.ezasm.simulation.Simulator;
import com.ezasm.simulation.exception.MisalignedStackPointerException;
import com.ezasm.simulation.exception.SimulationStackOverflowException;
import com.ezasm.util.RawData;
import jdk.incubator.vector.ByteVector;
import jdk.incubator.vector.IntVector;
import jdk.incubator.vector.LongVector;
import jdk.incubator.vector.VectorSpecies;

import java.util.Objects;

public class VectorInputOutput implements IAbstractInputOutput {
    private final int register;
    public static final int WORD_LENGTH = 8;


    /**
     * Construct the output based on the register reference number.
     *
     * @param register the register reference number.
     */
    public VectorInputOutput(int register) {
        this.register = register;
    }

    /**
     * Construct the output based on the register reference string.
     *
     * @param register the register reference string.
     */
    public VectorInputOutput(String register) {
        this.register = Registers.getRegisterNumber(register);
    }

    /**
     * Gets the value stored within the register.
     *
     * @param simulator the program simulator.
     * @return the value stored within the register.
     */
    @Override
    public RawData get(Simulator simulator) {
        RawData val = simulator.getRegisters().getRegister(register).getData();
        return val.copy();
    }

    /**
     * Sets the value stored within the register.
     *
     * @param simulator the program simulator.
     * @param value     the value to set.
     */
    @Override
    public void set(Simulator simulator, RawData value) {
        simulator.getRegisters().getRegister(register).setDataWithGuiCallback(value);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o)
            return true;
        if (o == null || getClass() != o.getClass())
            return false;
        VectorInputOutput that = (VectorInputOutput) o;
        return register == that.register;
    }

    @Override
    public int hashCode() {
        return Objects.hash(register);
    }
}
