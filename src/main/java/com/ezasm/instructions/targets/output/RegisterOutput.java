package com.ezasm.instructions.targets.output;

import com.ezasm.simulation.ISimulator;

/**
 * The implementation of a register to be used as an output.
 */
public class RegisterOutput implements IAbstractOutput {

    private final int register;

    /**
     * Construct the output based on the register reference number.
     *
     * @param register the register reference number.
     */
    public RegisterOutput(int register) {
        this.register = register;
    }

    /**
     * Sets the value stored within the register.
     *
     * @param simulator the program simulator.
     * @param value     the value to set.
     */
    @Override
    public void set(ISimulator simulator, byte[] value) {
        simulator.getRegisters().getRegister(register).setBytes(value);
    }
}
