package com.ezasm.simulation.exception;

/**
 * Represents an attempt to write to an out address which is read-only.
 */
public class WriteToReadOnlyException extends SimulationException {

    /**
     * Basic constructor of the exception with the given address of failure.
     *
     * @param address the address of the attempted write.
     */
    public WriteToReadOnlyException(int address) {
        super(String.format("Could not write to read-only address %d", address));
    }
}
