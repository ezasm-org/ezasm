package com.ezasm.simulation;

import com.ezasm.instructions.InstructionDispatcher;
import com.ezasm.instructions.targets.inputoutput.RegisterInputOutput;
import com.ezasm.parsing.Lexer;
import com.ezasm.parsing.Line;
import com.ezasm.parsing.ParseException;
import com.ezasm.simulation.exception.InvalidFileIdentifierException;
import com.ezasm.simulation.exception.InvalidProgramCounterException;
import com.ezasm.simulation.exception.SimulationException;
import com.ezasm.simulation.exception.SimulationInterruptedException;
import com.ezasm.simulation.transform.Transformation;
import com.ezasm.simulation.transform.TransformationSequence;
import com.ezasm.simulation.transform.transformable.InputOutputTransformable;
import com.ezasm.util.FileIO;
import com.ezasm.util.RawData;
import org.apache.commons.lang3.tuple.ImmutablePair;
import org.apache.commons.lang3.tuple.Pair;
import org.apache.commons.collections4.BidiMap;
import org.apache.commons.collections4.bidimap.DualHashBidiMap;

import java.io.File;
import java.io.IOException;
import com.ezasm.util.RandomAccessFileStream;
import java.util.*;

/**
 * An implementation of a simulator which manages the memory, registers, and lines of code in an instance of the EzASM
 * runtime. Provides capabilities to run lines of code and access to the internal representation.
 */
public class Simulator {

    /**
     * FID of the file the user started running code from.
     */
    public static final int MAIN_FILE_IDENTIFIER = 0;

    private final Memory memory;
    private final Registers registers;
    private final InstructionDispatcher instructionDispatcher;

    private final BidiMap<String, Integer> fileToIdentifier;
    private final Map<Integer, List<Line>> fileIdToLineArray;
    private final Map<String, Pair<Integer, Long>> labelToFileIdAndLineNumber;
    private final Deque<TransformationSequence> transforms;

    private final Register pc;
    private final Register fi;
    // File descriptor table for runtime file IO (maps fd -> RandomAccessFileStream)
    private final Map<Integer, RandomAccessFileStream> fdTable;
    private int nextFd;
    private String executionDirectory;
    private boolean canUndo;

    /**
     * Constructs a Simulator with the given word size and memory size specifications.
     *
     * @param wordSize   the size of words in bytes for the program.
     * @param memorySize the size of the memory in words for the program.
     */
    public Simulator(int wordSize, int memorySize) {
        this.memory = new Memory(wordSize, memorySize);
        this.registers = new Registers(wordSize);
        this.instructionDispatcher = new InstructionDispatcher(this);

        this.fileToIdentifier = new DualHashBidiMap<>();
        this.fileIdToLineArray = new HashMap<>();
        this.labelToFileIdAndLineNumber = new HashMap<>();
        this.transforms = new ArrayDeque<>();

        this.pc = registers.getRegister(Registers.PC);
        this.fi = registers.getRegister(Registers.FID);
        this.executionDirectory = "";
        this.canUndo = false;
        this.fdTable = new HashMap<>();
        this.nextFd = 3; // reserve 0-2 for stdin/out/err if desired

        initialize();
    }

    /**
     * Initialization function which sets up any registers and memory.
     */
    private void initialize() {
        registers.getRegister(Registers.SP).setLong(memory.initialStackPointer());
        fi.setLong(MAIN_FILE_IDENTIFIER);
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
        fileToIdentifier.clear();
        fileIdToLineArray.clear();
        labelToFileIdAndLineNumber.clear();
        transforms.clear();
        initialize();
    }

    /**
     * Sets whether the program stores a list of all transformations done to the simulator. Setting this to true will
     *
     * @param canUndo
     */
    public void setAllowUndo(boolean canUndo) {
        this.canUndo = canUndo;
        if (!canUndo) {
            transforms.clear();
        }
    }

