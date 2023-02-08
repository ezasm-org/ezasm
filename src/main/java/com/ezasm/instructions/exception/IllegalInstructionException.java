package com.ezasm.instructions.exception;

/**
 * Represents the usage of an instruction with an improper number of parameters or a nonexistent instruction.
 */
public class IllegalInstructionException extends InstructionDispatchException {

    /**
     * Basic constructor of the exception with the in.
     *
     * @param instruction the message to send.
     */
    public IllegalInstructionException(String instruction) {
        super("Unknown instruction: " + instruction);
    }
}
