package EzASM;

import EzASM.parsing.ParseException;

import java.util.Scanner;

public class CommandLineInterface {

    private final Simulator simulator;
    private Thread simulationThread;
    private final boolean cli;

    public CommandLineInterface(Simulator simulator) {
        this.simulator = simulator;
        this.cli = true;
    }

    public CommandLineInterface(Simulator simulator, String file) {
        this.simulator = simulator;
        this.cli = false;
        try {
            this.simulator.readFile(file);
        } catch (EzASM.parsing.ParseException e) {
            System.err.println(e.getMessage());
        }
    }

    public void startSimulation() {
        stopSimulation();
        if(cli) {
            startCliInput();
        } else {
            startSimulationFromBeginning();
        }
    }

    public void startCliInput() {
        simulationThread = new Thread(() -> {
            Scanner scanner = new Scanner(System.in);
            System.out.print("> ");
                while (scanner.hasNext()){
                    try {
                        simulator.executeLine(scanner.nextLine());
                    } catch (EzASM.parsing.ParseException e) {
                        System.err.println(e.getMessage());
                    }
                    System.out.print("> ");
                }
        });
        simulationThread.start();
    }

    private void startSimulationFromBeginning() {
        simulationThread = new Thread(() -> {
            try {
                simulator.runLinesFromStart();
                System.out.println(simulator.registryToString());
            } catch (ParseException e) {
                System.err.println(e.getMessage());
            }
        });
        simulationThread.start();
    }

    public void runOneLine() {
        stopSimulation();
        simulationThread = new Thread(() -> {
            try {
                simulator.runOneLine();
            } catch (ParseException e) {
                System.err.println(e.getMessage());
            }
        });
        simulationThread.start();

    }

    public void stopSimulation() {
        if(simulationThread != null) simulationThread.interrupt();
    }

}
