package com.ezasm.instructions;

public record InstructionOverload(Class<?>[] args, DispatchInstruction dispatch) {
    boolean isCallableWith(Class<?>[] args) {
        if (this.args.length != args.length)
            return false;

        for (int i = 0; i < args.length; i++) {
            if (!this.args[i].isAssignableFrom(args[i]))
                return false;
        }

        return true;
    }
}
