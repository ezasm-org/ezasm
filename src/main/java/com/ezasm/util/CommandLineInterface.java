package com.ezasm.util;

import com.ezasm.instructions.implementation.TerminalInstructions;
import com.ezasm.parsing.Lexer;
import com.ezasm.parsing.Line;
import com.ezasm.parsing.ParseException;
import com.ezasm.simulation.Simulator;
import com.ezasm.simulation.Registers;
import com.ezasm.simulation.exception.SimulationException;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.List;
import java.util.Scanner;

/**
 * A representation of an instance in the command line interface. Stores the current simulation and the simulation
 * thread.
 */
public class CommandLineInterface {

    private final Simulator simulator;
    private final boolean cli;
    private InputStream inputStream = null;
    private OutputStream outputStream = null;

    /**
     * Constructs a basic CLI based on the given Simulator. This CLI will read from the terminal until the program is
     * closed or the EOF signal is sent.
     *
     * @param simulator the given Simulator.
     */
    public CommandLineInterface(Simulator simulator) {
        this.simulator = simulator;
        this.cli = true;
    }

    /**
     * Constructs a CLI based on the given Simulator for operating on code from a file.
     *
     * @param simulator the given Simulator.
     * @param path      the file to read code from.
     */
    public CommandLineInterface(Simulator simulator, String path) {
        this.simulator = simulator;
        this.cli = false;
        try {
            File file = new File(path);
            List<Line> lines = Lexer.parseLines(FileIO.readFile(file));
            this.simulator.addLines(lines, file);
        } catch (ParseException | IOException e) {
            System.err.println("Unable to parse the given file: " + e.getMessage());
            System.exit(1);
        }
    }

    /**
     * Constructs a CLI based ont the given Simulator for operating code from a file with redirected input and/or output
     *
     * @param simulator      the given Simulator.
     * @param file           the file to read code from.
     * @param inputFilePath  the file to read the input from.
     * @param outputFilePath the file to write the output to.
     */
    public CommandLineInterface(Simulator simulator, String path, String inputFilePath, String outputFilePath) {
        this.simulator = simulator;
        this.cli = false;
        try {
            File file = new File(path);
            List<Line> lines = Lexer.parseLines(FileIO.readFile(file));
            this.simulator.addLines(lines, file);
        } catch (ParseException | IOException e) {
            System.err.println("Unable to parse the given file: " + e.getMessage());
            System.exit(1);
        }

        try {
            if (inputFilePath.length() > 0) {
                inputStream = new FileInputStream(new File(inputFilePath));
            } else {
                inputStream = System.in;
            }
        } catch (IOException e) {
            System.err.printf("Unable to read input from %s: %s", inputFilePath, e.getMessage());
            System.exit(1);
        }

        try {
            if (outputFilePath.length() > 0) {
                File outputFile = new File(outputFilePath);
                outputFile.createNewFile();
                outputStream = new FileOutputStream(outputFile);
            } else
                outputStream = System.out;
        } catch (IOException e) {
            System.err.printf("Unable to write output to %s: %s", outputFilePath, e.getMessage());
            System.exit(1);
        }
    }

    /**
     * Begins the simulation. Starts reading CLI input or reads and executes from the given file.
     */
    public void startSimulation() {
        if (inputStream != null) {
            TerminalInstructions.streams().setInputStream(inputStream);
        }
        if (outputStream != null) {
            TerminalInstructions.streams().setOutputStream(outputStream);
        }
        if (cli) {
            runFromCliInput();
        } else {
            runLinesFromBeginning();
        }
    }

    /**
     * Uses the simulation thread to run from the CLI input.
     */
    private void runFromCliInput() {
        Scanner scanner = new Scanner(System.in);
        int lineNumber = 0;

        System.out.print("> ");
        while (scanner.hasNextLine() && !Thread.interrupted()) {
            try {
                Line line = Lexer.parseLine(scanner.nextLine(), lineNumber);
                simulator.runLine(line);
            } catch (ParseException | SimulationException e) {
                System.err.flush();
            }
            System.out.print("> ");
        }
        try {
            Thread.sleep(50);
        } catch (InterruptedException ignored) {
        }
    }

    /**
     * Uses the simulation thread to run the code from the file.
     */
    private void runLinesFromBeginning() {
        try {
            simulator.executeProgramFromPC();
        } catch (SimulationException e) {
            System.err.println(e.getMessage());
        }
        System.exit((int) simulator.getRegisters().getRegister(Registers.R0).getLong());
    }

}
