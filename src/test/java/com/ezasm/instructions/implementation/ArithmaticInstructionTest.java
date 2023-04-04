package com.ezasm.instructions.implementation;

import com.ezasm.parsing.Lexer;
import com.ezasm.parsing.Line;
import com.ezasm.parsing.ParseException;
import com.ezasm.simulation.Memory;
import com.ezasm.simulation.Register;
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

            simulator.runLine(Lexer.parseLine("mul $t0 $s0 $s1", 0));
            simulator.runLine(Lexer.parseLine("mul $t1 $s1 $s0", 1));
            simulator.runLine(Lexer.parseLine("mul $t2 $s1 $s2", 2));
            simulator.runLine(Lexer.parseLine("mul $t3 $s2 $s1", 3));
            simulator.runLine(Lexer.parseLine("mul $t4 $s0 $zero", 4));
            simulator.runLine(Lexer.parseLine("mul $t5 $zero $s0", 5));
            simulator.runLine(Lexer.parseLine("mul $t6 $zero $zero", 6));
            simulator.runLine(Lexer.parseLine("mul $t7 $s3 $s3", 7));
            simulator.runLine(Lexer.parseLine("mul $t8 $s4 $s4", 8));
            simulator.runLine(Lexer.parseLine("mul $t9 $s3 $s4", 9));

            assertEquals(simulator.getRegisters().getRegister(Registers.T0).getLong(), 15);
            assertEquals(simulator.getRegisters().getRegister(Registers.T1).getLong(), 15);
            assertEquals(simulator.getRegisters().getRegister(Registers.T2).getLong(), -50);
            assertEquals(simulator.getRegisters().getRegister(Registers.T3).getLong(), -50);
            assertEquals(simulator.getRegisters().getRegister(Registers.T4).getLong(), 0);
            assertEquals(simulator.getRegisters().getRegister(Registers.T5).getLong(), 0);
            assertEquals(simulator.getRegisters().getRegister(Registers.T6).getLong(), 0);
            assertEquals(simulator.getRegisters().getRegister(Registers.T7).getLong(), Long.MAX_VALUE * Long.MAX_VALUE);
            assertEquals(simulator.getRegisters().getRegister(Registers.T8).getLong(), Long.MIN_VALUE * Long.MIN_VALUE);
            assertEquals(simulator.getRegisters().getRegister(Registers.T9).getLong(), Long.MAX_VALUE * Long.MIN_VALUE);
        } catch (Exception e) {
            fail();
        }
    }

    @Test
    void testDiv() {
        try {
            simulator.getRegisters().getRegister(Registers.S0).setLong(12);
            simulator.getRegisters().getRegister(Registers.S1).setLong(-12);
            simulator.getRegisters().getRegister(Registers.S2).setLong(3);
            simulator.getRegisters().getRegister(Registers.S3).setLong(-3);
            simulator.getRegisters().getRegister(Registers.S4).setLong(5);
            simulator.getRegisters().getRegister(Registers.S5).setLong(-5);
            simulator.getRegisters().getRegister(Registers.S6).setLong(15);
            simulator.getRegisters().getRegister(Registers.S7).setLong(-15);
            simulator.getRegisters().getRegister(Registers.S8).setLong(Long.MAX_VALUE);
            simulator.getRegisters().getRegister(Registers.S9).setLong(Long.MIN_VALUE);

            simulator.runLine(Lexer.parseLine("div $t0 $s0 $s2", 0));
            simulator.runLine(Lexer.parseLine("div $t1 $s0 $s3", 1));
            simulator.runLine(Lexer.parseLine("div $t2 $s1 $s2", 2));
            simulator.runLine(Lexer.parseLine("div $t3 $s1 $s3", 3));
            simulator.runLine(Lexer.parseLine("div $t4 $s0 $s4", 4));
            simulator.runLine(Lexer.parseLine("div $t5 $s0 $s5", 5));
            simulator.runLine(Lexer.parseLine("div $t6 $s0 $s6", 6));
            simulator.runLine(Lexer.parseLine("div $t7 $s0 $s7", 7));
            simulator.runLine(Lexer.parseLine("div $t8 $s8 $s9", 8));
            simulator.runLine(Lexer.parseLine("div $t9 $s9 $s8", 9));

            assertEquals(simulator.getRegisters().getRegister(Registers.T0).getLong(), 4);
            assertEquals(simulator.getRegisters().getRegister(Registers.T1).getLong(), -4);
            assertEquals(simulator.getRegisters().getRegister(Registers.T2).getLong(), -4);
            assertEquals(simulator.getRegisters().getRegister(Registers.T3).getLong(), 4);
            assertEquals(simulator.getRegisters().getRegister(Registers.T4).getLong(), 2);
            assertEquals(simulator.getRegisters().getRegister(Registers.T5).getLong(), -2);
            assertEquals(simulator.getRegisters().getRegister(Registers.T6).getLong(), 0);
            assertEquals(simulator.getRegisters().getRegister(Registers.T7).getLong(), 0);
            assertEquals(simulator.getRegisters().getRegister(Registers.T8).getLong(), 0);
            assertEquals(simulator.getRegisters().getRegister(Registers.T9).getLong(), -1);
        } catch (Exception e) {
            fail();
        }
    }

    @Test
    void testAnd() {
        try {
            simulator.getRegisters().getRegister(Registers.S0).setLong(0);
            simulator.getRegisters().getRegister(Registers.S1).setLong(-1);
            simulator.getRegisters().getRegister(Registers.S2).setLong(3);
            simulator.getRegisters().getRegister(Registers.S3).setLong(-5);
            simulator.getRegisters().getRegister(Registers.S4).setLong(928235345);
            simulator.getRegisters().getRegister(Registers.S5).setLong(-671230823);
            simulator.getRegisters().getRegister(Registers.S6).setLong(Long.MAX_VALUE);
            simulator.getRegisters().getRegister(Registers.S7).setLong(Long.MIN_VALUE);

            simulator.runLine(Lexer.parseLine("and $t0 $s0 $s0", 0));
            simulator.runLine(Lexer.parseLine("and $t1 $s1 $s1", 1));
            simulator.runLine(Lexer.parseLine("and $t2 $s0 $s1", 2));
            simulator.runLine(Lexer.parseLine("and $t3 $s1 $s0", 3));
            simulator.runLine(Lexer.parseLine("and $t4 $s2 $s3", 4));
            simulator.runLine(Lexer.parseLine("and $t5 $s3 $s2", 5));
            simulator.runLine(Lexer.parseLine("and $t6 $s4 $s5", 6));
            simulator.runLine(Lexer.parseLine("and $t7 $s5 $s4", 7));
            simulator.runLine(Lexer.parseLine("and $t8 $s6 $s7", 8));
            simulator.runLine(Lexer.parseLine("and $t9 $s7 $s6", 9));

            assertEquals(simulator.getRegisters().getRegister(Registers.T0).getLong(), 0);
            assertEquals(simulator.getRegisters().getRegister(Registers.T1).getLong(), -1);
            assertEquals(simulator.getRegisters().getRegister(Registers.T2).getLong(), 0);
            assertEquals(simulator.getRegisters().getRegister(Registers.T3).getLong(), 0);
            assertEquals(simulator.getRegisters().getRegister(Registers.T4).getLong(), -5 & 3);
            assertEquals(simulator.getRegisters().getRegister(Registers.T5).getLong(), 3 & -5);
            assertEquals(simulator.getRegisters().getRegister(Registers.T6).getLong(), -671230823 & 928235345);
            assertEquals(simulator.getRegisters().getRegister(Registers.T7).getLong(), 928235345 & -671230823);
            assertEquals(simulator.getRegisters().getRegister(Registers.T8).getLong(), 0);
            assertEquals(simulator.getRegisters().getRegister(Registers.T9).getLong(), 0);
        } catch (Exception e) {
            fail();
        }
    }

    @Test
    void testOr() {
        try {
            simulator.getRegisters().getRegister(Registers.S0).setLong(0);
            simulator.getRegisters().getRegister(Registers.S1).setLong(-1);
            simulator.getRegisters().getRegister(Registers.S2).setLong(3);
            simulator.getRegisters().getRegister(Registers.S3).setLong(-5);
            simulator.getRegisters().getRegister(Registers.S4).setLong(102399850);
            simulator.getRegisters().getRegister(Registers.S5).setLong(-810238219);
            simulator.getRegisters().getRegister(Registers.S6).setLong(Long.MAX_VALUE);
            simulator.getRegisters().getRegister(Registers.S7).setLong(Long.MIN_VALUE);

            simulator.runLine(Lexer.parseLine("or $t0 $s0 $s0", 0));
            simulator.runLine(Lexer.parseLine("or $t1 $s1 $s1", 1));
            simulator.runLine(Lexer.parseLine("or $t2 $s0 $s1", 2));
            simulator.runLine(Lexer.parseLine("or $t3 $s1 $s0", 3));
            simulator.runLine(Lexer.parseLine("or $t4 $s2 $s3", 4));
            simulator.runLine(Lexer.parseLine("or $t5 $s3 $s2", 5));
            simulator.runLine(Lexer.parseLine("or $t6 $s4 $s5", 6));
            simulator.runLine(Lexer.parseLine("or $t7 $s5 $s4", 7));
            simulator.runLine(Lexer.parseLine("or $t8 $s6 $s7", 8));
            simulator.runLine(Lexer.parseLine("or $t9 $s7 $s6", 9));

            assertEquals(simulator.getRegisters().getRegister(Registers.T0).getLong(), 0);
            assertEquals(simulator.getRegisters().getRegister(Registers.T1).getLong(), -1);
            assertEquals(simulator.getRegisters().getRegister(Registers.T2).getLong(), -1);
            assertEquals(simulator.getRegisters().getRegister(Registers.T3).getLong(), -1);
            assertEquals(simulator.getRegisters().getRegister(Registers.T4).getLong(), -5 | 3);
            assertEquals(simulator.getRegisters().getRegister(Registers.T5).getLong(), 3 | -5);
            assertEquals(simulator.getRegisters().getRegister(Registers.T6).getLong(), -810238219 | 102399850);
            assertEquals(simulator.getRegisters().getRegister(Registers.T7).getLong(), 102399850 | -810238219);
            assertEquals(simulator.getRegisters().getRegister(Registers.T8).getLong(), -1);
            assertEquals(simulator.getRegisters().getRegister(Registers.T9).getLong(), -1);
        } catch (Exception e) {
            fail();
        }
    }

    @Test
    void testXor() {
        try {
            simulator.getRegisters().getRegister(Registers.S0).setLong(0);
            simulator.getRegisters().getRegister(Registers.S1).setLong(-1);
            simulator.getRegisters().getRegister(Registers.S2).setLong(3);
            simulator.getRegisters().getRegister(Registers.S3).setLong(-5);
            simulator.getRegisters().getRegister(Registers.S4).setLong(238782348);
            simulator.getRegisters().getRegister(Registers.S5).setLong(-506492003);
            simulator.getRegisters().getRegister(Registers.S6).setLong(Long.MAX_VALUE);
            simulator.getRegisters().getRegister(Registers.S7).setLong(Long.MIN_VALUE);

            simulator.runLine(Lexer.parseLine("xor $t0 $s0 $s0", 0));
            simulator.runLine(Lexer.parseLine("xor $t1 $s1 $s1", 1));
            simulator.runLine(Lexer.parseLine("xor $t2 $s0 $s1", 2));
            simulator.runLine(Lexer.parseLine("xor $t3 $s1 $s0", 3));
            simulator.runLine(Lexer.parseLine("xor $t4 $s2 $s3", 4));
            simulator.runLine(Lexer.parseLine("xor $t5 $s3 $s2", 5));
            simulator.runLine(Lexer.parseLine("xor $t6 $s4 $s5", 6));
            simulator.runLine(Lexer.parseLine("xor $t7 $s5 $s4", 7));
            simulator.runLine(Lexer.parseLine("xor $t8 $s6 $s7", 8));
            simulator.runLine(Lexer.parseLine("xor $t9 $s7 $s6", 9));

            assertEquals(simulator.getRegisters().getRegister(Registers.T0).getLong(), 0);
            assertEquals(simulator.getRegisters().getRegister(Registers.T1).getLong(), 0);
            assertEquals(simulator.getRegisters().getRegister(Registers.T2).getLong(), -1);
            assertEquals(simulator.getRegisters().getRegister(Registers.T3).getLong(), -1);
            assertEquals(simulator.getRegisters().getRegister(Registers.T4).getLong(), -5 ^ 3);
            assertEquals(simulator.getRegisters().getRegister(Registers.T5).getLong(), 3 ^ -5);
            assertEquals(simulator.getRegisters().getRegister(Registers.T6).getLong(), -506492003 ^ 238782348);
            assertEquals(simulator.getRegisters().getRegister(Registers.T7).getLong(), 238782348 ^ -506492003);
            assertEquals(simulator.getRegisters().getRegister(Registers.T8).getLong(), -1);
            assertEquals(simulator.getRegisters().getRegister(Registers.T9).getLong(), -1);
        } catch (Exception e) {
            fail();
        }
    }

    void testNot() {
        try {

        } catch (Exception e) {
            fail();
        }
    }

    void testSll() {
        try {

        } catch (Exception e) {
            fail();
        }
    }

    void testSrl() {
        try {

        } catch (Exception e) {
            fail();
        }
    }

    void testDec() {
        try {

        } catch (Exception e) {
            fail();
        }
    }

    void testInc() {
        try {

        } catch (Exception e) {
            fail();
        }
    }
}
