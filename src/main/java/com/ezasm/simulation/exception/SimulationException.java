package com.ezasm.simulation.exception;

/**
 * Represents an exception during the runtime of the simulation of the language.
 */
public class SimulationException extends Exception {

    /**
     * Basic constructor of the exception with the given message.
     *
     * @param message the message to send.
     */
    public SimulationException(String message) {
        super(message);
    }

}
