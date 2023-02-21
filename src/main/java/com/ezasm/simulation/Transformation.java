package com.ezasm.simulation;

import com.ezasm.instructions.targets.output.IAbstractOutput;
import com.ezasm.simulation.exception.SimulationException;

import java.util.Arrays;
import java.util.Objects;

/**
 * Represents a transformation on a simulation. Implemented so that transforms can be reversible.
 * The contents of <code>from</code> and <code>to</code> are never to be modified. They are read-only.
 */
public record Transformation(IAbstractOutput output, byte[] from, byte[] to) {

    /**
     * Inverts the transformation to get the opposite operation.
     * @return the new inverse transformation.
     */
    public Transformation invert() {
        return new Transformation(output, to, from);
    }

    /**
     * Applies this transformation to the given simulator.
     *
     * @param simulator the simulator to apply the transformation to.
     * @throws SimulationException if there is an exception in applying the transformation.
     */
    public void apply(ISimulator simulator) throws SimulationException {
        output.set(simulator, to);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Transformation that = (Transformation) o;
        return output.equals(that.output) && Arrays.equals(from, that.from) && Arrays.equals(to, that.to);
    }

    @Override
    public int hashCode() {
        int result = Objects.hash(output);
        result = 31 * result + Arrays.hashCode(from);
        result = 31 * result + Arrays.hashCode(to);
        return result;
    }

    @Override
    public Transformation clone() {
        // A call to super.clone would be redundant and waste resources
        return new Transformation(output, from.clone(), to.clone());
    }
}
