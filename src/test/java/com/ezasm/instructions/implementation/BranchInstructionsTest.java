package com.ezasm.instructions.implementation;

import com.ezasm.parsing.Line;
import com.ezasm.parsing.ParseException;
import com.ezasm.simulation.Memory;
import com.ezasm.simulation.Simulator;
import com.ezasm.simulation.exception.SimulationException;
import com.ezasm.simulation.exception.SimulationInterruptedException;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.fail;

public class BranchInstructionsTest {

    private final Simulator simulator = new Simulator(Memory.DEFAULT_WORD_SIZE, Memory.DEFAULT_MEMORY_WORDS);

    @Test
    void iteration() {
        try {
            simulator.addLine(new Line("test:", new String[] {}));
            simulator.addLine(new Line("add", new String[] { "$t0", "$t0", "1" }));
            simulator.addLine(new Line("blt", new String[] { "$t0", "10", "test" }));
        } catch (ParseException e) {
            fail(e);
        }

        simulator.getRegisters().getRegister("$pc").setLong(0);
        try {
            simulator.executeProgramFromPC();
        } catch (SimulationException | SimulationInterruptedException e) {
            fail(e);
        }

        assertEquals(simulator.getRegisters().getRegister("$t0").getLong(), 10);
    }
}
