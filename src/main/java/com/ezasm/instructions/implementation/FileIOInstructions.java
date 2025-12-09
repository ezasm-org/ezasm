package com.ezasm.instructions.implementation;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang3.tuple.Pair;

import com.ezasm.instructions.Instruction;
import com.ezasm.instructions.targets.input.IAbstractInput;
import com.ezasm.instructions.targets.inputoutput.RegisterInputOutput;
import com.ezasm.instructions.targets.inputoutput.DereferenceInputOutput;
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
 * Implements file descriptor based instructions such as open, close, exec, readfd, and writefd.
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

    /**
     * Execute an external program and return two file descriptors for stdin and stdout. Takes a pointer to a
     * null-terminated command string with space-separated arguments. Sets $t0 to stdin FD and $t1 to stdout FD.
     *
     * @param commandPtr pointer to null-terminated command string in memory (program path and args)
     * @return a transformation sequence which sets $t0 (stdin fd) and $t1 (stdout fd)
     * @throws SimulationException on memory or process execution error
     */
    @Instruction
    public TransformationSequence exec(IAbstractInput commandPtr) throws SimulationException {
        int cmdAddr = (int) commandPtr.get(simulator).intValue();
        StringBuilder cmdStr = new StringBuilder();
        int idx = 0;
        long cur = simulator.getMemory().read(cmdAddr).intValue();
        while (cur != 0) {
            cmdStr.append((char) cur);
            idx++;
            cur = simulator.getMemory().read(cmdAddr + idx * Memory.getWordSize()).intValue();
        }

        // Parse command string into array (split by spaces)
        String[] command = cmdStr.toString().trim().split("\\s+");
        Pair<Integer, Integer> fds = simulator.executeProcess(command);

        // Set $t0 to stdin FD and $t1 to stdout FD
        InputOutputTransformable t0io = new InputOutputTransformable(simulator, new RegisterInputOutput(Registers.T0));
        Transformation t0 = t0io.transformation(new RawData(fds.getLeft()));

        InputOutputTransformable t1io = new InputOutputTransformable(simulator, new RegisterInputOutput(Registers.T1));
        Transformation t1 = t1io.transformation(new RawData(fds.getRight()));

        return new TransformationSequence(t0, t1);
    }

    /**
     * Read bytes from a file descriptor into memory. Takes: fd (file descriptor), destPtr (memory address), maxLen (max
     * bytes to read). Returns number of bytes read in $v0 (or -1 on EOF).
     *
     * @param fdInput      the file descriptor to read from
     * @param destPtrInput pointer to memory location to write data
     * @param maxLenInput  maximum number of bytes to read
     * @return a transformation sequence which writes data to memory and sets $v0 to bytes read
     * @throws SimulationException on invalid fd or read error
     */
    @Instruction
    public TransformationSequence readfd(IAbstractInput fdInput, IAbstractInput destPtrInput,
            IAbstractInput maxLenInput) throws SimulationException {
        int fd = (int) fdInput.get(simulator).intValue();
        int destAddr = (int) destPtrInput.get(simulator).intValue();
        int maxLen = (int) maxLenInput.get(simulator).intValue();

        byte[] buffer = new byte[maxLen];
        int bytesRead = simulator.readFromFd(fd, buffer, 0, maxLen);

        // Write bytes to memory (one byte per word for simplicity)
        List<Transformation> transformations = new ArrayList<>();
        for (int i = 0; i < bytesRead; i++) {
            int addr = destAddr + i * Memory.getWordSize();
            DereferenceInputOutput memIO = new DereferenceInputOutput(Registers.ZERO, addr);
            InputOutputTransformable memTransform = new InputOutputTransformable(simulator, memIO);
            transformations.add(memTransform.transformation(new RawData(buffer[i] & 0xFF)));
        }

        // Set $r0 to number of bytes read
        InputOutputTransformable r0io = new InputOutputTransformable(simulator, new RegisterInputOutput(Registers.R0));
        transformations.add(r0io.transformation(new RawData(bytesRead)));

        return new TransformationSequence(transformations.toArray(new Transformation[0]));
    }

    /**
     * Write bytes from memory to a file descriptor. Takes: fd (file descriptor), srcPtr (memory address), len (number
     * of bytes to write). Writes data from memory to the FD.
     *
     * @param fdInput     the file descriptor to write to
     * @param srcPtrInput pointer to memory location containing data
     * @param lenInput    number of bytes to write
     * @return empty transformation sequence (side-effect performed immediately)
     * @throws SimulationException on invalid fd or write error
     */
    @Instruction
    public TransformationSequence writefd(IAbstractInput fdInput, IAbstractInput srcPtrInput, IAbstractInput lenInput)
            throws SimulationException {
        int fd = (int) fdInput.get(simulator).intValue();
        int srcAddr = (int) srcPtrInput.get(simulator).intValue();
        int len = (int) lenInput.get(simulator).intValue();

        // Read bytes from memory (one byte per word)
        byte[] buffer = new byte[len];
        for (int i = 0; i < len; i++) {
            int addr = srcAddr + i * Memory.getWordSize();
            long value = simulator.getMemory().read(addr).intValue();
            buffer[i] = (byte) (value & 0xFF);
        }

        simulator.writeToFd(fd, buffer, 0, len);
        return new TransformationSequence();
    }

}
