package com.ezasm.instruction.memory;

import com.ezasm.instructions.targets.inputoutput.DereferenceInputOutput;
import com.ezasm.simulation.Registers;
import com.ezasm.simulation.exception.SimulationException;
import com.ezasm.util.Conversion;
import com.ezasm.instructions.implementation.MemoryInstructions;
import com.ezasm.instructions.targets.input.IAbstractInput;
import com.ezasm.instructions.targets.input.ImmediateInput;
import com.ezasm.instructions.targets.inputoutput.IAbstractInputOutput;
import com.ezasm.instructions.targets.inputoutput.RegisterInputOutput;
import com.ezasm.simulation.Simulator;
import com.ezasm.util.RawData;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class TestMemoryInstructions {

    @Test
    public void TestAllocInstruction() throws SimulationException {
        Simulator sim = new Simulator(8, 16);
        MemoryInstructions memoryInstructions = new MemoryInstructions(sim);
        IAbstractInputOutput register = new RegisterInputOutput("t0");
        IAbstractInput immediateTwo = new ImmediateInput(new RawData(2));

        long bytesBefore = sim.getRegisters().getRegister("t0").getLong();
        memoryInstructions.alloc(register, immediateTwo).apply();
        long bytesAfter = sim.getRegisters().getRegister("t0").getLong();
        assertEquals(bytesBefore + 65536, bytesAfter);

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

        assertEquals(2, sim.getMemory().read((int) register.get(sim).intValue(), 16).intValue());

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
