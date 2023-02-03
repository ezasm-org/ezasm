package com.ezasm.simulation;

import com.ezasm.parsing.Line;

import java.util.Collection;

public interface ISimulator {

    void resetData();

    void resetAll();

    boolean isDone();

    boolean isErrorPC();

    void addLine(Line line);

    void addLines(Collection<Line> lines);

    void runLine(Line line);

    void executeProgramFromPC();

    void executeLineFromPC();

    Registers getRegisters();

    Memory getMemory();

}
