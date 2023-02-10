package com.ezasm;

import com.ezasm.parsing.Lexer;
import com.ezasm.parsing.Line;
import com.ezasm.parsing.ParseException;
import com.ezasm.simulation.ISimulator;
import com.ezasm.simulation.exception.SimulationException;

import java.io.File;
import java.io.IOException;
import java.util.Scanner;

/**
 * A representation of an instance in the command line interface. Stores the current simulation and the simulation
 * thread.
 */
public class CommandLineInterface {

    private final ISimulator simulator;
    private final boolean cli;

    /**
     * Constructs a basic CLI based on the given Simulator. This CLI will read from the terminal until the program is
     * closed or the EOF signal is sent.
     *
     * @param simulator the given Simulator.
     */
    public CommandLineInterface(ISimulator simulator) {
        this.simulator = simulator;
        this.cli = true;
    }

    /**
     * Constructs a CLI based on the given Simulator for operating on code from a file.
     *
     * @param simulator the given Simulator.
     * @param file      the file to read code from.
     */
    public CommandLineInterface(ISimulator simulator, String file) {
        this.simulator = simulator;
        this.cli = false;
        try {
            this.simulator.addLines(Lexer.parseLines(FileIO.readFile(new File(file)), 0));
        } catch (ParseException | IOException e) {
            System.err.println(e.getMessage());
            System.exit(1);
        }
    }

    /**
     * Begins the simulation. Starts reading CLI input or reads and executes from the given file.
     */
    public void startSimulation() {
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
                System.err.println(e.getMessage());
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

    }

}
