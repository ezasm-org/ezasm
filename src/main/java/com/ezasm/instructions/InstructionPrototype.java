package com.ezasm.instructions;

import com.ezasm.instructions.targets.IAbstractTarget;

import java.util.Arrays;

public record InstructionPrototype(String name, Class<?>... argTypes) {

    private boolean argTypesMatch(InstructionPrototype other) {
        if (argTypes.length != other.argTypes.length) return false;

        for (int i = 0; i < argTypes.length; i++) {
            System.out.println(argTypes[i] + ", " + other.argTypes[i] + " : " + argTypes[i].isAssignableFrom(other.argTypes[i]));
            if (!argTypes[i].isAssignableFrom(other.argTypes[i])) return false;
        }

        return true;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        InstructionPrototype that = (InstructionPrototype) o;

        if (!name.equals(that.name)) return false;
        // Probably incorrect - comparing Object[] arrays with Arrays.equals
        return argTypesMatch(that);
    }

    @Override
    public int hashCode() {
        return name.hashCode();
    }

    @Override
    public String toString() {
        return "InstructionPrototype{" +
                "name='" + name + '\'' +
                ", argTypes=" + Arrays.toString(argTypes) +
                '}';
    }
}
