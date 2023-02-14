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
        MemoryInstructions mi = new MemoryInstructions(sim);
        IAbstractOutput ao = new RegisterInputOutput("t0");
        IAbstractInput ai = new ImmediateInput(Conversion.longToBytes(2));

        long bytesBefore = Conversion.bytesToLong(sim.getRegisters().getRegister("t0").getBytes());
        mi.alloc(ao, ai);
        long bytesAfter = Conversion.bytesToLong(sim.getRegisters().getRegister("t0").getBytes());
        assertEquals(bytesBefore + 65536, bytesAfter);

        mi.alloc(ao, ai);
        long bytesAfterSecondCall = Conversion.bytesToLong(sim.getRegisters().getRegister("t0").getBytes());
        assertEquals(bytesAfter + 2, bytesAfterSecondCall);

    }

    @Test
    public void TestStoreInstruction() throws SimulationException {
        Simulator sim = new Simulator(8, 16);

        MemoryInstructions mi = new MemoryInstructions(sim);

        IAbstractOutput ao = new RegisterInputOutput("t0");

        IAbstractInput ai = new ImmediateInput(Conversion.longToBytes(2));
        IAbstractInputOutput ai2 = new RegisterInputOutput("t0");

        mi.alloc(ao, ai);
        mi.store(ai, ai2);

        assertEquals(2, Conversion.bytesToLong(sim.getMemory().read((int) Conversion.bytesToLong(ai2.get(sim)), 16)));

    }

    @Test
    public void TestLoadInstruction() throws SimulationException {
        Simulator sim = new Simulator(8, 16);

        MemoryInstructions mi = new MemoryInstructions(sim);

        IAbstractOutput ao = new RegisterInputOutput("t0");

        IAbstractInput ai = new ImmediateInput(Conversion.longToBytes(2));
        IAbstractInputOutput ai2 = new RegisterInputOutput("t0");

        IAbstractOutput loadOut = new RegisterInputOutput("t1");

        mi.alloc(ao, ai);
        mi.store(ai, ai2);
        mi.load(loadOut, ai2);

        assertEquals(2, Conversion.bytesToLong(sim.getRegisters().getRegister("t1").getBytes()));
    }

}
