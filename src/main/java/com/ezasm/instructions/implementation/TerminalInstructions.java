package com.ezasm.instructions.implementation;

import java.io.*;
import java.util.regex.Pattern;

import com.ezasm.instructions.targets.inputoutput.IAbstractInputOutput;
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
        try {
            streams.outputWriter().print(Conversion.bytesToLong(input.get(simulator)));
        } catch (Exception e) {
            // TODO make I/O simulation exception
            throw new SimulationException("Error writing integer to output");
        }
        return new TransformationSequence();
    }

    @Instruction
    public TransformationSequence printf(IAbstractInput input) throws SimulationException {
        try {
            streams.outputWriter().print(Conversion.bytesToDouble(input.get(simulator)));
        } catch (Exception e) {
            // TODO make I/O simulation exception
            throw new SimulationException("Error writing float to output");
        }
        return new TransformationSequence();
    }

    @Instruction
    public TransformationSequence printc(IAbstractInput input) throws SimulationException {
        try {
            streams.outputWriter().print((char) Conversion.bytesToLong(input.get(simulator)));
        } catch (Exception e) {
            // TODO make I/O simulation exception
            throw new SimulationException("Error writing character to output");
        }
        return new TransformationSequence();
    }

    @Instruction
    public TransformationSequence prints(IAbstractInput input1, IAbstractInput input2) throws SimulationException {
        int address = (int) Conversion.bytesToLong(input1.get(simulator));
        int maxSize = (int) Conversion.bytesToLong(input2.get(simulator));
        String s = simulator.getMemory().readString(address, maxSize);
        try {
            streams.outputWriter().print(s);
        } catch (Exception e) {
            // TODO make I/O simulation exception
            throw new SimulationException("Error writing string to output");
        }
        return new TransformationSequence();
    }

    @Instruction
    public TransformationSequence readi(IAbstractInputOutput output) throws SimulationException {
        try {
            return new TransformationSequence(output, output.get(simulator),
                    Conversion.longToBytes(streams.inputReader().nextLong()));
        } catch (Exception e) {
            // TODO make I/O simulation exception
            throw new SimulationException("Error reading integer from input");
        }
    }

    @Instruction
    public TransformationSequence readf(IAbstractInputOutput output) throws SimulationException {
        try {
            output.set(simulator, Conversion.doubleToBytes(streams.inputReader().nextDouble()));
            return new TransformationSequence(output, output.get(simulator),
                    Conversion.doubleToBytes(streams.inputReader().nextDouble()));
        } catch (Exception e) {
            // TODO make I/O simulation exception
            throw new SimulationException("Error reading double from input");
        }
    }

    @Instruction
    public TransformationSequence readc(IAbstractInputOutput output) throws SimulationException {
        Pattern oldDelimiter = streams.inputReader().delimiter();
        streams.inputReader().useDelimiter("");
        try {
            String current = " ";
            while (current.matches("\\s")) {
                current = streams.inputReader().next();
            }
            streams.inputReader().useDelimiter(oldDelimiter);
            return new TransformationSequence(output, output.get(simulator), Conversion.longToBytes(current.charAt(0)));
        } catch (Exception e) {
            // TODO make I/O simulation exception
            streams.inputReader().useDelimiter(oldDelimiter);
            throw new SimulationException("Error reading character from input");
        }
    }

    @Instruction
    public TransformationSequence reads(IAbstractInput input1, IAbstractInput input2) throws SimulationException {
        try {
            int maxSize = (int) Conversion.bytesToLong(input2.get(simulator));
            int address = (int) Conversion.bytesToLong(input1.get(simulator));
            String string = streams.inputReader().next();

            int size = min(maxSize, string.length());
            Transformation[] transformations = new Transformation[size];

            for (int i = 0; i < size; ++i) {
                MemoryInputOutput m = new MemoryInputOutput(address);
                transformations[i] = m.transformation(simulator, Conversion.longToBytes(string.charAt(i)));
                address = address + simulator.getMemory().WORD_SIZE;
            }

            return new TransformationSequence(transformations);
        } catch (Exception e) {
            // TODO make I/O simulation exception
            throw new SimulationException("Error reading string from input");
        }
    }

    @Instruction
    public TransformationSequence readline(IAbstractInput input1, IAbstractInput input2) throws SimulationException {
        try {
            int maxSize = (int) Conversion.bytesToLong(input2.get(simulator));
            int address = (int) Conversion.bytesToLong(input1.get(simulator));
            String string = streams.inputReader().nextLine();

            int size = min(maxSize, string.length());
            Transformation[] transformations = new Transformation[size];

            for (int i = 0; i < size; ++i) {
                MemoryInputOutput m = new MemoryInputOutput(address);
                transformations[i] = m.transformation(simulator, Conversion.longToBytes(string.charAt(i)));
                address = address + simulator.getMemory().WORD_SIZE;
            }

            return new TransformationSequence(transformations);
        } catch (Exception e) {
            // TODO make I/O simulation exception
            throw new SimulationException("Error reading string from input");
        }
    }

}
