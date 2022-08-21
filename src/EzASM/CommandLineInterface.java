package EzASM;

import EzASM.simulation.SimulationThread;
import EzASM.simulation.Simulator;

public class CommandLineInterface {

    private final Simulator simulator;
    private final SimulationThread simulationThread;
    private final boolean cli;

    public CommandLineInterface(Simulator simulator) {
        this.simulator = simulator;
        this.cli = true;
        this.simulationThread = new SimulationThread(simulator);
    }

    public CommandLineInterface(Simulator simulator, String file) {
        this.simulator = simulator;
        this.cli = false;
        this.simulationThread = new SimulationThread(simulator);
        try {
            this.simulator.readMultiLineString(file);
        } catch (EzASM.parsing.ParseException e) {
            System.err.println(e.getMessage());
        }
    }

    public void startSimulation() {
        if(cli) {
            runFromCliInput();
        } else {
            runLinesFromBeginning();
        }
    }

    public void runFromCliInput() {
        simulationThread.runFromCliInput();
    }

    private void runLinesFromBeginning() {
        simulationThread.runLinesFromPC();
    }

}
