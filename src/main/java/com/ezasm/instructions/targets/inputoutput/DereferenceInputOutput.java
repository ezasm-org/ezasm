package com.ezasm.instructions.targets.inputoutput;

import com.ezasm.simulation.Transformation;
import com.ezasm.simulation.TransformationSequence;
import com.ezasm.util.Conversion;
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
    public DereferenceInputOutput(String register, int offset) {
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
        this.register = new RegisterInputOutput(register);
        if (offset.length() == 0) {
            this.offset = 0;
            return;
        }
        try {
            this.offset = Integer.parseInt(offset);
        } catch (NumberFormatException e) {
            throw new ParseException(String.format("Offset %s is outside the range of valid offset numbers", offset));
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

    /**
     * Gets the transformation corresponding to calling set on this object.
     *
     * @param simulator the program simulator.
     * @param value     the value to set.
     * @return the transformation corresponding to this action.
     */
    @Override
    public Transformation transformation(ISimulator simulator, byte[] value) throws SimulationException {
        return new Transformation(this, get(simulator), value);
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
