package com.ezasm.instructions;

import com.ezasm.parsing.Line;
import com.ezasm.simulation.exception.SimulationException;
import com.ezasm.simulation.transform.TransformationSequence;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

/**
 * An instruction which can be dispatched. This has all the information necessary to interpret a parsed {@link Line} and
 * caches it for quick interpretation.
 */
public class DispatchInstruction {

    /**
     * The method that corresponds to the instruction.
     */
    private final Method invocationTarget;

    /**
     * The parent class of the method that corresponds to the instruction.
     */
    private final Class<?> parent;

    /**
     * Create a new dispatchable instruction based on a method with specific parameters and its parent class.
     *
     * @param parent           the parent class.
     * @param invocationTarget the method for which to deduce operands for instructions and compile into a dispatchable
     *                         instruction.
     */
    public DispatchInstruction(Class<?> parent, Method invocationTarget) {
        this.parent = parent;
        this.invocationTarget = invocationTarget;
    }

    /**
     * Gets the parent class of the instruction (the instruction handler).
     *
     * @return a Class object corresponding to the parent class.
     */
    public Class<?> getParent() {
        return parent;
    }

    public Method getInvocationTarget() {
        return invocationTarget;
    }

    /**
     * Invoke an instruction based on the parsed line (interpret the arguments and invoke the bound method).
     *
     * @param parent the parent instruction handler. An instance of {@link DispatchInstruction#getParent()}.
     * @param line   the parsed line to interpret.
     */
    public TransformationSequence invoke(Object parent, Line line) throws SimulationException {
        try {
            return (TransformationSequence) this.invocationTarget.invoke(parent, line.getArguments());
        } catch (InvocationTargetException e) {
            throw new SimulationException(e.getTargetException().getMessage());
        } catch (IllegalAccessException | IllegalArgumentException e) {
            // TODO handle
            throw new RuntimeException();
        }
    }

}
