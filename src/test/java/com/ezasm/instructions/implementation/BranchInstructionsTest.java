package com.ezasm.instructions.implementation;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.fail;
import org.junit.jupiter.api.Test;

import com.ezasm.parsing.Line;
import com.ezasm.parsing.ParseException;
import com.ezasm.simulation.Memory;
import com.ezasm.simulation.Simulator;
import com.ezasm.simulation.exception.SimulationException;
import com.ezasm.simulation.exception.SimulationInterruptedException;

public class BranchInstructionsTest {

    @Test
    void bltTest() {
        Simulator simulator = new Simulator(Memory.DEFAULT_WORD_SIZE, Memory.DEFAULT_MEMORY_WORDS);
        try {
            simulator.addLine(new Line("start:", new String[] {}));
            simulator.addLine(new Line("add", new String[] { "$t0", "$t0", "1" }));
            simulator.addLine(new Line("blt", new String[] { "$t0", "10", "taken" }));
            simulator.addLine(new Line("add", new String[] { "$t0", "$t0", "100" }));
            simulator.addLine(new Line("taken:", new String[] {}));
            simulator.addLine(new Line("add", new String[] { "$t1", "$t1", "1" }));
        } catch (ParseException e) {
            fail(e);
        }

        simulator.getRegisters().getRegister("$pc").setLong(0);
        try {
            simulator.executeProgramFromPC();
        } catch (SimulationException | SimulationInterruptedException e) {
            fail(e);
        }

        assertEquals(simulator.getRegisters().getRegister("$t1").getLong(), 1);
        assertEquals(simulator.getRegisters().getRegister("$t0").getLong(), 1);
    }
}
