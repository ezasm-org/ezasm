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

public class ComparisonInstructionsTest {

    private final Simulator simulator = new Simulator(Memory.DEFAULT_WORD_SIZE, Memory.DEFAULT_MEMORY_WORDS);

    @Test
    void testLessThan() throws SimulationException {
        try {
            simulator.addLine(new Line("add", new String[] { "$t0", "$t0", "1" }));
            simulator.addLine(new Line("add", new String[] { "$t1", "$t1", "2" }));
            simulator.addLine(new Line("slt", new String[] { "$t2", "$t0", "$t1" }));
        } catch (ParseException e) {
            fail(e);
        }

        simulator.getRegisters().getRegister("$pc").setLong(0);

        try {
            simulator.executeProgramFromPC();
        } catch (SimulationException | SimulationInterruptedException e) {
            fail(e);
        }

        assertEquals(simulator.getRegisters().getRegister("$t2").getLong(), 1);
    }

    @Test
    void testLessThanEqual() throws SimulationException {
        try {
            simulator.addLine(new Line("add", new String[] { "$t0", "$t0", "2" }));
            simulator.addLine(new Line("add", new String[] { "$t1", "$t1", "2" }));
            simulator.addLine(new Line("sle", new String[] { "$t2", "$t0", "$t1" }));
        } catch (ParseException e) {
            fail(e);
        }

        simulator.getRegisters().getRegister("$pc").setLong(0);

        try {
            simulator.executeProgramFromPC();
        } catch (SimulationException | SimulationInterruptedException e) {
            fail(e);
        }

        assertEquals(simulator.getRegisters().getRegister("$t2").getLong(), 1);
    }

    @Test
    void testGreaterThan() throws SimulationException {
        try {
            simulator.addLine(new Line("add", new String[] { "$t0", "$t0", "2" }));
            simulator.addLine(new Line("add", new String[] { "$t1", "$t1", "1" }));
            simulator.addLine(new Line("sgt", new String[] { "$t2", "$t0", "$t1" }));
        } catch (ParseException e) {
            fail(e);
        }

        simulator.getRegisters().getRegister("$pc").setLong(0);

        try {
            simulator.executeProgramFromPC();
        } catch (SimulationException | SimulationInterruptedException e) {
            fail(e);
        }

        assertEquals(simulator.getRegisters().getRegister("$t2").getLong(), 1);
    }

    @Test
    void testGreaterThanEqual() throws SimulationException {
        try {
            simulator.addLine(new Line("add", new String[] { "$t0", "$t0", "2" }));
            simulator.addLine(new Line("add", new String[] { "$t1", "$t1", "2" }));
            simulator.addLine(new Line("sge", new String[] { "$t2", "$t0", "$t1" }));
        } catch (ParseException e) {
            fail(e);
        }

        simulator.getRegisters().getRegister("$pc").setLong(0);

        try {
            simulator.executeProgramFromPC();
        } catch (SimulationException | SimulationInterruptedException e) {
            fail(e);
        }

        assertEquals(simulator.getRegisters().getRegister("$t2").getLong(), 1);
    }

    @Test
    void testEqual() throws SimulationException {
        try {
            simulator.addLine(new Line("add", new String[] { "$t0", "$t0", "2" }));
            simulator.addLine(new Line("add", new String[] { "$t1", "$t1", "2" }));
            simulator.addLine(new Line("seq", new String[] { "$t2", "$t0", "$t1" }));
        } catch (ParseException e) {
            fail(e);
        }

        simulator.getRegisters().getRegister("$pc").setLong(0);

        try {
            simulator.executeProgramFromPC();
        } catch (SimulationException | SimulationInterruptedException e) {
            fail(e);
        }

        assertEquals(simulator.getRegisters().getRegister("$t2").getLong(), 1);
    }

    @Test
    void testNotEqual() throws SimulationException {
        try {
            simulator.addLine(new Line("add", new String[] { "$t0", "$t0", "1" }));
            simulator.addLine(new Line("add", new String[] { "$t1", "$t1", "2" }));
            simulator.addLine(new Line("sne", new String[] { "$t2", "$t0", "$t1" }));
        } catch (ParseException e) {
            fail(e);
        }

        simulator.getRegisters().getRegister("$pc").setLong(0);

        try {
            simulator.executeProgramFromPC();
        } catch (SimulationException | SimulationInterruptedException e) {
            fail(e);
        }

        assertEquals(simulator.getRegisters().getRegister("$t2").getLong(), 1);
    }
}
