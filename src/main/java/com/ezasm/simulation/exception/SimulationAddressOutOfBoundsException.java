package com.ezasm.simulation.exception;

public class SimulationAddressOutOfBoundsException extends SimulationException {

    /**
     * Basic constructor of the exception with the given address of failure.
     *
     * @param address the message to send.
     */
    public SimulationAddressOutOfBoundsException(int address) {
        super(String.format("Address %d could not be read or written to", address));
    }
}
