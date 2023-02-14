
package com.ezasm.instructions.implementation;

import com.ezasm.instructions.Instruction;
import com.ezasm.instructions.targets.input.IAbstractInput;
import com.ezasm.instructions.targets.output.IAbstractOutput;
import com.ezasm.simulation.ISimulator;
import com.ezasm.simulation.Registers;
import com.ezasm.simulation.exception.SimulationException;
import com.ezasm.util.Conversion;
import com.ezasm.simulation.Memory;


/**
 * An implementation of memory manipulation instructions for the simulation.
 */
public class MemoryInstructions {

    private final ISimulator simulator;

    /**
     * Some instructions require access to the Simulator directly, so that is provided.
     *
     * @param simulator the provided Simulator.
     */
    public MemoryInstructions(ISimulator simulator) {
        this.simulator = simulator;
    }

    /**
     * Pushes a value to the stack, moving the stack pointer to allow for room on the stack to store it.
     *
     * @param input the value to be pushed onto the stack.
     * @throws SimulationException if there is an error in accessing the simulation.
     */
    @Instruction
    public void push(IAbstractInput input) throws SimulationException {
        long sp = simulator.getRegisters().getRegister(Registers.SP).getLong() - simulator.getMemory().WORD_SIZE;
        simulator.getRegisters().getRegister(Registers.SP).setLong(sp);
        simulator.getMemory().write((int) sp, input.get(simulator));
    }

    /**
     * Pops a value from the stack, moving the stack pointer to deallocate it.
     *
     * @param output the place to store the obtained value.
     * @throws SimulationException if there is an error in accessing the simulation.
     */
    @Instruction
    public void pop(IAbstractOutput output) throws SimulationException {
        long sp = simulator.getRegisters().getRegister(Registers.SP).getLong();
        output.set(simulator, simulator.getMemory().read((int) sp, simulator.getMemory().WORD_SIZE));
        simulator.getRegisters().getRegister(Registers.SP).setLong(sp + simulator.getMemory().WORD_SIZE);
    }

    @Instruction
    public void load(IAbstractOutput out, IAbstractInput inp) throws SimulationException{
        Memory m = this.simulator.getMemory();
        byte[] word = m.read((int) Conversion.bytesToLong(inp.get(simulator)), m.WORD_SIZE);
        out.set(this.simulator, word);
    }

    @Instruction
    public void store(IAbstractInput inp1, IAbstractInput inp2) throws SimulationException{
        Memory m = this.simulator.getMemory();
        m.write( (int) Conversion.bytesToLong(inp2.get(simulator)), inp1.get(simulator) );
    }

    @Instruction

    public void alloc(IAbstractOutput out, IAbstractInput inp) throws SimulationException{
        Memory m = this.simulator.getMemory();
        int bytesWritten = m.unsafeAllocate((int) Conversion.bytesToLong(inp.get(simulator)));
        out.set(this.simulator, Conversion.longToBytes( bytesWritten ));
    }

}
