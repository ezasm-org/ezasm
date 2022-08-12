package EzASM;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Simulator {

    private Memory memory;
    private Registers registers;

    private List<String> lines;
    private Map<String, Integer> labels;

    public Simulator() {
        this.memory = new Memory();
        this.registers = new Registers();
        this.lines = new ArrayList<>();
        this.labels = new HashMap<>();
    }

    public void readLine(String line) {

    }

}