    /**
     * Gets all lines in current file.
     *
     * @return list of lines in current file
     */
    private List<Line> currentFileLines() {
        return Objects.requireNonNullElse(fileIdToLineArray.get((int) fi.getLong()), new ArrayList<>());
    }

    /**
     * Return true if the program has run off of the end of the code as in program completion, false otherwise.
     *
     * @return true if the program has run off of the end of the code as in program completion, false otherwise.
     */
    public boolean isDone() {
        return fileIdToLineArray.isEmpty() || pc.getLong() == currentFileLines().size();
    }

    /**
     * Return true if the program counter is in an error state, false otherwise.
     *
     * @return true if the program counter is in an error state, false otherwise.
     */
    public boolean isError() {
        long line = pc.getLong();
        return !fileIdToLineArray.isEmpty() && (line > currentFileLines().size() || line < 0);
    }

    /**
     * Adds the given line to the program for the corresponding file identifier.
     *
     * @param line   the line to add to the program.
     * @param fileId a number representing the file in which this line originated.
     */
    private void addLine(Line line, int fileId) throws ParseException {
        fileIdToLineArray.computeIfAbsent(fileId, k -> new ArrayList<>());
        if (line.isLabel()) {
            if (labelToFileIdAndLineNumber.containsKey(line.getLabel())) {
                throw new ParseException(String.format("Label %s already declared", line.getLabel()));
            }
            labelToFileIdAndLineNumber.put(line.getLabel(),
                    new ImmutablePair<>(fileId, (long) fileIdToLineArray.get(fileId).size()));
        }
        fileIdToLineArray.get(fileId).add(line);
        try {
            memory.addStringImmediates(line.getStringImmediates());
        } catch (SimulationException e) {
            throw new ParseException(e.getMessage());
        }
    }

    /**
     * Adds the given line to the program to the main file.
     *
     * @param line the line to add to the program.
     */
    public void addLine(Line line) throws ParseException {
        addLine(line, MAIN_FILE_IDENTIFIER);
    }

    /**
     * Parses the given text as a multi-line String. Then adds those lines to the program.
     *
     * @param file the relative path from the main file to the file to read lines from.
     */
    public void importLinesFromFile(String file) throws ParseException {
        String absoluteFilePath = executionDirectory + File.separator + file;
        if (fileToIdentifier.containsKey(absoluteFilePath)) {
            return;
        }
        int fileId = fileToIdentifier.size();
        fileToIdentifier.put(absoluteFilePath, fileId);

        try {
            String contentText = FileIO.readFile(new File(absoluteFilePath));
            List<Line> content = Lexer.parseLines(contentText);
            for (Line line : content) {
                addLine(line, fileId);
            }
        } catch (IOException e) {
            throw new ParseException(e.getMessage());
        }
    }

    /**
     * Adds the given lines to the main program. Then adds those lines to the program.
     *
     * @param lines    the lines.
     * @param mainFile the main program file.
     */
    public void addLines(List<Line> lines, File mainFile) throws ParseException {
        String parent = mainFile.getParent();
        parent = Objects.requireNonNullElse(parent, "");
        this.executionDirectory = parent;
        fileToIdentifier.put(mainFile.getAbsolutePath(), MAIN_FILE_IDENTIFIER);
        for (Line line : lines) {
            addLine(line, MAIN_FILE_IDENTIFIER);
        }
    }

    /**
     * Adds the given lines to the main program. Then adds those lines to the program.
     *
     * @param lines    the lines.
     * @param fileName the anonymous file name.
     */
    public void addAnonymousLines(List<Line> lines, String fileName) throws ParseException {
        this.executionDirectory = "";
        fileToIdentifier.put(fileName, MAIN_FILE_IDENTIFIER);
        for (Line line : lines) {
            addLine(line, MAIN_FILE_IDENTIFIER);
        }
    }

