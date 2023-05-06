package com.ezasm.instructions;

import com.ezasm.parsing.Line;
import com.ezasm.simulation.exception.SimulationException;
import com.ezasm.simulation.exception.SimulationInterruptedException;
import com.ezasm.simulation.transform.TransformationSequence;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

/**
 * An instruction which can be dispatched. This has all the information necessary to interpret a parsed {@link Line} and
 * caches it for quick interpretation.
 *
 * @param invocationTarget The method that corresponds to the instruction.
 * @param parent           The parent class of the method that corresponds to the instruction.
 */
public record DispatchInstruction(Class<?> parent, Method invocationTarget) {

    /**
     * Checks if this instruction is callable with the given argument types.
     *
     * @param givenArguments the arguments the caller gave.
     * @return true if this instruction is callable with the given argument types, false otherwise.
     */
    public boolean isCallableWith(Class<?>[] givenArguments) {
        Class<?>[] arguments = invocationTarget.getParameterTypes();
        if (arguments.length != givenArguments.length) {
            return false;
        }

        for (int i = 0; i < arguments.length; i++) {
            if (!arguments[i].isAssignableFrom(givenArguments[i]))
                return false;
        }
        return true;
    }

    /**
     * Invoke an instruction based on the parsed line (interpret the arguments and invoke the bound method).
     *
     * @param parent the parent instruction handler. An instance of {@link DispatchInstruction#parent ()}.
     * @param line   the parsed line to interpret.
     * @throws SimulationException            if an error occurs executing the target instruction.
     * @throws SimulationInterruptedException if an interrupt occurs while executing.
     */
    public TransformationSequence invoke(Object parent, Line line)
            throws SimulationException, SimulationInterruptedException {
        try {
            return (TransformationSequence) this.invocationTarget.invoke(parent, line.getArguments());
        } catch (InvocationTargetException e) {
            if (e.getTargetException() instanceof SimulationInterruptedException interrupt) {
                throw interrupt;
            }
            if (e.getTargetException() == null || e.getTargetException().getMessage() == null) {
                e.printStackTrace();
                throw new SimulationException("An unknown error occurred");
            }
            throw new SimulationException(e.getTargetException().getMessage());
        } catch (IllegalAccessException | IllegalArgumentException e) {
            // TODO handle
            throw new RuntimeException();
        }
    }

}
