package com.ezasm.simulation.exception;

/**
 * Represents an attempt to read to an out address which is out of bounds.
 */
public class ReadOutOfBoundsException extends SimulationException {

    /**
     * Basic constructor of the exception with the given address of failure.
     *
     * @param address the address of te attempted read.
     */
    public ReadOutOfBoundsException(int address) {
        super(String.format("Address %d could not be read", address));
    }
}
