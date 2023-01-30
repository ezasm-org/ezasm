package com.ezasm.parsing;

import com.ezasm.instructions.InstructionDispatcher;
import com.ezasm.simulation.Registers;
import org.junit.jupiter.api.Test;

import java.util.HashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

class LexerTest {

    @Test
    void isComment() {
        assertTrue(Lexer.isComment("# this is a comment"));
        assertTrue(Lexer.isComment("# this is a comment\t"));
        assertTrue(Lexer.isComment("# this is a comment \t\t"));
        assertTrue(Lexer.isComment("#$s0 this is a comment:"));
        assertFalse(Lexer.isComment("add $s0 $s1 1 # this is a comment"));
        assertFalse(Lexer.isComment(""));
    }

    @Test
    void isLabel() {
        assertTrue(Lexer.isLabel("label:"));
        assertTrue(Lexer.isLabel("_label-:"));
        assertTrue(Lexer.isLabel("123label:"));
        assertFalse(Lexer.isLabel("my label:"));
        assertFalse(Lexer.isLabel(""));
    }

    @Test
    void isRegister() {
        assertTrue(Lexer.isRegister("$0"));
        assertTrue(Lexer.isRegister("$" + Registers.ZERO));
        assertFalse(Lexer.isRegister("#$0"));
        assertFalse(Lexer.isRegister("$ABC"));
        assertFalse(Lexer.isRegister(""));
    }

    @Test
    void isDereference() {
        // Not yet properly implemented
    }

    @Test
    void isImmediate() {
        assertTrue(Lexer.isImmediate("123"));
        assertTrue(Lexer.isImmediate("-123"));
        assertTrue(Lexer.isImmediate("1234567890123456789"));
        // Future functionality of issue #29
        // assertTrue(Lexer.isImmediate("0x1000"));
        // assertTrue(Lexer.isImmediate("0xABCDEF"));
        // assertTrue(Lexer.isImmediate("0b100101"));
        assertFalse(Lexer.isImmediate("123.456"));
        assertFalse(Lexer.isImmediate("ABC"));
        assertFalse(Lexer.isImmediate(""));
    }

    @Test
    void isInstruction() {
        for(String instruction : InstructionDispatcher.getInstructions().keySet()) {
            assertTrue(Lexer.isInstruction(instruction));
        }
        assertFalse(Lexer.isInstruction("add0"));
        assertFalse(Lexer.isInstruction("#add"));
        assertFalse(Lexer.isInstruction(""));
    }

    private static final Map<String, Integer> map = new HashMap<>();
    private static boolean testLineAgainstString(Line line, String string) {
        try {
            Line newline = Lexer.parseLine(string, map, 0);
            if (newline == null) {
                return false;
            }
            return line.toString().equals(newline.toString());
        } catch (ParseException e) {
            return false;
        }
    }

    @Test
    void parseLine() {
        try {
            Line line = new Line("add", "$s0", new String[]{"$t0", "1"});
            assertTrue(testLineAgainstString(line, "add $s0 $t0 1"));
            assertTrue(testLineAgainstString(line, "add $s0,$t0,1"));
            assertTrue(testLineAgainstString(line, " \t\t  add $s0 $t0 1   "));
            assertTrue(testLineAgainstString(line, "add  $s0  \t $t0   1 "));
            assertTrue(testLineAgainstString(line, "add $s0 $t0 1;"));
            assertTrue(testLineAgainstString(line, "add $s0,$t0,1;"));
            assertTrue(testLineAgainstString(line, " \t\t  add $s0 $t0 1   ;"));
            assertTrue(testLineAgainstString(line, "add  $s0  \t $t0   1 ;"));

            assertFalse(testLineAgainstString(line, "add $s0 $t1 1"));
            assertFalse(testLineAgainstString(line, "add $s0 $t0 2"));
            assertFalse(testLineAgainstString(line, ""));

            assertFalse(testLineAgainstString(line, ""));
        } catch (ParseException ignored) {}
    }

    @Test
    void parseLineException() {
        assertThrows(ParseException.class, () -> {
            Lexer.parseLine("add $s0 $s1 $abc", map, 0);
        });
        assertThrows(ParseException.class, () -> {
            Lexer.parseLine("add $s0 $s1 ABC", map, 0);
        });
        /* Will not work until issue #30 is resolved
        assertThrows(ParseException.class, () -> {
            Lexer.parseLine("add $s0 $s1", map, 0);
        });*/
        assertThrows(ParseException.class, () -> {
            Lexer.parseLine("add", map, 0);
        });
        assertThrows(ParseException.class, () -> {
            Lexer.parseLine("$s0", map, 0);
        });
        assertThrows(ParseException.class, () -> {
            Lexer.parseLine("$s0", map, 0);
        });

    }

    @Test
    void parseLines() {
        // TODO
    }
}