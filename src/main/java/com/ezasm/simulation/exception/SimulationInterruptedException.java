package com.ezasm.simulation.exception;

/**
 * Represents an exception where the simulation is interrupted during execution. This exception should not be caught and
 * then rethrown as a different exception.
 */
public class SimulationInterruptedException extends Exception {

    /**
     * Constructs a basic interruption exception.
     */
    public SimulationInterruptedException() {
        super("Simulation execution interrupted");
    }

    /**
     * Constructs an interruption exception with the given message.
     *
     * @param message the message to send.
     */
    public SimulationInterruptedException(String message) {
        super(message);
    }

    /**
     * Handles the case of interrupt signal. Use when waiting user input or completing some otherwise long instruction.
     *
     * @throws SimulationInterruptedException if the thread has been interrupted.
     */
    public static void handleInterrupts() throws SimulationInterruptedException {
        if (Thread.currentThread().isInterrupted()) {
            throw new SimulationInterruptedException();
        }
    }
}
