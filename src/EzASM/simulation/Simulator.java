package EzASM.simulation;

import EzASM.gui.Window;
import EzASM.instructions.InstructionDispatcher;
import EzASM.instructions.exception.InstructionDispatchException;
import EzASM.parsing.Lexer;
import EzASM.parsing.Line;
import EzASM.parsing.ParseException;

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

    private long delayMS = 500L;

    public Simulator() {
        this.memory = new Memory();
        this.registers = new Registers(this.memory.WORD_SIZE);
        this.lines = new ArrayList<>();
        this.labels = new HashMap<>();
        pc = registers.getRegister(Registers.PC);
        instructionDispatcher = new InstructionDispatcher(this);
    }

    public Simulator(int wordSize, int memorySize) {
        this.memory = new Memory(wordSize, memorySize);
        this.registers = new Registers(wordSize);
        this.lines = new ArrayList<>();
        this.labels = new HashMap<>();
        pc = registers.getRegister(Registers.PC);
        instructionDispatcher = new InstructionDispatcher(this);
    }

    public void resetMemory() {
        memory.reset();
        registers.reset();
    }

    public void resetAll() {
        resetMemory();
        lines.clear();
        labels.clear();
    }

    public boolean isDone() {
        return pc.getLong() == lines.size();
    }

    public Line readLine(String line) throws ParseException {
        Line lexed = Lexer.parseLine(line, labels, lines.size());
        lines.add(lexed);
        return lexed;
    }

    public void readMultiLineString(String content) throws ParseException {
        lines.addAll(Lexer.parseLines(content, labels));
    }

    public void executeLine(Line line) throws ParseException {
        if(line == null) return;
        try {
            instructionDispatcher.execute(line);
            Window.updateAll();
        } catch (InstructionDispatchException e) {
            throw new ParseException(e.getMessage());
        }
    }

    public void executeLine(String line) throws ParseException {
        executeLine(readLine(line));
    }

    public void runLinesFromPC(AtomicBoolean paused) throws ParseException {
        for(int i = (int) pc.getLong(); i < lines.size() && !Thread.interrupted(); ++i) {
            while(paused.get()) {
                try {
                    Thread.sleep(SimulationThread.SLEEP_INTERVAL);
                } catch (InterruptedException e) {
                    return;
                }
            }
            i = validatePC();
            if(isDone()) return;
            i = executeLineInLoop(i);
            try {
                Thread.sleep(delayMS);
            } catch (InterruptedException e) {
                return;
            }
        }
    }

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

    public void runOneLine() throws ParseException {
        int lineNumber = validatePC();
        executeLine(lines.get(lineNumber));
        int currentSP = validatePC();
        if(currentSP == lineNumber) {
            pc.setLong(currentSP + 1);
        } // otherwise the PC was set by the program to a certain line and should be read as such
        Window.updateAll();
    }

    private int executeLineInLoop(int i) throws ParseException {
        executeLine(lines.get(i));
        int currentPC = validatePC();
        if(currentPC == i) {
            pc.setLong(currentPC+1);
        } else {
            i = currentPC;
        }
        Window.updateAll();
        return i;
    }

    private int validatePC() {
        long number = pc.getLong();
        if(number < 0 || number > lines.size()) {
            // Guaranteed invalid SP
            // TODO handle better
            throw new RuntimeException();
        }
        return (int) number;
    }

    public Register getRegister(int register) {
        return registers.getRegister(register);
    }

    public Register getRegister(String register) {
        return registers.getRegister(register);
    }

    public String registryToString() {
        return registers.toString();
    }

    public Registers getRegisters() {
        return registers;
    }

    public Memory getMemory() {
        return memory;
    }

}
