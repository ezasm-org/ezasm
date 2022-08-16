package EzASM;

import EzASM.parsing.Line;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Simulator {

    private Memory memory;
    private Registers registers;

    private List<Line> lines;
    private Map<String, Integer> labels;

    public Simulator() {
        this.memory = new Memory();
        this.registers = new Registers();
        this.lines = new ArrayList<>();
        this.labels = new HashMap<>();
    }

    public void readLine(String line) {

    }

    public Register getRegister(int register) {
        throw new RuntimeException("Not implemented");
    }

}
