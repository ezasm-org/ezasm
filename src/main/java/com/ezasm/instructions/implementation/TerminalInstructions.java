package com.ezasm.instructions.implementation;

import java.io.*;
import java.util.Scanner;
import java.util.regex.Pattern;

import com.ezasm.instructions.targets.inputoutput.IAbstractInputOutput;
import com.ezasm.instructions.targets.inputoutput.mock.MemoryInputOutput;
import com.ezasm.simulation.Transformation;
import com.ezasm.simulation.TransformationSequence;
import com.ezasm.util.Conversion;
import com.ezasm.gui.Window;
import com.ezasm.instructions.Instruction;
import com.ezasm.instructions.targets.input.IAbstractInput;
import com.ezasm.instructions.targets.output.IAbstractOutput;
import com.ezasm.simulation.ISimulator;
import com.ezasm.simulation.exception.SimulationException;

import static com.ezasm.gui.util.DialogFactory.promptWarningDialog;
import static org.apache.commons.lang3.math.NumberUtils.min;

/**
 * An implementation of standard terminal I/O instructions for simulation.
 */
public class TerminalInstructions {

    public static final InputStream DEFAULT_INPUT_STREAM = System.in;
    public static final OutputStream DEFAULT_OUTPUT_STREAM = System.out;
    private static InputStream inputStream = DEFAULT_INPUT_STREAM;
    private static OutputStream outputStream = DEFAULT_OUTPUT_STREAM;

    private final ISimulator simulator;
    private static Scanner inputReader;
    private static PrintStream outputWriter;

    static {
        setInputOutput(inputStream, outputStream);
    }

    /**
     * Set the input and output of all terminal instructions.
     *
     * @param newInput  the input stream.
     * @param newOutput the output stream.
     */
    public static void setInputOutput(InputStream newInput, OutputStream newOutput) {
        setInputStream(newInput);
        setOutputStream(newOutput);
    }

    public static void setOutputStream(OutputStream newOutput) {
        outputStream = newOutput;
        outputWriter = new PrintStream(newOutput);
    }

    public static void setInputStream(InputStream newInput) {
        inputStream = newInput;
        inputReader = new Scanner(newInput);
    }

    public static void resetInputStream() {
        try {
            if (inputStream instanceof FileInputStream) {
                inputReader = new Scanner(new FileInputStream(Window.getInputFilePath()));
            }
        } catch (IOException e) {
            promptWarningDialog("Error Reading File",
                    String.format("There was an error reading from '%s'", Window.getInputFilePath()));
        }
    }

    public TerminalInstructions(ISimulator simulator) {
        this.simulator = simulator;
    }

    @Instruction
    public TransformationSequence printi(IAbstractInput input) throws SimulationException {
        try {
            outputWriter.print(Conversion.bytesToLong(input.get(simulator)));
        } catch (Exception e) {
            // TODO make I/O simulation exception
            throw new SimulationException("Error writing integer to output");
        }
        return new TransformationSequence();
    }

    @Instruction
    public TransformationSequence printf(IAbstractInput input) throws SimulationException {
        try {
            outputWriter.print(Conversion.bytesToDouble(input.get(simulator)));
        } catch (Exception e) {
            // TODO make I/O simulation exception
            throw new SimulationException("Error writing float to output");
        }
        return new TransformationSequence();
    }

    @Instruction
    public TransformationSequence printc(IAbstractInput input) throws SimulationException {
        try {
            outputWriter.print((char) Conversion.bytesToLong(input.get(simulator)));
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
            outputWriter.print(s);
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
                    Conversion.longToBytes(inputReader.nextLong()));
        } catch (Exception e) {
            // TODO make I/O simulation exception
            throw new SimulationException("Error reading integer from input");
        }
    }

    @Instruction
    public TransformationSequence readf(IAbstractInputOutput output) throws SimulationException {
        try {
            output.set(simulator, Conversion.doubleToBytes(inputReader.nextDouble()));
            return new TransformationSequence(output, output.get(simulator),
                    Conversion.doubleToBytes(inputReader.nextDouble()));
        } catch (Exception e) {
            // TODO make I/O simulation exception
            throw new SimulationException("Error reading double from input");
        }
    }

    @Instruction
    public TransformationSequence readc(IAbstractInputOutput output) throws SimulationException {
        Pattern oldDelimiter = inputReader.delimiter();
        inputReader.useDelimiter("");
        try {
            String current = " ";
            while (current.matches("\\s")) {
                current = inputReader.next();
            }
            inputReader.useDelimiter(oldDelimiter);
            return new TransformationSequence(output, output.get(simulator), Conversion.longToBytes(current.charAt(0)));
        } catch (Exception e) {
            // TODO make I/O simulation exception
            inputReader.useDelimiter(oldDelimiter);
            throw new SimulationException("Error reading character from input");
        }
    }

    @Instruction
    public TransformationSequence reads(IAbstractInput input1, IAbstractInput input2) throws SimulationException {
        try {
            int maxSize = (int) Conversion.bytesToLong(input2.get(simulator));
            int address = (int) Conversion.bytesToLong(input1.get(simulator));
            String string = inputReader.next();

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
            String string = inputReader.nextLine();

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

    /**
     * Clears the scanner's buffer for use on error and program end.
     */
    public static void clearBuffer() {
        try {
            inputStream.skipNBytes(inputStream.available());
            // modifications to the input stream do not update the scanner
            // and the scanner has no way to clear its buffer... so evil hack
            inputReader = new Scanner(inputStream);
        } catch (Exception ignored) {
        }
    }
}
