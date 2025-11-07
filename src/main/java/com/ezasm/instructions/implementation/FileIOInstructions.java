package com.ezasm.instructions.implementation;

import com.ezasm.instructions.Instruction;
import com.ezasm.instructions.targets.input.IAbstractInput;
import com.ezasm.instructions.targets.inputoutput.RegisterInputOutput;
import com.ezasm.simulation.Simulator;
import com.ezasm.simulation.Registers;
import com.ezasm.simulation.exception.SimulationException;
import com.ezasm.simulation.transform.Transformation;
import com.ezasm.simulation.transform.TransformationSequence;
import com.ezasm.simulation.transform.transformable.InputOutputTransformable;
import com.ezasm.simulation.Memory;
import com.ezasm.util.RawData;

// no direct File import needed; simulator handles file resolution
/**
 * Implements file descriptor based instructions such as open and close.
 */
public class FileIOInstructions {
    private final Simulator simulator;

    /**
     * Some instructions require access to the Simulator directly, so that is provided.
     *
     * @param simulator the provided Simulator.
     */
    public FileIOInstructions(Simulator simulator) {
        this.simulator = simulator;
    }

    /**
     * Open a file. The input is a pointer to a null-terminated string in memory (address). The FD register will be set
     * to the returned file descriptor on success.
     *
     * @param input pointer to file path string in memory
     * @return a transformation sequence which sets the FD register to the allocated descriptor
     * @throws SimulationException on memory or IO error
     */
    @Instruction
    public TransformationSequence open(IAbstractInput input) throws SimulationException {
        int address = (int) input.get(simulator).intValue();
        StringBuilder sb = new StringBuilder();
        int index = 0;
        long current = simulator.getMemory().read(address).intValue();
        while (current != 0) {
            sb.append((char) current);
            index++;
            current = simulator.getMemory().read(address + index * Memory.getWordSize()).intValue();
        }

        // Resolve path relative to execution directory is handled by Simulator.openFile
        int fd = simulator.openFile(sb.toString());

        InputOutputTransformable fdio = new InputOutputTransformable(simulator, new RegisterInputOutput(Registers.FD));
        Transformation t = fdio.transformation(new RawData(fd));
        return new TransformationSequence(t);
    }

    /**
     * Close the file descriptor currently stored in the FD register. On success sets FD to 0.
     *
     * @return a transformation sequence which resets FD to 0.
     * @throws SimulationException on invalid fd or IO error
     */
    @Instruction
    public TransformationSequence close() throws SimulationException {
        int fd = (int) simulator.getRegisters().getRegister(Registers.FD).getLong();
        simulator.closeFile(fd);
        InputOutputTransformable fdio = new InputOutputTransformable(simulator, new RegisterInputOutput(Registers.FD));
        Transformation t = fdio.transformation(new RawData(0));
        return new TransformationSequence(t);
    }

    /**
     * Write a null-terminated string from memory into a new file. The instruction takes two pointers: the address of
     * the filename string and the address of the data string. The file will be created/overwritten with the data.
     *
     * @param filenamePtr pointer to null-terminated filename string in memory
     * @param dataPtr     pointer to null-terminated data string in memory
     * @return empty transformation sequence (side-effect performed immediately)
     * @throws SimulationException on memory or IO error
     */
    @Instruction
    public TransformationSequence write(IAbstractInput filenamePtr, IAbstractInput dataPtr) throws SimulationException {
        int fnameAddr = (int) filenamePtr.get(simulator).intValue();
        StringBuilder fname = new StringBuilder();
        int idx = 0;
        long cur = simulator.getMemory().read(fnameAddr).intValue();
        while (cur != 0) {
            fname.append((char) cur);
            idx++;
            cur = simulator.getMemory().read(fnameAddr + idx * Memory.getWordSize()).intValue();
        }

        int dataAddr = (int) dataPtr.get(simulator).intValue();
        StringBuilder data = new StringBuilder();
        idx = 0;
        cur = simulator.getMemory().read(dataAddr).intValue();
        while (cur != 0) {
            data.append((char) cur);
            idx++;
            cur = simulator.getMemory().read(dataAddr + idx * Memory.getWordSize()).intValue();
        }

        simulator.writeFile(fname.toString(), data.toString());
        return new TransformationSequence();
    }

}
