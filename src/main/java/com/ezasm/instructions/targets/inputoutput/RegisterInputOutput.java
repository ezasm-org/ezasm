package com.ezasm.instructions.targets.inputoutput;

import com.ezasm.simulation.ISimulator;
import com.ezasm.simulation.Registers;
import com.ezasm.util.RawData;

import java.util.Objects;

/**
 * The implementation of a register to be used as either an input or an output.
 */
public class RegisterInputOutput implements IAbstractInputOutput {

    private final int register;

    /**
     * Construct the output based on the register reference number.
     *
     * @param register the register reference number.
     */
    public RegisterInputOutput(int register) {
        this.register = register;
    }

    /**
     * Construct the output based on the register reference string.
     *
     * @param register the register reference string.
     */
    public RegisterInputOutput(String register) {
        this.register = Registers.getRegisterNumber(register);
    }

    /**
     * Gets the value stored within the register.
     *
     * @param simulator the program simulator.
     * @return the value stored within the register.
     */
    @Override
    public RawData get(ISimulator simulator) {
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
    public void set(ISimulator simulator, RawData value) {
        simulator.getRegisters().getRegister(register).setDataInsideInstruction(value);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o)
            return true;
        if (o == null || getClass() != o.getClass())
            return false;
        RegisterInputOutput that = (RegisterInputOutput) o;
        return register == that.register;
    }

    @Override
    public int hashCode() {
        return Objects.hash(register);
    }
}
