package com.ezasm.instructions.exception;

/**
 * Not yet implemented. Represents the usage of an argument which cannot be accepted by the
 * function.
 */
public class IllegalArgumentException extends java.lang.IllegalArgumentException {

    private final int argIndex;

    public IllegalArgumentException(int argIndex) {
        this.argIndex = argIndex;
    }

    public int getArgIndex() {
        return argIndex;
    }
}
