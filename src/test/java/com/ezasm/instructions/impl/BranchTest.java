package com.ezasm.instructions.impl;

import com.ezasm.parsing.Line;
import com.ezasm.parsing.ParseException;
import com.ezasm.simulation.Memory;
import com.ezasm.simulation.Simulator;
import com.ezasm.simulation.exception.SimulationException;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.fail;

public class BranchTest {

    private final Simulator simulator = new Simulator(Memory.DEFAULT_WORD_SIZE, Memory.DEFAULT_MEMORY_WORDS);

    @Test
    void iteration() {
        ArrayList<Line> prgm = new ArrayList<>();
        try {
            prgm.add(new Line("test:", new String[] {}));
            prgm.add(new Line("add", new String[] { "$t0", "$t0", "1" }));
            prgm.add(new Line("blt", new String[] { "$t0", "10", "test" }));
        } catch (ParseException e) {
            fail(e);
        }

        simulator.getRegisters().getRegister("$pc").setLong(0);
        try {
            simulator.addLines(prgm);
            simulator.executeProgramFromPC();
        } catch (SimulationException e) {
            fail(e);
        }

        assertEquals(simulator.getRegisters().getRegister("$t0").getLong(), 10);
    }
}
