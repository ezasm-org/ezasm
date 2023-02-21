package com.ezasm.simulation;

import com.ezasm.simulation.exception.SimulationException;

import java.util.ArrayDeque;
import java.util.Deque;

/**
 * Represents the series of transformations on a simulation for an instruction to complete its action.
 */
public class TransformationSequence {

    private final Transformation[] transformations;
    private final static Deque<TransformationSequence> transformationSequences = new ArrayDeque<>();

    /**
     * Requires at least one transformation. Represents the transformations done to a simulator by an instruction.
     *
     * @param transformations the transformations done to a simulator by an instruction.
     * @throws RuntimeException if no transformations are given.
     */
    public TransformationSequence(Transformation... transformations) {
        if (transformations == null || transformations.length == 0) {
            throw new RuntimeException("No transformations given");
        }
        this.transformations = transformations;
    }

    /**
     * Inverts the sequence of transformations by inverting all elements and reversing the order.
     *
     * @return an inverted sequence of transformations which can undo the effects of the first.
     */
    public TransformationSequence invert() {
        Transformation[] newTransformations = new Transformation[transformations.length];
        for (int i = 0; i < transformations.length; ++i) {
            newTransformations[transformations.length - i - 1]  = transformations[i].invert();
        }
        return new TransformationSequence(newTransformations);
    }

    /**
     * Applies the transformation sequence to the simulator.
     *
     * @param simulator the simulator on which the operations are to be applied.
     * @throws SimulationException if an error occurs applying the transformations.
     */
    public void apply(ISimulator simulator) throws SimulationException {
        for (int i = 0; i < transformations.length; ++i) {
            transformations[i].apply(simulator);
        }
    }

    /**
     * Applies the transformation sequence to the simulator, then adds the transformation to the stack.
     *
     * @param simulator the simulator on which the operations are to be applied.
     * @throws SimulationException if an error occurs applying the transformations.
     */
    public void applyToStack(ISimulator simulator) throws SimulationException {
        transformationSequences.push(this);
        apply(simulator);
    }

    public static void resetStack() {
        transformationSequences.clear();
    }

    public static TransformationSequence popStack() {
        return transformationSequences.pop();
    }

    public static boolean isEmpty() {
        return transformationSequences.isEmpty();
    }

}
