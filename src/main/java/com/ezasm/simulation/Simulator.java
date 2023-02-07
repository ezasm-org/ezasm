package com.ezasm.simulation;

import com.ezasm.gui.Window;
import com.ezasm.instructions.InstructionDispatcher;
import com.ezasm.instructions.exception.InstructionDispatchException;
import com.ezasm.parsing.Line;

import java.util.*;

/**
 * The main controller class. Manages the memory, registers, and lines.
 */
public class Simulator implements ISimulator {

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
    public Simulator() {
        this.memory = new Memory();
        this.registers = new Registers(this.memory.WORD_SIZE);
        this.lines = new ArrayList<>();
        this.labels = new HashMap<>();
        pc = registers.getRegister(Registers.PC);
        instructionDispatcher = new InstructionDispatcher(this);
        init();
    }

    /**
     * Constructs a Simulator with the given word size and memory size specifications.
     *
     * @param wordSize   the size of words in bytes for the program.
     * @param memorySize the size of the memory in words for the program.
     */
    public Simulator(int wordSize, int memorySize, long delay) {
        this.memory = new Memory(wordSize, memorySize);
        this.registers = new Registers(wordSize);
        this.lines = new ArrayList<>();
        this.labels = new HashMap<>();
        this.delayMS = delay;
        pc = registers.getRegister(Registers.PC);
        instructionDispatcher = new InstructionDispatcher(this);
        init();
    }

    private void init() {
        registers.getRegister(Registers.SP).setLong(memory.initialStackPointer());
    }

    /**
     * Changes the simulation speed.
     *
     * @param delay amount of time in milliseconds to wait between instructions.
     */
    public void setSimulationSpeed(long delay) {
        delayMS = delay;
    }

    /**
     * Resets the contents of memory and registers.
     */
    public void resetData() {
        memory.reset();
        registers.reset();
    }

    /**
     * Resets the contents of memory and registers as well as stored lines and labels.
     */
    public void resetAll() {
        resetData();
        lines.clear();
        labels.clear();
        init();
    }

    /**
     * Return true if the program has run off of the end of the code as in program completion, false
     * otherwise.
     *
     * @return true if the program has run off of the end of the code as in program completion, false
     *         otherwise.
     */
    public boolean isDone() {
        return pc.getLong() == lines.size();
    }

    /**
     * Return true if the program counter is in an error state, false otherwise.
     *
     * @return true if the program counter is in an error state, false otherwise.
     */
    public boolean isErrorPC() {
        long line = pc.getLong();
        return line > lines.size() || line < 0;
    }

    /**
     * Adds the given line to the program.
     *
     * @param line the line to add to the program.
     */
    public void addLine(Line line) {
        if (line.isLabel()) {
            labels.put(line.getLabel(), lines.size());
        }
        lines.add(line);
    }

    /**
     * Parses the given text as a multi-line String. Then adds those lines to the program.
     *
     * @param content the collection of Lines to add to the program.
     */
    public void addLines(Collection<Line> content) {
        content.forEach(this::addLine);
    }

    /**
     * Executes the given line on the simulator.
     *
     * @param line the line to execute.
     * @throws InstructionDispatchException if there is an error executing the line.
     */
    public void runLine(Line line) throws SimulationException {
        if (line == null) {
            return;
        }
        if (line.isLabel()) {
            if(labels.containsKey(line.getLabel())) {
                // What to do in case of duplicate labels... currently nothing
            }
            labels.put(line.getLabel(), (int) pc.getLong());
        } else {
            instructionDispatcher.execute(line);
        }
        Window.updateAll();
    }

    /**
     * Runs the program to completion or error state from the current state of the PC.
     *
     */
    public void executeProgramFromPC() throws SimulationException {
        while (!isDone() && !Thread.interrupted()) {
            executeLineFromPC();
            Window.updateAll();
            try {
                Thread.sleep(delayMS);
            } catch (InterruptedException e) {
                break;
            }
        }
    }

    /**
     * Runs a single line of code from the current PC.
     *
     * @throws InstructionDispatchException if there is an error executing the line.
     */
    public void executeLineFromPC() throws SimulationException {
        int lineNumber = validatePC();
        runLine(lines.get(lineNumber));
        int currentPC = validatePC();
        pc.setLong(currentPC + 1);
    }

    /**
     * A helper function to validate the state of the PC register.
     *
     * @return the validated PC.
     */
    private int validatePC() {
        if (isErrorPC()) {
            // Guaranteed invalid PC
            // TODO handle better
            throw new RuntimeException();
        }
        return (int) pc.getLong();
    }

    /**
     * Get the labels mapping for the simulator
     *
     * @return the label mapping.
     */
    public Map<String, Integer> getLabels() {
        return labels;
    }

    /**
     * Gets the registers representation of the program.
     *
     * @return the registers representation of the program.
     */
    public Registers getRegisters() {
        return registers;
    }

    /**
     * Gets the memory representation of the program.
     *
     * @return the memory representation of the program.
     */
    public Memory getMemory() {
        return memory;
    }

    /**
     * Pauses ongoing program execution loop if applicable.
     */
    public void pause() {
    }

    /**
     * Resumes ongoing program execution loop if applicable.
     */
    public void resume() {
    }

}
