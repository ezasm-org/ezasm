package EzASM.instructions;

import EzASM.parsing.Line;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

public class DispatchInstruction {

    private final Method invocationTarget;
    private final Class<?> parent;

    public DispatchInstruction(Class<?> parent, Method invocationTarget) {
        this.parent = parent;
        this.invocationTarget = invocationTarget;
    }

    public Class<?> getParent() {
        return parent;
    }

    public void invoke(Object parent, Line line) {
        // TODO convert tokens to instruction targets
        Object[] args = null;
        try {
            this.invocationTarget.invoke(parent, args);
        } catch (IllegalAccessException | InvocationTargetException e) {
            // TODO handle
            throw new RuntimeException(e);
        }

    }

}
