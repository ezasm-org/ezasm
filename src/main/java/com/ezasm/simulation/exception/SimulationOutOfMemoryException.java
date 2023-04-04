package com.ezasm.simulation.exception;

public class SimulationOutOfMemoryException extends SimulationException {

    /**
     * Constructs the exception with the given stack pointer.
     *
     * @param memSize the size of the memory block that failed to be allocated.
     */
    public SimulationOutOfMemoryException(long memSize) {
        super(String.format("Simulation ran out of memory while attempting to allocate block of size: %d", memSize));
    }
}
