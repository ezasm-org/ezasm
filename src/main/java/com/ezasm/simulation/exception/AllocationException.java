package com.ezasm.simulation.exception;

/**
 * Represents an attempted memory allocation to an out address which is out of bounds.
 */
public class AllocationException extends SimulationException {

    /**
     * Basic constructor of the exception with the given address of failure and attempted allocation size.
     *
     * @param addressFrom the address from which the memory was to grow.
     * @param size        the size which the memory was to grow.
     */
    public AllocationException(int addressFrom, int size) {
        super(String.format("Address %d could not grow by %d bytes", addressFrom, size));
    }
}
