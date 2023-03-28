package com.ezasm.instructions.targets.input;

import com.ezasm.simulation.Simulator;
import com.ezasm.simulation.exception.SimulationException;
import com.ezasm.util.RawData;

/**
 * The implementation of a string immediate input to be used inline instead of a register or other input. Upon being
 * parsed, the string will be written into the simulator memory
 */
public class StringInput implements IAbstractInput {

    private final String string;

    /**
     * Constructs the string immediate with the given string.
     *
     * @param string the string.
     */
    public StringInput(String string) {
        this.string = string;
    }

    /**
     * Gets the address of the string in simulated memory.
     *
     * @param simulator the program simulator.
     * @return the string's address.
     */
    @Override
    public RawData get(Simulator simulator) throws SimulationException {
        return simulator.getMemory().getStringImmediateAddress(string);
    }

    /**
     * Gets the string referred to by the string input.
     *
     * @return the string referred to by the string input.
     */
    public String getString() {
        return string;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o)
            return true;
        if (o == null || getClass() != o.getClass())
            return false;
        StringInput that = (StringInput) o;
        return string.equals(that.string);
    }

    @Override
    public int hashCode() {
        return string.hashCode();
    }
}
