package com.ezasm.instructions.implementation;

import static org.junit.jupiter.api.Assertions.assertEquals;
import org.junit.jupiter.api.Test;

import com.ezasm.instructions.targets.input.IAbstractInput;
import com.ezasm.instructions.targets.input.ImmediateInput;
import com.ezasm.instructions.targets.inputoutput.DereferenceInputOutput;
import com.ezasm.instructions.targets.inputoutput.IAbstractInputOutput;
import com.ezasm.instructions.targets.inputoutput.RegisterInputOutput;
import com.ezasm.simulation.Registers;
import com.ezasm.simulation.Simulator;
import com.ezasm.simulation.exception.SimulationException;
import com.ezasm.util.RawData;

public class MemoryInstructionsTest {
    @Test
    public void TestMallocAndFreeOne() throws SimulationException {
        Simulator sim = new Simulator(8, 16);
        MemoryInstructions memoryInstructions = new MemoryInstructions(sim);
        IAbstractInputOutput register = new RegisterInputOutput("t0");
        IAbstractInput immediateTwo = new ImmediateInput(new RawData(2));
        IAbstractInput immediateFour = new ImmediateInput(new RawData(4));

        long before = sim.getRegisters().getRegister("t0").getLong();
        memoryInstructions.malloc(register, immediateFour).apply();
        memoryInstructions.free(register);
        memoryInstructions.malloc(register, immediateTwo).apply();
        long after = sim.getRegisters().getRegister("t0").getLong();
        assertEquals(before + sim.getMemory().initialHeapPointer(), after);
    }

    @Test
    public void TestMallocAndFreeTwo() throws SimulationException {
        Simulator sim = new Simulator(8, 16);
        MemoryInstructions memoryInstructions = new MemoryInstructions(sim);
        IAbstractInputOutput register = new RegisterInputOutput("t0");
        IAbstractInput immediateTwo = new ImmediateInput(new RawData(2));
        IAbstractInput immediateFour = new ImmediateInput(new RawData(4));

        long before = sim.getRegisters().getRegister("t0").getLong();
        memoryInstructions.malloc(register, immediateTwo).apply();
        memoryInstructions.free(register);
        memoryInstructions.malloc(register, immediateFour).apply();
        long after = sim.getRegisters().getRegister("t0").getLong();
        System.out.println(before + sim.getMemory().initialHeapPointer());
        System.out.println(after);
        assertEquals(before + sim.getMemory().initialHeapPointer(), after);
    }

    @Test
    public void TestMallocAndFreeThree() throws SimulationException {
        Simulator sim = new Simulator(8, 16);
        MemoryInstructions memoryInstructions = new MemoryInstructions(sim);
        IAbstractInputOutput register = new RegisterInputOutput("t0");
        IAbstractInput immediateTwo = new ImmediateInput(new RawData(2));
        IAbstractInput immediateFour = new ImmediateInput(new RawData(4));

        long before = sim.getRegisters().getRegister("t0").getLong();
        memoryInstructions.malloc(register, immediateFour).apply();
        memoryInstructions.free(register);
        memoryInstructions.malloc(register, immediateTwo).apply();
        long after = sim.getRegisters().getRegister("t0").getLong();
        assertEquals(before + sim.getMemory().initialHeapPointer(), after);

        memoryInstructions.free(register);
        long afterSecond = sim.getRegisters().getRegister("t0").getLong();
        assertEquals(before + sim.getMemory().initialHeapPointer(), afterSecond);

        memoryInstructions.malloc(register, immediateFour).apply();
        long afterThird = sim.getRegisters().getRegister("t0").getLong();
        assertEquals(afterSecond, afterThird);

        memoryInstructions.malloc(register, immediateFour).apply();
        long afterFourth = sim.getRegisters().getRegister("t0").getLong();
        assertEquals(afterThird + 4, afterFourth);

        memoryInstructions.free(register);
        long afterFifth = sim.getRegisters().getRegister("t0").getLong();
        assertEquals(afterFourth, afterFifth);
    }

