package com.ezasm.simulation.transform;

import com.ezasm.simulation.exception.SimulationException;
import com.ezasm.simulation.transform.transformable.OutputTransformable;
import com.ezasm.util.RawData;

/**
 * Represents the series of transformations on a simulation for an instruction to complete its action.
 */
public final class TransformationSequence {

    private final Transformation[] transformations;

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

    public TransformationSequence(OutputTransformable output, RawData from, RawData to) {
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
        System.arraycopy(transformations, 0, newTransformations, 0, transformations.length);
        System.arraycopy(other.transformations, 0, newTransformations, transformations.length,
                other.transformations.length);
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
     * @throws SimulationException if an error occurs applying the transformations.
     */
    public void apply() throws SimulationException {
        for (int i = 0; i < transformations.length; ++i) {
            transformations[i].apply();
        }
    }

}
