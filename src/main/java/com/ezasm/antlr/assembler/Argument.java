package com.ezasm.antlr.assembler;

import java.util.ArrayList;

/**
 * An algebraic datatype used to store instruction dispatch arguments.
 * These are used by {@link Assembler} and are used as the arguments to {@link InstructionDispatcher#execute(ArrayList)}.
 */
public sealed interface Argument {
    /**
     * Register argument representation.
     * @param id the MIPS register id.
     */
    record Register(int id) implements Argument {}

    /**
     * Register dereference argument representation.
     * @param reg the target MIPS register id.
     * @param offset the optional offset of the dereference.
     */
    record Dereference(int reg, int offset) implements Argument {}

    /**
     * Label argument representation.
     * This can also be used to represent custom text arguments, such as section names.
     * @param id the identifier String.
     */
    record Label(String id) implements Argument {}

    /**
     * Immediate argument representation
     */
    sealed interface Immediate extends Argument {
        /**
         * Integer literal representation.
         * @param value the value of the integer.
         */
        record Integer(int value) implements Immediate {}

        /**
         * Character literal representation.
         * @param value the character value.
         */
        record Character(char value) implements Immediate {}

        /**
         * String literal representation.
         * @param value the String value.
         */
        record String(java.lang.String value) implements Immediate {}
    }
}