    @Test
    public void TestFreeInstruction() throws SimulationException {
        Simulator sim = new Simulator(8, 16);
        MemoryInstructions memoryInstructions = new MemoryInstructions(sim);
        IAbstractInputOutput register = new RegisterInputOutput("t0");
        IAbstractInput immediateFour = new ImmediateInput(new RawData(4));

        long listBefore = sim.getMemory().getFreeList().size();
        long mapBefore = sim.getMemory().getAllocations().size();
        memoryInstructions.malloc(register, immediateFour).apply();
        long listDuring = sim.getMemory().getFreeList().size();
        long mapDuring = sim.getMemory().getAllocations().size();
        memoryInstructions.free(register).apply();
        long listAfter = sim.getMemory().getFreeList().size();
        long mapAfter = sim.getMemory().getAllocations().size();

        // list tests
        assertEquals(0, listBefore);
        assertEquals(0, listDuring);
        assertEquals(1, listAfter);

        // map tests
        assertEquals(0, mapBefore);
        assertEquals(1, mapDuring);
        assertEquals(0, mapAfter);
    }

    @Test
    public void TestMallocInstruction() throws SimulationException {
        Simulator sim = new Simulator(8, 16);
        MemoryInstructions memoryInstructions = new MemoryInstructions(sim);
        IAbstractInputOutput register = new RegisterInputOutput("t0");
        IAbstractInput immediateFour = new ImmediateInput(new RawData(4));

        long before = sim.getRegisters().getRegister("t0").getLong();
        memoryInstructions.malloc(register, immediateFour).apply();
        long after = sim.getRegisters().getRegister("t0").getLong();
        assertEquals(before + sim.getMemory().initialHeapPointer(), after);
        assertEquals(1, sim.getMemory().getAllocations().size());

        memoryInstructions.malloc(register, immediateFour).apply();
        long afterSecond = sim.getRegisters().getRegister("t0").getLong();
        assertEquals(after + 4, afterSecond);
    }

    @Test
    public void TestAllocInstruction() throws SimulationException {
        Simulator sim = new Simulator(8, 16);
        MemoryInstructions memoryInstructions = new MemoryInstructions(sim);
        IAbstractInputOutput register = new RegisterInputOutput("t0");
        IAbstractInput immediateTwo = new ImmediateInput(new RawData(2));

        long bytesBefore = sim.getRegisters().getRegister("t0").getLong();
        memoryInstructions.alloc(register, immediateTwo).apply();
        long bytesAfter = sim.getRegisters().getRegister("t0").getLong();
        assertEquals(bytesBefore + sim.getMemory().initialHeapPointer(), bytesAfter);

        memoryInstructions.alloc(register, immediateTwo).apply();
        long bytesAfterSecondCall = sim.getRegisters().getRegister("t0").getLong();
        assertEquals(bytesAfter + 2, bytesAfterSecondCall);

    }

    @Test
    public void TestStoreInstruction() throws SimulationException {
        Simulator sim = new Simulator(8, 16);

        MemoryInstructions memoryInstructions = new MemoryInstructions(sim);

        IAbstractInputOutput register = new RegisterInputOutput(Registers.T0);

        IAbstractInput aiimmediateTwo = new ImmediateInput(new RawData(2));

        memoryInstructions.alloc(register, aiimmediateTwo).apply();
        DereferenceInputOutput d = new DereferenceInputOutput(Registers.T0, 0);
        memoryInstructions.store(aiimmediateTwo, d).apply();

        assertEquals(2, sim.getMemory().read((int) register.get(sim).intValue(), 8).intValue());

    }

    @Test
    public void TestLoadInstruction() throws SimulationException {
        Simulator sim = new Simulator(8, 16);

        MemoryInstructions mi = new MemoryInstructions(sim);

        IAbstractInputOutput t0Register = new RegisterInputOutput(Registers.T0);

        IAbstractInput immediateTwo = new ImmediateInput(new RawData(2));

        IAbstractInputOutput loadOut = new RegisterInputOutput(Registers.T1);

        mi.alloc(t0Register, immediateTwo).apply();
        DereferenceInputOutput d = new DereferenceInputOutput(Registers.T0, 0);
        mi.store(immediateTwo, d).apply();
        mi.load(loadOut, d).apply();

        assertEquals(2, loadOut.get(sim).intValue());
    }

}
