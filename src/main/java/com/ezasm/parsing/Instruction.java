package com.ezasm.parsing;

import java.util.Objects;

/**
 * Represents an instruction in the assembly language: a name and a function to invoke.
 */
public record Instruction(String text) {

    /**
     * Gets the instruction name String.
     *
     * @return the instruction name String.
     */
    @Override
    public String text() {
        return text;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o)
            return true;
        if (o == null || getClass() != o.getClass())
            return false;
        Instruction that = (Instruction) o;
        return Objects.equals(text, that.text);
    }

    @Override
    public int hashCode() {
        return Objects.hash(text);
    }
}
