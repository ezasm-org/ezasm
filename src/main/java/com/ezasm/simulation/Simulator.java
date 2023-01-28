package com.ezasm.simulation;

import com.ezasm.gui.Window;
import com.ezasm.instructions.InstructionDispatcher;
import com.ezasm.instructions.exception.InstructionDispatchException;
import com.ezasm.parsing.Lexer;
import com.ezasm.parsing.Line;
import com.ezasm.parsing.ParseException;

import java.util.*;
import java.util.concurrent.atomic.AtomicBoolean;

/**
 * The main controller class. Manages the memory, registers, and lines.
 */
public class Simulator {

    private final Memory memory;
    private final Registers registers;
    private final InstructionDispatcher instructionDispatcher;
    private final Register pc;

    private final List<Line> lines;
    private final Map<String, Integer> labels;

    // The delay in ms before the next instruction is read
    private long delayMS = 500L;

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
    }

    /**
     * Constructs a Simulator with the given word size and memory size specifications.
     * @param wordSize the size of words in bytes for the program.
     * @param memorySize the size of the memory in words for the program.
     */
    public Simulator(int wordSize, int memorySize) {
        this.memory = new Memory(wordSize, memorySize);
        this.registers = new Registers(wordSize);
        this.lines = new ArrayList<>();
        this.labels = new HashMap<>();
        pc = registers.getRegister(Registers.PC);
        instructionDispatcher = new InstructionDispatcher(this);
    }

    /**
     * Resets the contents of memory and registers.
     */
    public void resetMemory() {
        memory.reset();
        registers.reset();
    }

    /**
     * Resets the contents of memory and registers as well as stored lines and labels.
     */
    public void resetAll() {
        resetMemory();
        lines.clear();
        labels.clear();
    }

    /**
     * Return true if the program has run off of the end of the code as in program completion, false otherwise.
     * @return true if the program has run off of the end of the code as in program completion, false otherwise.
     */
    public boolean isDone() {
        return pc.getLong() == lines.size();
    }

    /**
     * Return true if the program counter is in an error state, false otherwise.
     * @return true if the program counter is in an error state, false otherwise.
     */
    public boolean isErrored() {
        long line = pc.getLong();
        return line > lines.size() || line < 0;
    }

    /**
     * Parses the given line and returns it. Adds the line to the program as well.
     * @param line the line of text to parse.
     * @return the generated line.
     * @throws ParseException if there is an error parsing the line.
     */
    public Line readLine(String line) throws ParseException {
        Line lexed = Lexer.parseLine(line, labels, lines.size());
        lines.add(lexed);
        return lexed;
    }

    /**
     * Parses the given text as a multi-line String. Then adds those lines to the program.
     * @param content the multi-line string to parse.
     * @throws ParseException if there was an error in parsing any line.
     */
    public void readMultiLineString(String content) throws ParseException {
        lines.addAll(Lexer.parseLines(content, labels));
    }

    /**
     * Executes the given line on the simulator.
     * @param line the line to execute.
     * @throws ParseException if there is an error executing the line.
     */
    public void executeLine(Line line) throws ParseException {
        if(line == null) return;
        try {
            instructionDispatcher.execute(line);
            Window.updateAll(validatePC());
        } catch (InstructionDispatchException e) {
            throw new ParseException(e.getMessage());
        }
    }

    /**
     * Executes the given line on the simulator.
     * @param line the text representation of the line to execute.
     * @throws ParseException if there is an error parsing or executing the line.
     */
    public void executeLine(String line) throws ParseException {
        executeLine(readLine(line));
    }

    /**
     * Runs the program to completion or error state from the current state of the PC.
     * Allows for pausing of execution with the paused variable.
     * @param paused an AtomicBoolean which allows for control over whether the execution
     *               of this is paused.
     * @throws ParseException if there is an error executing any line.
     */
    public void runLinesFromPC(AtomicBoolean paused) throws ParseException {
        for(int i = (int) pc.getLong(); i < lines.size() && !Thread.interrupted(); ++i) {
            while(paused.get()) {
                try {
                    Thread.sleep(SimulationThread.SLEEP_INTERVAL);
                } catch (InterruptedException e) {
                    return;
                }
            }
            try {
                Thread.sleep(delayMS);
            } catch (InterruptedException e) {
                return;
            }
            i = validatePC();
            if(isDone()) return;
            i = executeLineInLoop(i);
        }
    }

    /**
     * Runs the program to completion or error state from the current state of the PC.
     * @throws ParseException if there is an error executing any line.
     */
    public void runLinesFromPC() throws ParseException {
        for(int i = (int) pc.getLong(); i < lines.size() && !Thread.interrupted(); ++i) {
            i = executeLineInLoop(i);
            try {
                Thread.sleep(delayMS);
            } catch (InterruptedException e) {
                break;
            }
        }
    }

    /**
     * Runs a single line of code from the current PC.
     * @throws ParseException if there is an error executing the line.
     */
    public void runOneLine() throws ParseException {
        int lineNumber = validatePC();
        executeLine(lines.get(lineNumber));
        int currentSP = validatePC();
        if(currentSP == lineNumber) {
            pc.setLong(currentSP + 1);
        } // otherwise the PC was set by the program to a certain line and should be read as such
        Window.updateAll(validatePC());
    }

    /**
     * Helper method to execute a line in a loop and return the new PC.
     * @param i the current PC value.
     * @return the new PC value.
     * @throws ParseException if an error occurred within execution.
     */
    private int executeLineInLoop(int i) throws ParseException {
        executeLine(lines.get(i));
        int currentPC = validatePC();
        if(currentPC == i) {
            pc.setLong(currentPC+1);
        } else {
            i = currentPC;
        }
        Window.updateAll(validatePC());
        return i;
    }

    /**
     * A helper function to validate the state of the PC register.
     * @return the validated PC.
     */
    private int validatePC() {
        long number = pc.getLong();
        if(number < 0 || number > lines.size()) {
            // Guaranteed invalid SP
            // TODO handle better
            throw new RuntimeException();
        }
        return (int) number;
    }

    /**
     * Gets the register of the given register reference.
     * @param register the register's reference number.
     * @return the register object corresponding to the register reference number.
     */
    public Register getRegister(int register) {
        return registers.getRegister(register);
    }

    /**
     * Gets the register of the given name.
     * @param register the register's name.
     * @return the register object corresponding to the register name.
     */
    public Register getRegister(String register) {
        return registers.getRegister(register);
    }

    /**
     * Gets the registers representation of the program.
     * @return the registers representation of the program.
     */
    public Registers getRegisters() {
        return registers;
    }

    /**
     * Gets the memory representation of the program.
     * @return the memory representation of the program.
     */
    public Memory getMemory() {
        return memory;
    }


    /**
     * Gets the list of parsed lines
     * @return the list of parsed lines
     */
    public List<Line> getLines() {
        return lines;
    }



}
