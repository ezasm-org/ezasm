package com.ezasm.instructions.targets.output;

import com.ezasm.simulation.Simulator;

/**
 * The implementation of a register to be used as an output.
 */
public class RegisterOutput extends AbstractOutput {

    private final int register;

    /**
     * Construct the output based on the register reference number.
     * @param register the register reference number.
     */
    public RegisterOutput(int register) {
        this.register = register;
    }

    /**
     * Sets the value stored within the register.
     * @param simulator the program simulator.
     * @param value the value to set.
     */
    @Override
    public void set(Simulator simulator, byte[] value) {
        simulator.getRegister(register).setBytes(value);
    }
}
