package com.ezasm.instructions.exception;

import com.ezasm.simulation.exception.SimulationException;

/**
 * An error in dispatching an instruction's corresponding method.
 */
public class InstructionDispatchException extends SimulationException {

    /**
     * Basic constructor of the exception with the given message.
     *
     * @param message the message to send.
     */
    public InstructionDispatchException(String message) {
        super(message);
    }
}
