package com.ezasm.simulation.transform;

import com.ezasm.simulation.exception.SimulationException;
import com.ezasm.simulation.transform.transformable.AbstractTransformable;
import com.ezasm.util.RawData;

import java.util.Objects;

/**
 * Represents a transformation on a simulation. Implemented so that transforms can be reversible. The contents of
 * <code>from</code> and <code>to</code> are never to be modified. They are read-only.
 */
public record Transformation(AbstractTransformable output, RawData from, RawData to) {

    /**
     * Inverts the transformation to get the opposite operation.
     *
     * @return the new inverse transformation.
     */
    public Transformation invert() {
        return new Transformation(output, to, from);
    }

    /**
     * Applies this transformation to the given simulator.
     *
     * @throws SimulationException if there is an exception in applying the transformation.
     */
    public void apply() throws SimulationException {
        output.set(to);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o)
            return true;
        if (o == null || getClass() != o.getClass())
            return false;
        Transformation that = (Transformation) o;
        return output.equals(that.output) && from.equals(that.from) && to.equals(that.to);
    }

    @Override
    public int hashCode() {
        int result = Objects.hash(output);
        result = 31 * result + from.hashCode();
        result = 31 * result + to.hashCode();
        return result;
    }
}
