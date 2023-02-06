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

    void addLines(Collection<Line> lines, Map<String, Integer> labels);

    void runLine(Line line) throws InstructionDispatchException;

    void executeProgramFromPC() throws InstructionDispatchException;

    void executeLineFromPC() throws InstructionDispatchException;

    Registers getRegisters();

    Memory getMemory();

    void pause();

    void resume();

}
