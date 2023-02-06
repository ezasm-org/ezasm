package com.ezasm.instructions.targets.inputoutput;

import com.ezasm.simulation.ISimulator;
import com.ezasm.simulation.Registers;

import java.util.Arrays;
import java.util.function.Function;

/**
 * The implementation of a register to be used as an output.
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
    public byte[] get(ISimulator simulator) {
        byte[] val = simulator.getRegisters().getRegister(register).getBytes();
        return Arrays.copyOf(val, val.length);
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

    /**
     * Mutates the register according to the mutator function given.
     *
     * @param simulator the program simulator.
     * @param mutator   the mutator function.
     */
    public void mutate(ISimulator simulator, Function<byte[], byte[]> mutator) {
        byte[] val = simulator.getRegisters().getRegister(register).getBytes();
        byte[] mutate = mutator.apply(val);
        simulator.getRegisters().getRegister(register).setBytes(mutate);
    }

}
