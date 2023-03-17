package com.ezasm.simulation;

import com.ezasm.instructions.InstructionDispatcher;
import com.ezasm.instructions.exception.InstructionDispatchException;
import com.ezasm.instructions.targets.inputoutput.RegisterInputOutput;
import com.ezasm.parsing.Lexer;
import com.ezasm.parsing.Line;
import com.ezasm.parsing.ParseException;
import com.ezasm.simulation.exception.InvalidProgramCounterException;
import com.ezasm.simulation.exception.SimulationException;
import com.ezasm.simulation.transform.Transformation;
import com.ezasm.simulation.transform.TransformationSequence;
import com.ezasm.simulation.transform.transformable.InputOutputTransformable;
import com.ezasm.util.FileIO;
import com.ezasm.util.RawData;
import org.apache.commons.lang3.tuple.ImmutablePair;
import org.apache.commons.lang3.tuple.Pair;

import java.io.File;
import java.io.IOException;
import java.util.*;

/**
 * An implementation of a simulator which manages the memory, registers, and lines of code in an instance of the EzASM
 * runtime. Provides capabilities to run lines of code and access to the internal representation.
 */
public class Simulator {

    private final Memory memory;
    private final Registers registers;
    private final InstructionDispatcher instructionDispatcher;
    private final Register pc;

    private final Map<String, List<Line>> linesByFileMap;
    private final Map<String, Pair<String, Long>> labels;
    private final Deque<TransformationSequence> transforms;
    private final Deque<String> fileCallstack;

    private String executionDirectory;
    private String mainFile;
    private String currentFile;

    /**
     * Constructs a Simulator with the given word size and memory size specifications.
     *
     * @param wordSize   the size of words in bytes for the program.
     * @param memorySize the size of the memory in words for the program.
     */
    public Simulator(int wordSize, int memorySize) {
        this.memory = new Memory(wordSize, memorySize);
        this.registers = new Registers(wordSize);
        this.linesByFileMap = new HashMap<>();
        this.labels = new HashMap<>();
        this.transforms = new ArrayDeque<>();
        this.fileCallstack = new ArrayDeque<>();
        this.mainFile = "/";
        this.currentFile = mainFile;
        this.instructionDispatcher = new InstructionDispatcher(this);
        this.pc = registers.getRegister(Registers.PC);
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
        linesByFileMap.clear();
        labels.clear();
        transforms.clear();
        fileCallstack.clear();
        initialize();
        currentFile = mainFile;
    }

    /**
     * Return true if the program has run off of the end of the code as in program completion, false otherwise.
     *
     * @return true if the program has run off of the end of the code as in program completion, false otherwise.
     */
    public boolean isDone() {
        return linesByFileMap.isEmpty() || pc.getLong() == linesByFileMap.get(currentFile).size();
    }

    /**
     * Return true if the program counter is in an error state, false otherwise.
     *
     * @return true if the program counter is in an error state, false otherwise.
     */
    public boolean isError() {
        long line = pc.getLong();
        return !linesByFileMap.isEmpty() && (line > linesByFileMap.get(currentFile).size() || line < 0);
    }

    /**
     * Adds the given line to the program.
     *
     * @param line the line to add to the program.
     */
    public void addLine(Line line) throws ParseException {
        addLine(line, currentFile);
    }

    /**
     * Adds the given line to the program.
     *
     * @param line the line to add to the program.
     * @param file the file in which this line originated.
     */
    private void addLine(Line line, String file) throws ParseException {
        linesByFileMap.computeIfAbsent(file, k -> new ArrayList<>());
        if (line.isLabel()) {
            if (labels.containsKey(line.getLabel())) {
                throw new ParseException(String.format("Label %s already declared", line.getLabel()));
            }
            labels.put(line.getLabel(), new ImmutablePair<>(file, (long) linesByFileMap.get(file).size()));
        }
        linesByFileMap.get(file).add(line);
        try {
            memory.addStringImmediates(line.getStringImmediates());
        } catch (SimulationException e) {
            throw new ParseException(e.getMessage());
        }
    }

    /**
     * Parses the given text as a multi-line String. Then adds those lines to the program.
     *
     * @param file the relative path from the main file to the file to read lines from.
     */
    public void addLinesByFile(String file) throws ParseException {
        try {
            String absoluteFilePath = String.format("%s%s%s", executionDirectory, File.separator, file);
            if (linesByFileMap.containsKey(absoluteFilePath)) {
                return;
            }
            String contentText = FileIO.readFile(new File(absoluteFilePath));
            List<Line> content = Lexer.parseLines(contentText);
            for (Line line : content) {
                addLine(line, absoluteFilePath);
            }
        } catch (IOException e) {
            throw new ParseException(e.getMessage());
        }
    }

    /**
     * Parses the given text as a multi-line String. Then adds those lines to the program.
     *
     * @param file the main program file.
     */
    public void addLines(File file) throws ParseException {
        file = file.getAbsoluteFile();
        currentFile = mainFile = file.getAbsolutePath();
        executionDirectory = file.getParent();
        addLinesByFile(file.getName());
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

    /**
     * Runs the program continuously until completion or error.
     *
     * @throws SimulationException if there is an error executing the program.
     */
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
        runLine(linesByFileMap.get(currentFile).get(lineNumber));
        validatePC();
    }

    /**
     * Applies the given transformation sequence to the simulation.
     *
     * @param t the transformation sequence to apply.
     *
     * @throws SimulationException if there is an error in applying the transformation.
     */
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
     * Gets the last valid program counter.
     *
     * @return the last valid program counter.
     */
    public long endPC() {
        return linesByFileMap.get(currentFile).size() - 1;
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
     * Adds a new file as the top element of the file switch stack.
     *
     * @param file the new top element of the file switch stack.
     */
    public void pushFileSwitch(String file) {
        fileCallstack.push(currentFile);
        currentFile = file;
    }

    /**
     * Gets and removes the top element of the file switch stack.
     *
     * @return the top element of the file switch stack.
     */
    public String popFileSwitch() {
        currentFile = fileCallstack.pop();
        return currentFile;
    }

    /**
     * Gets the top element of the file switch stack.
     *
     * @return the top element of the file switch stack.
     */
    public String peekFileSwitch() {
        return fileCallstack.peek();
    }

    /**
     * Gets the program's currently executing file.
     *
     * @return the program's currently executing file file if it exists, "/" otherwise.
     */
    public String getCurrentFile() {
        return currentFile;
    }

    /**
     * Gets the program's main file.
     *
     * @return the program's main file if it exists, "/" otherwise.
     */
    public String getMainFile() {
        return mainFile;
    }

    /**
     * Get the labels mapping for the simulator
     *
     * @return the label mapping.
     */
    public Map<String, Pair<String, Long>> getLabels() {
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
