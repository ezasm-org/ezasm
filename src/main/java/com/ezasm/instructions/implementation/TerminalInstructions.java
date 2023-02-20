package com.ezasm.instructions.implementation;

import java.io.*;
import java.util.Scanner;
import java.util.regex.Pattern;

import com.ezasm.util.Conversion;
import com.ezasm.gui.Window;
import com.ezasm.instructions.Instruction;
import com.ezasm.instructions.targets.input.IAbstractInput;
import com.ezasm.instructions.targets.output.IAbstractOutput;
import com.ezasm.simulation.ISimulator;
import com.ezasm.simulation.exception.SimulationException;

/**
 * An implementation of standard terminal I/O instructions for simulation.
 */
public class TerminalInstructions {

    public static InputStream defaultInputStream = System.in;
    public static OutputStream defaultOutputStream = System.out;
    private static InputStream inputStream = defaultInputStream;
    private static OutputStream outputStream = defaultOutputStream;

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

        } catch (IOException ignored) {

        }
    }

    public TerminalInstructions(ISimulator simulator) {
        this.simulator = simulator;
    }

    @Instruction
    public void printi(IAbstractInput input) throws SimulationException {
        outputWriter.print(Conversion.bytesToLong(input.get(simulator)));
    }

    @Instruction
    public void printf(IAbstractInput input) throws SimulationException {
        outputWriter.print(Conversion.bytesToDouble(input.get(simulator)));
    }

    @Instruction
    public void printc(IAbstractInput input) throws SimulationException {
        outputWriter.print((char) Conversion.bytesToLong(input.get(simulator)));
    }

    @Instruction
    public void prints(IAbstractInput input1, IAbstractInput input2) throws SimulationException {
        int address = (int) Conversion.bytesToLong(input1.get(simulator));
        int maxSize = (int) Conversion.bytesToLong(input2.get(simulator));
        String s = simulator.getMemory().readString(address, maxSize);
        outputWriter.print(s);
    }

    @Instruction
    public void readi(IAbstractOutput output) throws SimulationException {
        try {
            output.set(simulator, Conversion.longToBytes(inputReader.nextLong()));
        } catch (Exception e) {
            // TODO make I/O simulation exception
            throw new SimulationException("Error reading integer from input");
        }
    }

    @Instruction
    public void readf(IAbstractOutput output) throws SimulationException {
        try {
            output.set(simulator, Conversion.doubleToBytes(inputReader.nextDouble()));
        } catch (Exception e) {
            // TODO make I/O simulation exception
            throw new SimulationException("Error reading double from input");
        }
    }

    @Instruction
    public void readc(IAbstractOutput output) throws SimulationException {
        Pattern oldDelimiter = inputReader.delimiter();
        inputReader.useDelimiter("");
        try {
            String current = " ";
            while (current.matches("\\s")) {
                current = inputReader.next();
            }
            output.set(simulator, Conversion.longToBytes(current.charAt(0)));
            inputReader.useDelimiter(oldDelimiter);
        } catch (Exception e) {
            // TODO make I/O simulation exception
            inputReader.useDelimiter(oldDelimiter);
            throw new SimulationException("Error reading character from input");
        }
    }

    @Instruction
    public void reads(IAbstractInput input1, IAbstractInput input2) throws SimulationException {
        try {
            int maxSize = (int) Conversion.bytesToLong(input2.get(simulator));
            int address = (int) Conversion.bytesToLong(input1.get(simulator));
            simulator.getMemory().writeString(address, inputReader.next(), maxSize);
        } catch (Exception e) {
            // TODO make I/O simulation exception
            throw new SimulationException("Error reading string from input");
        }
    }

    @Instruction
    public void readline(IAbstractInput input1, IAbstractInput input2) throws SimulationException {
        try {
            int maxSize = (int) Conversion.bytesToLong(input2.get(simulator));
            int address = (int) Conversion.bytesToLong(input1.get(simulator));
            simulator.getMemory().writeString(address, inputReader.nextLine(), maxSize);
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
