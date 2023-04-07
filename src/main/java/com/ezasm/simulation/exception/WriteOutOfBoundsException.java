package com.ezasm.simulation.exception;

/**
 * Represents an attempt to write to an out address which is out of bounds.
 */
public class WriteOutOfBoundsException extends SimulationException {

    /**
     * Basic constructor of the exception with the given address of failure.
     *
     * @param address the address of the attempted write.
     */
    public WriteOutOfBoundsException(int address) {
        super(String.format("Address %d could not be written to", address));
    }
}
