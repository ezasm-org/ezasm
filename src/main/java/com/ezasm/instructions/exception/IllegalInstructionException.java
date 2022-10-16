package com.ezasm.instructions.exception;

/**
 * Represents the usage of an instruction with an improper number of parameters or a nonexistent instruction.
 */
public class IllegalInstructionException extends InstructionDispatchException {
    private final String message;

    /**
     * Basic constructor of the exception with the given message.
     * @param message the message to send.
     */
    public IllegalInstructionException(String message) {
        this.message = message;
    }

    /**
     * The message to return when the requested.
     * @return the corresponding message with an identifying prefix.
     */
    @Override
    public String getMessage() {
        return "Unknown instruction: " + this.message;
    }
}