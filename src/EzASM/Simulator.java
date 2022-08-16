package EzASM;

import EzASM.parsing.Line;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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

    public void readLine(String line) {

    }

    public Register getRegister(int register) {
        return registers.getRegister(register);
    }

    public Register getRegister(String register) {
        return registers.getRegister(register);
    }

}
