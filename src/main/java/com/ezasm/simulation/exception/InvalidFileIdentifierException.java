package com.ezasm.simulation.exception;

/**
 * Represents an exception due to attempting to run at an unknown file identifier.
 */
public class InvalidFileIdentifierException extends SimulationException {

    /**
     * Constructs the exception with the given file identifier.
     *
     * @param fid the file identifier.
     */
    public InvalidFileIdentifierException(long fid) {
        super(String.format("Invalid file identifier (fid) value: %d", fid));
    }
}
