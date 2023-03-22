package com.ezasm.instructions.implementation;

import com.ezasm.parsing.Lexer;
import com.ezasm.parsing.Line;
import com.ezasm.parsing.ParseException;
import com.ezasm.simulation.Memory;
import com.ezasm.simulation.Registers;
import com.ezasm.simulation.Simulator;
import com.ezasm.simulation.exception.SimulationException;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.fail;

public class ArithmaticInstructionTest {

    private static Simulator simulator;

    @BeforeAll
    static void before() {
        simulator = new Simulator(Memory.DEFAULT_WORD_SIZE, Memory.DEFAULT_MEMORY_WORDS);
    }

    @Test
    void testAdd() {
        try {
            simulator.getRegisters().getRegister(Registers.S0).setLong(3);
            simulator.getRegisters().getRegister(Registers.S1).setLong(-100);
            simulator.getRegisters().getRegister(Registers.S2).setLong(32767);
            simulator.getRegisters().getRegister(Registers.S3).setLong(-32767);
            simulator.getRegisters().getRegister(Registers.S4).setLong(Long.MAX_VALUE);
            simulator.getRegisters().getRegister(Registers.S5).setLong(Long.MIN_VALUE);

            simulator.runLine(Lexer.parseLine("add $t0 5 10", 0));
            simulator.runLine(Lexer.parseLine("add $t1 -15 10", 0));
            simulator.runLine(Lexer.parseLine("add $t2 $s0 10", 0));
            simulator.runLine(Lexer.parseLine("add $t3 56 $s1", 0));
            simulator.runLine(Lexer.parseLine("add $t4 $s2 $s3", 0));
            simulator.runLine(Lexer.parseLine("add $t5 $s4 1", 0));
            simulator.runLine(Lexer.parseLine("add $t6 $s4 $s5", 0));
            simulator.runLine(Lexer.parseLine("add $t7 $s4 $s4", 0));
            simulator.runLine(Lexer.parseLine("add $t8 $s5 $s5", 0));

            assertEquals(simulator.getRegisters().getRegister(Registers.T0).getLong(), 15);
            assertEquals(simulator.getRegisters().getRegister(Registers.T1).getLong(), -5);
            assertEquals(simulator.getRegisters().getRegister(Registers.T2).getLong(), 13);
            assertEquals(simulator.getRegisters().getRegister(Registers.T3).getLong(), -44);
            assertEquals(simulator.getRegisters().getRegister(Registers.T4).getLong(), 0);
            assertEquals(simulator.getRegisters().getRegister(Registers.T5).getLong(), Long.MIN_VALUE);
            assertEquals(simulator.getRegisters().getRegister(Registers.T6).getLong(), -1);
            assertEquals(simulator.getRegisters().getRegister(Registers.T7).getLong(), -2);
            assertEquals(simulator.getRegisters().getRegister(Registers.T8).getLong(), 0);
        } catch (Exception e) {
            fail();
        }
    }
}
