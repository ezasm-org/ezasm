package com.ezasm.instructions.implementation;

import java.io.*;

import com.ezasm.instructions.targets.inputoutput.IAbstractInputOutput;
import com.ezasm.instructions.targets.inputoutput.mock.FileReadInputOutput;
import com.ezasm.instructions.targets.inputoutput.mock.MemoryInputOutput;
import com.ezasm.simulation.Transformation;
import com.ezasm.simulation.TransformationSequence;
import com.ezasm.util.Conversion;
import com.ezasm.instructions.Instruction;
import com.ezasm.instructions.targets.input.IAbstractInput;
import com.ezasm.simulation.ISimulator;
import com.ezasm.simulation.exception.SimulationException;

import static org.apache.commons.lang3.math.NumberUtils.min;

/**
 * An implementation of standard terminal I/O instructions for simulation.
 */
public class TerminalInstructions {

    public static final InputStream DEFAULT_INPUT_STREAM = System.in;
    public static final OutputStream DEFAULT_OUTPUT_STREAM = System.out;

    private static final StreamManager streams = new StreamManager(DEFAULT_INPUT_STREAM, DEFAULT_OUTPUT_STREAM);

    private final ISimulator simulator;

    public static StreamManager streams() {
        return streams;
    }

    public TerminalInstructions(ISimulator simulator) {
        this.simulator = simulator;
    }

    @Instruction
    public TransformationSequence printi(IAbstractInput input) throws SimulationException {
        streams.write(Conversion.bytesToLong(input.get(simulator)));
        return new TransformationSequence();
    }

    @Instruction
    public TransformationSequence printf(IAbstractInput input) throws SimulationException {
        streams.write(Conversion.bytesToDouble(input.get(simulator)));
        return new TransformationSequence();
    }

    @Instruction
    public TransformationSequence printc(IAbstractInput input) throws SimulationException {
        streams.write((char) Conversion.bytesToLong(input.get(simulator)));
        return new TransformationSequence();
    }

    @Instruction
    public TransformationSequence prints(IAbstractInput input1, IAbstractInput input2) throws SimulationException {
        int address = (int) Conversion.bytesToLong(input1.get(simulator));
        int maxSize = (int) Conversion.bytesToLong(input2.get(simulator));
        String s = simulator.getMemory().readString(address, maxSize);
        streams.write(s);
        return new TransformationSequence();
    }

    private interface DataSupplier {
        byte[] get() throws SimulationException;
    }

    private TransformationSequence read(DataSupplier supplier, IAbstractInputOutput output) throws SimulationException {
        FileReadInputOutput f = new FileReadInputOutput(streams().getCursor());
        byte[] data = supplier.get();
        System.out.println("--> " + Conversion.bytesToLong(data));
        Transformation t1 = f.transformation(simulator, Conversion.longToBytes(streams().getCursor()));
        Transformation t2 = new Transformation(output, output.get(simulator), data);
        return new TransformationSequence(t1, t2);
    }

    @Instruction
    public TransformationSequence readi(IAbstractInputOutput output) throws SimulationException {
        return read(() -> Conversion.longToBytes(streams.readLong()), output);
    }

    @Instruction
    public TransformationSequence readf(IAbstractInputOutput output) throws SimulationException {
        return read(() -> Conversion.doubleToBytes(streams.readDouble()), output);
    }

    @Instruction
    public TransformationSequence readc(IAbstractInputOutput output) throws SimulationException {
        return read(() -> Conversion.longToBytes(streams.readChar()), output);
    }

    @Instruction
    public TransformationSequence reads(IAbstractInput input1, IAbstractInput input2) throws SimulationException {
        int maxSize = (int) Conversion.bytesToLong(input2.get(simulator));
        int address = (int) Conversion.bytesToLong(input1.get(simulator));

        FileReadInputOutput f = new FileReadInputOutput(streams().getCursor());
        String string = streams.readString();

        int size = min(maxSize, string.length());
        Transformation[] transformations = new Transformation[size + 1];
        transformations[0] = f.transformation(simulator, Conversion.longToBytes(streams().getCursor()));

        for (int i = 1; i <= size; ++i) {
            MemoryInputOutput m = new MemoryInputOutput(address);
            transformations[i] = m.transformation(simulator, Conversion.longToBytes(string.charAt(i)));
            address = address + simulator.getMemory().WORD_SIZE;
        }

        return new TransformationSequence(transformations);
    }

    @Instruction
    public TransformationSequence readline(IAbstractInput input1, IAbstractInput input2) throws SimulationException {
        int maxSize = (int) Conversion.bytesToLong(input2.get(simulator));
        int address = (int) Conversion.bytesToLong(input1.get(simulator));

        FileReadInputOutput f = new FileReadInputOutput(streams().getCursor());
        String string = streams.readLine();

        int size = min(maxSize, string.length());
        Transformation[] transformations = new Transformation[size + 1];
        transformations[0] = f.transformation(simulator, Conversion.longToBytes(streams().getCursor()));

        for (int i = 1; i <= size; ++i) {
            MemoryInputOutput m = new MemoryInputOutput(address);
            transformations[i] = m.transformation(simulator, Conversion.longToBytes(string.charAt(i)));
            address = address + simulator.getMemory().WORD_SIZE;
        }

        return new TransformationSequence(transformations);
    }

}
