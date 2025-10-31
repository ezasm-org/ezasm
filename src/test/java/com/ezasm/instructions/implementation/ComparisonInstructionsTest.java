package com.ezasm.instructions.implementation;

import org.junit.jupiter.api.Test;

import com.ezasm.simulation.Memory;
import com.ezasm.simulation.Simulator;
import com.ezasm.simulation.exception.SimulationException;

public class ComparisonInstructionsTest {

    private final Simulator simulator = new Simulator(Memory.DEFAULT_WORD_SIZE, Memory.DEFAULT_MEMORY_WORDS);

    @Test
    void testLessThan() throws SimulationException{
    }  

    @Test
    void testLessThanEqual() throws SimulationException {
    
    }

    @Test
    void testGreaterThan() throws SimulationException{
    }

    @Test
    void testGreaterThanEqual() throws SimulationException{
    }

    @Test
    void testEqual() throws SimulationException {
    }

    @Test
    void testNotEqual() throws SimulationException{
    }
}