package EzASM;

import EzASM.instructions.InstructionDispatcher;
import EzASM.instructions.exception.InstructionDispatchException;
import EzASM.parsing.Lexer;
import EzASM.parsing.Line;
import EzASM.parsing.ParseException;

import java.io.*;
import java.util.*;

public class Simulator {

    private final Memory memory;
    private final Registers registers;
    private final InstructionDispatcher instructionDispatcher;

    private final List<Line> lines;
    private final Map<String, Integer> labels;

    public Simulator() {
        this.memory = new Memory();
        this.registers = new Registers(this.memory.WORD_SIZE);
        this.lines = new ArrayList<>();
        this.labels = new HashMap<>();
        instructionDispatcher = new InstructionDispatcher(this);
    }

    public Simulator(int wordSize, int memorySize) {
        this.memory = new Memory(wordSize, memorySize);
        this.registers = new Registers(wordSize);
        this.lines = new ArrayList<>();
        this.labels = new HashMap<>();
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

    public void runLinesFromStart() throws ParseException {
        Register sp = registers.getRegister("sp");
        for(int i = 0; i < lines.size(); ++i) {
            executeLine(lines.get(i));
            if(sp.getLong() == i) {
                sp.setLong(i+1);
            } else {
                i = (int) sp.getLong();
            }
        }
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

}
