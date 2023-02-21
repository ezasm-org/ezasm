package com.ezasm.instructions.targets.inputoutput;

import com.ezasm.instructions.targets.input.IAbstractInput;
import com.ezasm.instructions.targets.output.IAbstractOutput;
import com.ezasm.simulation.ISimulator;
import com.ezasm.simulation.Transformation;
import com.ezasm.simulation.exception.SimulationException;

/**
 * Represents something that can be either an output or an output to an operation. Requires implementing a "set"
 * operation to serve as the output and a "get" operation to serve as the input.
 */
public interface IAbstractInputOutput extends IAbstractInput, IAbstractOutput {

    public Transformation transformation(ISimulator simulator, byte[] value) throws SimulationException;

}
