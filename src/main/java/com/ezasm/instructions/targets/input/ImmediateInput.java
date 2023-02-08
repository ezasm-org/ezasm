package com.ezasm.instructions.targets.input;

import com.ezasm.simulation.ISimulator;

import java.util.Arrays;

/**
 * The implementation of an "immediate" input to be used inline instead of a register or other input. Is used as a fixed
 * value or constant.
 */
public class ImmediateInput implements IAbstractInput {

    private final byte[] value;

    /**
     * Constructs the input with the given constant value.
     *
     * @param value the constant value.
     */
    public ImmediateInput(byte[] value) {
        this.value = value;
    }

    /**
     * Gets the constant value of the immediate.
     *
     * @param simulator the program simulator.
     * @return the constant value.
     */
    @Override
    public byte[] get(ISimulator simulator) {
        return value;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o)
            return true;
        if (o == null || getClass() != o.getClass())
            return false;
        ImmediateInput that = (ImmediateInput) o;
        return Arrays.equals(value, that.value);
    }

    @Override
    public int hashCode() {
        return Arrays.hashCode(value);
    }
}
