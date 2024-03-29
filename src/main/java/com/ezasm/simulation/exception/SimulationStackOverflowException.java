package com.ezasm.simulation.exception;

/**
 * Represents a situation where the stack began to overwrite the heap.
 */
public class SimulationStackOverflowException extends SimulationException {

    /**
     * Constructs the exception with the given stack pointer.
     *
     * @param sp the stack pointer.
     */
    public SimulationStackOverflowException(long sp) {
        super(String.format("Stack overflow detected. Stack pointer (sp) was at: %d", sp));
    }
}
