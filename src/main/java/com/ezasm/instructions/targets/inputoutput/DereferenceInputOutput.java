package com.ezasm.instructions.targets.inputoutput;

import com.ezasm.Conversion;
import com.ezasm.instructions.targets.output.IAbstractOutput;
import com.ezasm.parsing.Lexer;
import com.ezasm.parsing.ParseException;
import com.ezasm.simulation.ISimulator;
import com.ezasm.simulation.Registers;

import java.util.Arrays;
import java.util.Objects;

/**
 * The implementation of a register to be used as either an input or an output.
 */
public class DereferenceInputOutput implements IAbstractInputOutput {

    private final RegisterInputOutput register;
    private final int offset;

    /**
     * Construct the output based on the register reference number.
     *
     * @param register the register reference number.
     */
    public DereferenceInputOutput(int register, int offset) {
        this.register = new RegisterInputOutput(register);
        this.offset = offset;
    }

    /**
     * Construct the output based on the register reference string.
     *
     * @param register the register reference string.
     */
    public DereferenceInputOutput(String text) throws ParseException{
        String offset = text.substring(0, text.indexOf('('));
        String register = text.substring(text.indexOf('('), text.indexOf(')'));
        this.register = new RegisterInputOutput(register);
        this.offset = Integer.parseInt(offset);
    }

    /**
     * Gets the value stored within the register.
     *
     * @param simulator the program simulator.
     * @return the value stored within the register.
     */
    @Override
    public byte[] get(ISimulator simulator) {
        int address = (int) Conversion.bytesToLong(register.get(simulator));
        byte[] val = simulator.getMemory().read(address);
        return Arrays.copyOf(val, val.length);
    }

    /**
     * Sets the value at the address contained within the specified register.
     *
     * @param simulator the program simulator.
     * @param value     the value to set.
     */
    @Override
    public void set(ISimulator simulator, byte[] value) {
        int address = (int) Conversion.bytesToLong(register.get(simulator));
        simulator.getMemory().write(address, value);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o)
            return true;
        if (o == null || getClass() != o.getClass())
            return false;
        RegisterInputOutput that = (RegisterInputOutput) o;
        return register.equals(that);
    }

    @Override
    public int hashCode() {
        return Objects.hash(register);
    }
}
