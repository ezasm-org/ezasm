package com.ezasm.simulation;

import com.ezasm.instructions.exception.InstructionDispatchException;
import com.ezasm.parsing.Line;
import com.ezasm.parsing.ParseException;

import java.util.Collection;
import java.util.Map;

public interface ISimulator {

    void resetData();

    void resetAll();

    boolean isDone();

    boolean isErrorPC();

    void addLine(Line line);

    void addLines(Collection<Line> lines);

    void runLine(Line line) throws SimulationException;

    void executeProgramFromPC() throws SimulationException;

    void executeLineFromPC() throws SimulationException;

    Map<String, Integer> getLabels();

    Registers getRegisters();

    Memory getMemory();

    void pause();

    void resume();

}
