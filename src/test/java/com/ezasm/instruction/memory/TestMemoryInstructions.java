package com.ezasm.instruction.memory;

import com.ezasm.simulation.exception.SimulationException;
import com.ezasm.util.Conversion;
import com.ezasm.instructions.implementation.MemoryInstructions;
import com.ezasm.instructions.targets.input.IAbstractInput;
import com.ezasm.instructions.targets.input.ImmediateInput;
import com.ezasm.instructions.targets.inputoutput.IAbstractInputOutput;
import com.ezasm.instructions.targets.inputoutput.RegisterInputOutput;
import com.ezasm.instructions.targets.output.IAbstractOutput;
import com.ezasm.simulation.Simulator;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class TestMemoryInstructions {

    @Test
    public void TestAllocInstruction() throws SimulationException {
        Simulator sim = new Simulator(8, 16);
        MemoryInstructions memoryInstructions = new MemoryInstructions(sim);
        IAbstractOutput register = new RegisterInputOutput("t0");
        IAbstractInput immediateTwo = new ImmediateInput(Conversion.longToBytes(2));

        long bytesBefore = Conversion.bytesToLong(sim.getRegisters().getRegister("t0").getBytes());
        memoryInstructions.alloc(register, immediateTwo);
        long bytesAfter = Conversion.bytesToLong(sim.getRegisters().getRegister("t0").getBytes());
        assertEquals(bytesBefore + 65536, bytesAfter);

        memoryInstructions.alloc(register, immediateTwo);
        long bytesAfterSecondCall = Conversion.bytesToLong(sim.getRegisters().getRegister("t0").getBytes());
        assertEquals(bytesAfter + 2, bytesAfterSecondCall);

    }

    @Test
    public void TestStoreInstruction() throws SimulationException {
        Simulator sim = new Simulator(8, 16);

        MemoryInstructions memoryInstructions = new MemoryInstructions(sim);

        IAbstractInputOutput register = new RegisterInputOutput("t0");

        IAbstractInput aiimmediateTwo = new ImmediateInput(Conversion.longToBytes(2));

        memoryInstructions.alloc(register, aiimmediateTwo);
        memoryInstructions.store(aiimmediateTwo, register);

        assertEquals(2,
                Conversion.bytesToLong(sim.getMemory().read((int) Conversion.bytesToLong(register.get(sim)), 16)));

    }

    @Test
    public void TestLoadInstruction() throws SimulationException {
        Simulator sim = new Simulator(8, 16);

        MemoryInstructions mi = new MemoryInstructions(sim);

        IAbstractInputOutput t0Register = new RegisterInputOutput("t0");

        IAbstractInput immediateTwo = new ImmediateInput(Conversion.longToBytes(2));

        IAbstractInputOutput loadOut = new RegisterInputOutput("t1");

        mi.alloc(t0Register, immediateTwo);
        mi.store(immediateTwo, t0Register);
        mi.load(loadOut, t0Register);

        assertEquals(2, Conversion.bytesToLong(loadOut.get(sim)));
    }

}
