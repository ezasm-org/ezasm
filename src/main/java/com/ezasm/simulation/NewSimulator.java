package com.ezasm.simulation;

import com.ezasm.instructions.InstructionDispatcher;
import com.ezasm.parsing.Line;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class NewSimulator {

    private final Memory memory;
    private final Registers registers;
    private final InstructionDispatcher instructionDispatcher;
    private final Register pc;

    private final List<Line> lines;
    private final Map<String, Integer> labels;

    // The delay in ms before the next instruction is read
    private long delayMS = 50L;

    /**
     * Constructs a Simulator with the default specifications.
     */
    public NewSimulator() {
        this.memory = new Memory();
        this.registers = new Registers(this.memory.WORD_SIZE);
        this.lines = new ArrayList<>();
        this.labels = new HashMap<>();
        pc = registers.getRegister(Registers.PC);
        instructionDispatcher = null; // new InstructionDispatcher(this);
    }

}
