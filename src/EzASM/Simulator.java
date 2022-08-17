package EzASM;

import EzASM.parsing.Lexer;
import EzASM.parsing.Line;
import EzASM.parsing.ParseException;

import java.util.*;

public class Simulator {

    private final Memory memory;
    private final Registers registers;

    private final List<Line> lines;
    private final Map<String, Integer> labels;

    public Simulator() {
        this.memory = new Memory();
        this.registers = new Registers();
        this.lines = new ArrayList<>();
        this.labels = new HashMap<>();
        System.out.println(registers);
    }

    public void readLine(String line) throws ParseException {
        line = line.trim().replaceAll("\s+", " ");
        if(Lexer.isComment(line)) return;
        if(Lexer.isLabel(line)) {
            labels.putIfAbsent(line, lines.size());
        }
        String[] tokens = line.split("[ ,]");
        if(tokens.length < 2) {
            // ERROR too few tokens to be a line
            throw new ParseException("Too few tokens found: likely an incomplete line");
        }
        String[] args = Arrays.copyOfRange(tokens, 2, tokens.length);
        Line lexed = new Line(tokens[0], tokens[1], args);
        System.out.println(lexed);
        lines.add(lexed);
    }

    public Register getRegister(int register) {
        return registers.getRegister(register);
    }

    public Register getRegister(String register) {
        return registers.getRegister(register);
    }

}
