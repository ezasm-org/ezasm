package com.ezasm.simulation;

import com.ezasm.parsing.Line;
import com.ezasm.simulation.exception.SimulationException;

import java.util.Collection;
import java.util.Map;

/**
 * An implementation of a simulator which manages the memory, registers, and lines of code in an instance of the EzASM
 * runtime. Provides capabilities to run lines of code and access to the internal representation.
 */
public interface ISimulator {

    void resetData();

    void resetAll();

    boolean isDone();

    boolean isError();

    void addLine(Line line);

    void addLines(Collection<Line> lines);

    void runLine(Line line) throws SimulationException;

    void executeProgramFromPC() throws SimulationException;

    void executeLineFromPC() throws SimulationException;

    Map<String, Integer> getLabels();

    Registers getRegisters();

    Memory getMemory();

}
