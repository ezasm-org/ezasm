package com.ezasm.simulation;

import com.ezasm.instructions.targets.inputoutput.IAbstractInputOutput;
import com.ezasm.instructions.targets.output.IAbstractOutput;
import com.ezasm.simulation.exception.SimulationException;

import java.util.ArrayDeque;
import java.util.Deque;

/**
 * Represents the series of transformations on a simulation for an instruction to complete its action.
 */
public final class TransformationSequence {

    private final Transformation[] transformations;
    private final static Deque<TransformationSequence> transformationSequences = new ArrayDeque<>();

    /**
     * Requires at least one transformation. Represents the transformations done to a simulator by an instruction.
     *
     * @param transformations the transformations done to a simulator by an instruction.
     * @throws RuntimeException if no transformations are given.
     */
    public TransformationSequence(Transformation... transformations) {
        if (transformations == null) {
            throw new RuntimeException("Bad transformations list");
        }
        this.transformations = transformations;
    }

    public TransformationSequence(IAbstractOutput output, byte[] from, byte[] to) {
        this.transformations = new Transformation[] { new Transformation(output, from, to) };
    }

    /**
     * Concatenate another transformation sequence onto the end of this transformation sequence.
     *
     * @param other the transformation sequence to be appended.
     * @return the resultant transformation sequence.
     */
    public TransformationSequence concatenate(TransformationSequence other) {
        Transformation[] newTransformations = new Transformation[transformations.length + other.transformations.length];
        for (int i = 0; i < transformations.length; ++i) {
            newTransformations[i] = transformations[i];
        }
        for (int i = 0; i < other.transformations.length; ++i) {
            newTransformations[i + transformations.length] = other.transformations[i];
        }
        return new TransformationSequence(newTransformations);
    }

    /**
     * Inverts the sequence of transformations by inverting all elements and reversing the order.
     *
     * @return an inverted sequence of transformations which can undo the effects of the first.
     */
    public TransformationSequence invert() {
        Transformation[] newTransformations = new Transformation[transformations.length];
        for (int i = 0; i < transformations.length; ++i) {
            newTransformations[transformations.length - i - 1] = transformations[i].invert();
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
