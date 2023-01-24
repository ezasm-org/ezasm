package com.ezasm.antlr.assembler;

import com.ezasm.instructions.exception.IllegalArgumentException;

import java.util.ArrayList;
import java.util.LinkedList;

public record InstructionDispatcher(Instruction instruction, Class<? extends Argument>... argTypes) {
    @SafeVarargs
    public InstructionDispatcher {}

    /**
     * Type-checks the {@link Argument Arguments} for an instruction dispatch and calls {@link Instruction#resolve(ArrayList)} if the {@link Argument Argument} types match.
     * @param args The {@link Argument Arguments} to type-check and call {@link Instruction#resolve(ArrayList)} with.
     * @return The generated list of assembler {@link Directive Directives} that are produced by @link Instruction#resolve(ArrayList)}.
     * @throws IllegalArgumentException if an incorrect number of {@link Argument Arguments} are specified or if the {@link Argument Arguments} are of the wrong type.
     */
    public LinkedList<Directive> execute(ArrayList<Argument> args) {
        if (args.size() != argTypes.length) {
            throw new IllegalArgumentException(-1);
        }

        for (int i = 0; i < argTypes.length; i++) {
            if (argTypes[i] != args.get(i).getClass()) {
                throw new IllegalArgumentException(i);
            }
        }

        return instruction.resolve(args);
    }
}
