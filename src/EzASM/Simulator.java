package EzASM;

import EzASM.gui.Window;
import EzASM.instructions.InstructionDispatcher;
import EzASM.instructions.exception.InstructionDispatchException;
import EzASM.parsing.Lexer;
import EzASM.parsing.Line;
import EzASM.parsing.ParseException;

import java.io.*;
import java.util.*;
import java.util.concurrent.atomic.AtomicBoolean;

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
        pc = registers.getRegister("pc");
        instructionDispatcher = new InstructionDispatcher(this);
    }

    public Simulator(int wordSize, int memorySize) {
        this.memory = new Memory(wordSize, memorySize);
        this.registers = new Registers(wordSize);
        this.lines = new ArrayList<>();
        this.labels = new HashMap<>();
        pc = registers.getRegister("pc");
        instructionDispatcher = new InstructionDispatcher(this);
    }

    public Line readLine(String line) throws ParseException {
        line = line.replaceAll("[\s,;]+", " ").trim();
        if(Lexer.isComment(line)) return null;
        if(Lexer.isLabel(line)) {
            labels.putIfAbsent(line, lines.size());
            return null;
        }
        String[] tokens = line.split("[ ,]");
        if(tokens.length == 0) {
            // Empty line
            return null;
        } else if(tokens.length < 2) {
            // ERROR too few tokens to be a line
            throw new ParseException(String.format("Too few tokens found: '%s' is likely an incomplete statement", line));
        }

        String[] args = Arrays.copyOfRange(tokens, 2, tokens.length);
        Line lexed = new Line(tokens[0], tokens[1], args);
        System.out.println(lexed);
        lines.add(lexed);
        return lexed;
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

    public void readFile(String filepath) throws ParseException {
        File file = new File(filepath);
        if(!file.exists() || !file.canRead() ) {
            throw new ParseException("Could not load specified file");
        }
        List<String> linesRead = new ArrayList<>();
        StringBuilder sb = new StringBuilder();

        try {
            BufferedReader reader = new BufferedReader(new FileReader(file));
            int r = reader.read();
            do {
                char c = (char) r;
                if (c == '\n' || c == ';') {
                    if(sb.length() > 0) {
                        linesRead.add(sb.toString());
                        sb.delete(0, sb.length());
                    }
                } else {
                    sb.append(c);
                }
                r = reader.read();
            } while (r != -1);
        } catch (IOException e) {
            throw new ParseException(e.getMessage());
        }

        for(int i = 0; i < linesRead.size(); ++i) {
            readLine(linesRead.get(i));
        }
    }

    public void runLinesFromStart(AtomicBoolean paused) throws ParseException {
        for(int i = 0; i < lines.size() && !Thread.interrupted(); ++i) {
            while(paused.get()) {
                try {
                    Thread.sleep(SimulationThread.SLEEP_INTERVAL);
                } catch (InterruptedException e) {
                    break;
                }
            }
            i = executeLineInLoop(i);
            try {
                Thread.sleep(delayMS);
            } catch (InterruptedException e) {
                break;
            }
            System.out.println(registryToString());
        }
    }

    public void runLinesFromStart() throws ParseException {
        for(int i = 0; i < lines.size() && !Thread.interrupted(); ++i) {
            i = executeLineInLoop(i);
            try {
                Thread.sleep(delayMS);
            } catch (InterruptedException e) {
                break;
            }
        }
    }

    private int executeLineInLoop(int i) throws ParseException {
        executeLine(lines.get(i));
        int currentSP = validatePC();
        if(currentSP == i) {
            pc.setLong(currentSP+1);
        } else {
            i = currentSP;
        }
        Window.updateAll();
        return i;
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

    public Register getRegister(int register) {
        return registers.getRegister(register);
    }

    public Register getRegister(String register) {
        return registers.getRegister(register);
    }

    public String registryToString() {
        return registers.toString();
    }

    private int validatePC() {
        long number = pc.getLong();
        if(number > lines.size() || number < 0) {
            // Guaranteed invalid SP
            // TODO handle better
            throw new RuntimeException();
        }
        return (int) number;
    }

    public Registers getRegisters() {
        return registers;
    }

    public Memory getMemory() {
        return memory;
    }

}
