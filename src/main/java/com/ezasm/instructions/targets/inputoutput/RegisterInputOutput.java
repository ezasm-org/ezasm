package com.ezasm.instructions.targets.inputoutput;

import com.ezasm.simulation.Simulator;
import com.ezasm.simulation.Registers;
import com.ezasm.simulation.exception.MisalignedStackPointerException;
import com.ezasm.simulation.exception.SimulationStackOverflowException;
import com.ezasm.util.RawData;

import java.util.Objects;

/**
 * The implementation of a register to be used as either an input or an output.
 */
public class RegisterInputOutput implements IAbstractInputOutput {

    private final int register;

    /**
     * Construct the output based on the register reference number.
     *
     * @param register the register reference number.
     */
    public RegisterInputOutput(int register) {
        this.register = register;
    }

    /**
     * Construct the output based on the register reference string.
     *
     * @param register the register reference string.
     */
    public RegisterInputOutput(String register) {
        this.register = Registers.getRegisterNumber(register);
    }

    /**
     * Gets the value stored within the register.
     *
     * @param simulator the program simulator.
     * @return the value stored within the register.
     */
    @Override
    public RawData get(Simulator simulator) {
        RawData val = simulator.getRegisters().getRegister(register).getData();
        return val.copy();
    }

    /**
     * Checks if the new stack pointer value is valid.
     *
     * @param simulator the program simulator
     * @param value the new stack pointer value
     * @throws MisalignedStackPointerException if the new stack pointer is not aligned on a word boundary
     * @throws SimulationStackOverflowException if the new stack pointer collides with the heap
     */
    public void validateStackPointer(Simulator simulator, RawData value) throws MisalignedStackPointerException, SimulationStackOverflowException {
        if (value.intValue() % simulator.getMemory().wordSize != 0) {
            throw new MisalignedStackPointerException(value.intValue());
        } else if (value.intValue() <= simulator.getMemory().currentHeapPointer()) {
            throw new SimulationStackOverflowException(value.intValue());
        }
    }

    /**
     * Sets the value stored within the register.
     *
     * @param simulator the program simulator.
     * @param value     the value to set.
     */
    @Override
    public void set(Simulator simulator, RawData value) throws MisalignedStackPointerException, SimulationStackOverflowException {
        if (register == Registers.getRegisterNumber(Registers.SP)) {
            validateStackPointer(simulator, value);
        }

        simulator.getRegisters().getRegister(register).setDataWithGuiCallback(value);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o)
            return true;
        if (o == null || getClass() != o.getClass())
            return false;
        RegisterInputOutput that = (RegisterInputOutput) o;
        return register == that.register;
    }

    @Override
    public int hashCode() {
        return Objects.hash(register);
    }
}
