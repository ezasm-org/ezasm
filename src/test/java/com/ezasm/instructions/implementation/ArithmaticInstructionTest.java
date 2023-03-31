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
            simulator.getRegisters().getRegister(Registers.S0).setLong(4);
            simulator.getRegisters().getRegister(Registers.S1).setLong(-10);
            simulator.getRegisters().getRegister(Registers.S2).setLong(32767);
            simulator.getRegisters().getRegister(Registers.S3).setLong(-424383);
            simulator.getRegisters().getRegister(Registers.S4).setLong(Long.MAX_VALUE);
            simulator.getRegisters().getRegister(Registers.S5).setLong(Long.MIN_VALUE);

            simulator.runLine(Lexer.parseLine("add $t0 5 10", 0));
            simulator.runLine(Lexer.parseLine("add $t1 -15 10", 1));
            simulator.runLine(Lexer.parseLine("add $t2 $s0 10", 2));
            simulator.runLine(Lexer.parseLine("add $t3 56 $s1", 3));
            simulator.runLine(Lexer.parseLine("add $t4 $s2 $s3", 4));
            simulator.runLine(Lexer.parseLine("add $t5 $s3 $s2", 5));
            simulator.runLine(Lexer.parseLine("add $t6 $s4 1", 6));
            simulator.runLine(Lexer.parseLine("add $t7 $s5 -1", 7));
            simulator.runLine(Lexer.parseLine("add $t8 $s4 $s5", 8));
            simulator.runLine(Lexer.parseLine("add $t9 $s5 $s4", 9));

            assertEquals(simulator.getRegisters().getRegister(Registers.T0).getLong(), 15);
            assertEquals(simulator.getRegisters().getRegister(Registers.T1).getLong(), -5);
            assertEquals(simulator.getRegisters().getRegister(Registers.T2).getLong(), 14);
            assertEquals(simulator.getRegisters().getRegister(Registers.T3).getLong(), 46);
            assertEquals(simulator.getRegisters().getRegister(Registers.T4).getLong(), -391616);
            assertEquals(simulator.getRegisters().getRegister(Registers.T5).getLong(), -391616);
            assertEquals(simulator.getRegisters().getRegister(Registers.T6).getLong(), Long.MIN_VALUE);
            assertEquals(simulator.getRegisters().getRegister(Registers.T7).getLong(), Long.MAX_VALUE);
            assertEquals(simulator.getRegisters().getRegister(Registers.T8).getLong(), -1);
            assertEquals(simulator.getRegisters().getRegister(Registers.T9).getLong(), -1);
        } catch (Exception e) {
            fail();
        }
    }

    @Test
    void testSub() {
        try {
            simulator.getRegisters().getRegister(Registers.S0).setLong(3);
            simulator.getRegisters().getRegister(Registers.S1).setLong(-15);
            simulator.getRegisters().getRegister(Registers.S2).setLong(8920321);
            simulator.getRegisters().getRegister(Registers.S3).setLong(-9309105);
            simulator.getRegisters().getRegister(Registers.S4).setLong(Long.MAX_VALUE);
            simulator.getRegisters().getRegister(Registers.S5).setLong(Long.MIN_VALUE);

            simulator.runLine(Lexer.parseLine("sub $t0 3 2", 0));
            simulator.runLine(Lexer.parseLine("sub $t1 2 3", 1));
            simulator.runLine(Lexer.parseLine("sub $t2 -5 7", 2));
            simulator.runLine(Lexer.parseLine("sub $t3 7 -5", 3));
            simulator.runLine(Lexer.parseLine("sub $t4 0 $zero", 4));
            simulator.runLine(Lexer.parseLine("sub $t5 3505 $s2", 5));
            simulator.runLine(Lexer.parseLine("sub $t6 $s3 -12930", 6));
            simulator.runLine(Lexer.parseLine("sub $t7 $s2 $s3", 7));
            simulator.runLine(Lexer.parseLine("sub $t8 $s4 $s5", 8));
            simulator.runLine(Lexer.parseLine("sub $t9 $s5 $s4", 9));

            assertEquals(simulator.getRegisters().getRegister(Registers.T0).getLong(), 1);
            assertEquals(simulator.getRegisters().getRegister(Registers.T1).getLong(), -1);
            assertEquals(simulator.getRegisters().getRegister(Registers.T2).getLong(), -12);
            assertEquals(simulator.getRegisters().getRegister(Registers.T3).getLong(), 12);
            assertEquals(simulator.getRegisters().getRegister(Registers.T4).getLong(), 0);
            assertEquals(simulator.getRegisters().getRegister(Registers.T5).getLong(), -8916816);
            assertEquals(simulator.getRegisters().getRegister(Registers.T6).getLong(), -9296175);
            assertEquals(simulator.getRegisters().getRegister(Registers.T7).getLong(), 18229426);
            assertEquals(simulator.getRegisters().getRegister(Registers.T8).getLong(), -1);
            assertEquals(simulator.getRegisters().getRegister(Registers.T9).getLong(), 1);
        } catch (Exception e) {
            fail();
        }
    }

    @Test
    void testMul() {
        try {
            simulator.getRegisters().getRegister(Registers.S0).setLong(3);
            simulator.getRegisters().getRegister(Registers.S1).setLong(5);
            simulator.getRegisters().getRegister(Registers.S2).setLong(-10);
            simulator.getRegisters().getRegister(Registers.S3).setLong(Long.MAX_VALUE);
            simulator.getRegisters().getRegister(Registers.S4).setLong(Long.MIN_VALUE);

            // reflexive, negative, zero, max, min
            simulator.runLine(Lexer.parseLine("mul $t0 $s0 $s1", 0));
            simulator.runLine(Lexer.parseLine("mul $t1 $s2 $s3", 0));

            assertEquals(simulator.getRegisters().getRegister(Registers.T0), 15);
            assertEquals(simulator.getRegisters().getRegister(Registers.T1), -50);
        } catch (Exception e) {
            fail();
        }
    }

    void testDiv() {
        try {

        } catch (Exception e) {
            fail();
        }
    }
}
