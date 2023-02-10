package com.ezasm.instructions.targets.inputoutput;

import com.ezasm.Conversion;
import com.ezasm.parsing.ParseException;
import com.ezasm.simulation.ISimulator;
import com.ezasm.simulation.exception.SimulationException;

import java.util.Arrays;
import java.util.Objects;

/**
 * The implementation of a dereference to be used as either an input or an output.
 */
public class DereferenceInputOutput implements IAbstractInputOutput {

    private final RegisterInputOutput register;
    private final int offset;

    /**
     * Construct based on the register number and offset.
     *
     * @param register the register reference number.
     * @param offset   the index offset.
     */
    public DereferenceInputOutput(int register, int offset) {
        this.register = new RegisterInputOutput(register);
        this.offset = offset;
    }

    /**
     * Construct based on the dereference string.
     *
     * @param text the register reference string.
     */
    public DereferenceInputOutput(String text) throws ParseException {
        String offset = text.substring(0, text.indexOf('('));
        String register = text.substring(text.indexOf('(') + 1, text.indexOf(')'));
        System.out.println(offset + "\t" + register);
        this.register = new RegisterInputOutput(register);
        if (offset.length() > 0) {
            this.offset = Integer.parseInt(offset);
        } else {
            this.offset = 0;
        }
    }

    /**
     * Gets the value stored within the address.
     *
     * @param simulator the program simulator.
     * @return the value stored within the address.
     */
    @Override
    public byte[] get(ISimulator simulator) throws SimulationException {
        int address = (int) Conversion.bytesToLong(register.get(simulator));
        byte[] val = simulator.getMemory().read(address + offset);
        return Arrays.copyOf(val, val.length);
    }

    /**
     * Sets the value at the address contained within the specified register.
     *
     * @param simulator the program simulator.
     * @param value     the value to set.
     */
    @Override
    public void set(ISimulator simulator, byte[] value) throws SimulationException {
        int address = (int) Conversion.bytesToLong(register.get(simulator));
        simulator.getMemory().write(address + offset, value);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o)
            return true;
        if (o == null || getClass() != o.getClass())
            return false;
        DereferenceInputOutput that = (DereferenceInputOutput) o;
        return register.equals(that.register) && offset == that.offset;
    }

    @Override
    public int hashCode() {
        return Objects.hash(register);
    }
}
