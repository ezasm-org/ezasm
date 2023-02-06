package com.ezasm.parsing;

import java.lang.reflect.Method;

/**
 * Represents an instruction in the assembly language: a name and a function to invoke.
 */
public record Instruction(String text, Method target) {

    /**
     * Gets the instruction name String.
     *
     * @return the instruction name String.
     */
    @Override
    public String text() {
        return text;
    }

    /**
     * Gets the target function to invoke.
     *
     * @return the target function to invoke.
     */
    @Override
    public Method target() {
        return target;
    }
}
