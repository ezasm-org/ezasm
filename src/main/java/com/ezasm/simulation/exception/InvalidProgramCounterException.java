package com.ezasm.simulation.exception;

/**
 * Represents an exception due to attempting to run at an invalid program counter.
 */
public class InvalidProgramCounterException extends SimulationException {

    /**
     * Constructs the exception with the given program counter.
     *
     * @param pc the program counter.
     */
    public InvalidProgramCounterException(long pc) {
        super(String.format("Invalid program counter value: %d", pc));
    }
}
