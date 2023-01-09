package com.ezasm.assembler;

import org.antlr.v4.runtime.Token;

/**
 * Runtime exception thrown by {@link Assembler} in the event of an error during assembly.
 */
public class AssemblerException extends RuntimeException {
    /**
     * Creates an exception that displays the line location of an error and a message.
     * @param line the line number that the error occurred on.
     * @param charInLine the character within the line that the error occurred at.
     * @param msg the message to display.
     */
    public AssemblerException(int line, int charInLine, String msg) {
        super("line " + line + ":" + charInLine + " " + msg);
    }

    /**
     * Creates an exception that displays the line location of an error and a message.
     * @param token the token that caused the error
     * @param msg the message to display
     */
    public AssemblerException(Token token, String msg) {
        super("line " + token.getLine() + ":" + token.getCharPositionInLine() + " " + msg);
    }

    /**
     * Creates an exception with a given cause.
     * @param cause the original cause of the error.
     */
    public AssemblerException(Throwable cause) {
        this.initCause(cause);
    }

    /**
     * Creates an exception that displays the line location of an error, a message, and a cause.
     * @param line the line number that the error occurred on.
     * @param charInLine the character within the line that the error occurred at.
     * @param msg the message to display.
     * @param cause the original cause of the error.
     */
    public AssemblerException(int line, int charInLine, String msg, Throwable cause) {
        this(line, charInLine, msg);
        this.initCause(cause);
    }
}
