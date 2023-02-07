package com.ezasm.instructions.impl;


import com.ezasm.Conversion;
import com.ezasm.instructions.targets.input.IAbstractInput;
import com.ezasm.instructions.targets.output.IAbstractOutput;
import com.ezasm.simulation.Memory;
import com.ezasm.simulation.Simulator;
import com.ezasm.instructions.Instruction;

public class MemoryInstructions {

    private final Simulator simulator;
    public MemoryInstructions(Simulator sim){
        this.simulator = sim;
    }

    @Instruction
    public void load(IAbstractOutput out, IAbstractInput inp){
        Memory m = this.simulator.getMemory();
        byte[] word = m.read((int) Conversion.bytesToLong(inp.get(simulator)), m.WORD_SIZE);
        out.set(this.simulator, word);
    }

    @Instruction
    public void store(IAbstractInput inp1, IAbstractInput inp2){
        Memory m = this.simulator.getMemory();
        m.write( (int) Conversion.bytesToLong(inp2.get(simulator)), inp1.get(simulator) );
    }

    @Instruction
    public void alloc(IAbstractOutput out, IAbstractInput inp){
        Memory m = this.simulator.getMemory();
        int bytesWritten = m.allocate((int) Conversion.bytesToLong(inp.get(simulator)));
        out.set(this.simulator, Conversion.longToBytes( bytesWritten ));
    }

}
