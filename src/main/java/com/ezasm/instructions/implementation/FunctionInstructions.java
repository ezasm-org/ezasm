package com.ezasm.instructions.implementation;

import com.ezasm.instructions.Instruction;
import com.ezasm.instructions.targets.input.IAbstractInput;
import com.ezasm.instructions.targets.inputoutput.RegisterInputOutput;
import com.ezasm.simulation.ISimulator;
import com.ezasm.simulation.Registers;
import com.ezasm.simulation.exception.SimulationException;

/**
 * An implementation of standard function call instructions for the simulation.
 */
public class FunctionInstructions {

    private final ISimulator simulator;
    private final MemoryInstructions memoryInstructions;

    /**
     * Some instructions require access to the Simulator directly, so that is provided.
     *
     * @param simulator the provided Simulator.
     */
    public FunctionInstructions(ISimulator simulator) {
        this.simulator = simulator;
        this.memoryInstructions = new MemoryInstructions(simulator);
    }

    /**
     * The standard jump operation: sets the PC to the given line number.
     *
     * @param input the line to jump to.
     * @throws SimulationException if there is an error in accessing the simulation.
     */
    @Instruction
    public void jump(IAbstractInput input) throws SimulationException {
        simulator.getRegisters().getRegister(Registers.PC).setBytes(input.get(simulator));
    }

    /**
     * The standard jump operation: sets the PC to the given line number.
     *
     * @param input the line to jump to.
     * @throws SimulationException if there is an error in accessing the simulation.
     */
    @Instruction
    public void j(IAbstractInput input) throws SimulationException {
        jump(input);
    }

    /**
     * The jump and link operation: sets the PC to the given line number and stores the return address. Stores the
     * previous return address onto the stack.
     *
     * @param input the line to jump to.
     * @throws SimulationException if there is an error in accessing the simulation.
     */
    @Instruction
    public void call(IAbstractInput input) throws SimulationException {
        memoryInstructions.push(new RegisterInputOutput(Registers.RA));
        simulator.getRegisters().getRegister(Registers.RA)
                .setBytes(simulator.getRegisters().getRegister(Registers.PC).getBytes());
        jump(input);
    }

    /**
     * The jump and link operation: sets the PC to the given line number and stores the return address. Stores the
     * previous return address onto the stack.
     *
     * @param input the line to jump to.
     * @throws SimulationException if there is an error in accessing the simulation.
     */
    @Instruction
    public void jal(IAbstractInput input) throws SimulationException {
        call(input);
    }

    /**
     * Return to the current return address and then pops the next return address off of the stack.
     *
     * @throws SimulationException if there is an error in accessing the simulation.
     */
    @Instruction
    public void _return() throws SimulationException {
        jump(new RegisterInputOutput(Registers.RA));
        memoryInstructions.pop(new RegisterInputOutput(Registers.RA));
    }

}