    /**
     * Executes the given line on the simulator.
     *
     * @param line the line to execute.
     * @throws SimulationException            if there is an error executing the line.
     * @throws SimulationInterruptedException if an interrupt occurs while executing.
     */
    public void runLine(Line line) throws SimulationException, SimulationInterruptedException {
        if (line.isLabel()) {
            applyTransformations(new TransformationSequence());
        } else {
            instructionDispatcher.execute(line);
        }
    }

    /**
     * Runs the program continuously until completion or error.
     *
     * @throws SimulationException            if there is an error executing the program.
     * @throws SimulationInterruptedException if an interrupt occurs while executing.
     */
    public void executeProgramFromPC() throws SimulationException, SimulationInterruptedException {
        while (!isDone() && !isError()) {
            executeLineFromPC();
        }
    }

    /**
     * Runs a single line of code from the current PC.
     *
     * @throws SimulationException            if there is an error executing the line.
     * @throws SimulationInterruptedException if an interrupt occurs while executing.
     */
    public void executeLineFromPC() throws SimulationException, SimulationInterruptedException {
        // Ensure a valid file identifier and program counter within that file
        validateFID();
        int lineNumber = validatePC();

        runLine(currentFileLines().get(lineNumber));
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

        if (canUndo) {
            transforms.push(t.concatenate(new TransformationSequence(endOfLine)));
        }
    }

    /**
     * Undoes the most recent transformation if it can. Returns whether it executed anything.
     *
     * @return true if a reverse transformation was applied, false otherwise
     * @throws SimulationException if an error occurs in the transformation.
     */
    public boolean undoLastTransformations() throws SimulationException {
        if (!canUndo || transforms.isEmpty()) {
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
        return currentFileLines().size() - 1;
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
     * A helper function to validate the state of the FID register.
     *
     * @return the validated FID.
     */
    private int validateFID() throws InvalidFileIdentifierException {
        int fid = (int) registers.getRegister(Registers.FID).getLong();
        if (!fileIdToLineArray.containsKey(fid)) {
            throw new InvalidFileIdentifierException(fid);
        }
        return fid;
    }

    /**
     * Get the labels mapping for the simulator
     *
     * @return the label mapping.
     */
    public Map<String, Pair<Integer, Long>> getLabelToFileIdAndLineNumber() {
        return labelToFileIdAndLineNumber;
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
     * Gets a file at a given file identifier.
     *
     * @return the file path in question.
     */
    public String getFile(int fid) {
        return fileToIdentifier.getKey(fid);
    }

    /**
     * Opens a file for reading and returns a file descriptor integer. The file path is resolved relative to the
     * simulator's execution directory when appropriate.
     *
     * @param path the path to the file to open (may be relative to the execution directory).
     * @return the allocated file descriptor integer.
     * @throws SimulationException if the file cannot be opened.
     */
    public int openFile(String path) throws SimulationException {
        String absoluteFilePath = executionDirectory.isEmpty() ? path : executionDirectory + File.separator + path;
        File file = new File(absoluteFilePath);
        try {
            RandomAccessFileStream raf = new RandomAccessFileStream(file);
            int fd = nextFd++;
            fdTable.put(fd, raf);
            return fd;
        } catch (IOException e) {
            throw new SimulationException(String.format("Unable to open file '%s': %s", path, e.getMessage()));
        }
    }

    /**
     * Closes the given file descriptor.
     *
     * @param fd the file descriptor to close.
     * @throws SimulationException if the fd is invalid or closing fails.
     */
    public void closeFile(int fd) throws SimulationException {
        RandomAccessFileStream raf = fdTable.remove(fd);
        if (raf == null) {
            throw new SimulationException(String.format("Invalid file descriptor %d", fd));
        }
        try {
            raf.close();
        } catch (IOException e) {
            throw new SimulationException(String.format("Unable to close file descriptor %d: %s", fd, e.getMessage()));
        }
    }

}
