package com.ezasm;

import com.ezasm.parsing.ParseException;
import com.ezasm.simulation.SimulationThread;
import com.ezasm.simulation.Simulator;

/**
 * A representation of an instance in the command line interface. Stores the current simulation and
 * the simulation thread.
 */
public class CommandLineInterface {

    private final Simulator simulator;
    private final SimulationThread simulationThread;
    private final boolean cli;

    /**
     * Constructs a basic CLI based on the given Simulator. This CLI will read from the terminal until
     * the program is closed or the EOF signal is sent.
     *
     * @param simulator the given Simulator.
     */
    public CommandLineInterface(Simulator simulator) {
        this.simulator = simulator;
        this.cli = true;
        this.simulationThread = new SimulationThread(simulator);
    }

    /**
     * Constructs a CLI based on the given Simulator for operating on code from a file.
     *
     * @param simulator the given Simulator.
     * @param file      the file to read code from.
     */
    public CommandLineInterface(Simulator simulator, String file) {
        this.simulator = simulator;
        this.cli = false;
        this.simulationThread = new SimulationThread(simulator);
        try {
            this.simulator.readMultiLineString(file);
        } catch (ParseException e) {
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
        simulationThread.runFromCliInput();
    }

    /**
     * Uses the simulation thread to run the code from the file.
     */
    private void runLinesFromBeginning() {
        simulationThread.runLinesFromPC();
    }

}
