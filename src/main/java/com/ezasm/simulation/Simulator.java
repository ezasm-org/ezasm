package com.ezasm.simulation;

import com.ezasm.instructions.InstructionDispatcher;
import com.ezasm.instructions.exception.InstructionDispatchException;
import com.ezasm.instructions.targets.inputoutput.RegisterInputOutput;
import com.ezasm.parsing.Line;
import com.ezasm.simulation.exception.InvalidProgramCounterException;
import com.ezasm.simulation.exception.SimulationException;
import com.ezasm.simulation.transform.Transformation;
import com.ezasm.simulation.transform.TransformationSequence;
import com.ezasm.simulation.transform.transformable.InputOutputTransformable;
import com.ezasm.util.RawData;

import java.util.*;

/**
 * An implementation of a simulator which manages the memory, registers, and lines of code in an instance of the EzASM
 * runtime. Provides capabilities to run lines of code and access to the internal representation.
 */
public class Simulator implements ISimulator {

    private final Memory memory;
    private final Registers registers;
    private final InstructionDispatcher instructionDispatcher;
    private final Register pc;

    private final List<Line> lines;
    private final Map<String, Integer> labels;
    private final Deque<TransformationSequence> transforms;

    /**
     * Constructs a Simulator with the given word size and memory size specifications.
     *
     * @param wordSize   the size of words in bytes for the program.
     * @param memorySize the size of the memory in words for the program.
     */
    public Simulator(int wordSize, int memorySize) {
        this.memory = new Memory(wordSize, memorySize);
        this.registers = new Registers(wordSize);
        this.lines = new ArrayList<>();
        this.labels = new HashMap<>();
        this.transforms = new ArrayDeque<>();
        pc = registers.getRegister(Registers.PC);
        instructionDispatcher = new InstructionDispatcher(this);
        initialize();
    }

    /**
     * Initialization function which sets up any registers and memory.
     */
    private void initialize() {
        registers.getRegister(Registers.SP).setLong(memory.initialStackPointer());
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
        transforms.clear();
        initialize();
    }

    /**
     * Return true if the program has run off of the end of the code as in program completion, false otherwise.
     *
     * @return true if the program has run off of the end of the code as in program completion, false otherwise.
     */
    public boolean isDone() {
        return pc.getLong() == lines.size();
    }

    /**
     * Return true if the program counter is in an error state, false otherwise.
     *
     * @return true if the program counter is in an error state, false otherwise.
     */
    public boolean isError() {
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
        if (line != null) {
            if (line.isLabel()) {
                applyTransformations(new TransformationSequence());
            } else {
                instructionDispatcher.execute(line);
            }
        }
    }

    @Override
    public void executeProgramFromPC() throws SimulationException {
        while (!isDone() && !isError()) {
            executeLineFromPC();
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
        validatePC();
    }

    @Override
    public void applyTransformations(TransformationSequence t) throws SimulationException {
        t.apply();
        InputOutputTransformable io = new InputOutputTransformable(this, new RegisterInputOutput(Registers.PC));
        Transformation endOfLine = io.transformation(new RawData(io.get().intValue() + 1));
        endOfLine.apply();
        transforms.push(t.concatenate(new TransformationSequence(endOfLine)));
    }

    /**
     * Undoes the most recent transformation if it can. Returns whether it executed anything.
     *
     * @return true if a reverse transformation was applied, false otherwise
     * @throws SimulationException if an error occurs in the transformation.
     */
    public boolean undoLastTransformations() throws SimulationException {
        if (transforms.isEmpty()) {
            return false;
        }
        transforms.pop().invert().apply();
        return true;
    }

    /**
     * Causes the program to terminate naturally.
     */
    public void exit() {
        pc.setLong(lines.size() - 1);
    }

    /**
     * Causes the program to terminate naturally.
     */
    public long endPC() {
        return lines.size() - 1;
    }

    /**
     * A helper function to validate the state of the PC register.
     *
     * @return the validated PC.
     */
    private int validatePC() throws InvalidProgramCounterException {
        if (isError()) {
            throw new InvalidProgramCounterException(pc.getLong());
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

}
