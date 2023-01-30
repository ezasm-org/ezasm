package com.ezasm.instructions.targets.input;

import com.ezasm.simulation.Simulator;

import java.util.Arrays;
import java.util.function.Function;

/**
 * The implementation of a register to be used as an input.
 */
public class RegisterInput extends AbstractInput {

    private final int register;

    /**
     * Constructs the input based on the register reference number.
     *
     * @param register
     *            the register reference number.
     */
    public RegisterInput(int register) {
        this.register = register;
    }

    /**
     * Gets the value stored within the register.
     *
     * @param simulator
     *            the program simulator.
     *
     * @return the value stored within the register.
     */
    @Override
    public byte[] get(Simulator simulator) {
        byte[] val = simulator.getRegister(register).getBytes();
        return Arrays.copyOf(val, val.length);
    }

    /**
     * Mutates the register according to the mutator function given.
     *
     * @param simulator
     *            the program simulator.
     * @param mutator
     *            the mutator function.
     */
    public void mutate(Simulator simulator, Function<byte[], byte[]> mutator) {
        byte[] val = simulator.getRegister(register).getBytes();
        byte[] mutate = mutator.apply(val);
        simulator.getRegister(register).setBytes(mutate);
    }

}
