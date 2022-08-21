package EzASM;

import EzASM.parsing.ParseException;

import java.util.Scanner;
import java.util.concurrent.atomic.AtomicBoolean;

public class SimulationThread {

    private Thread worker;
    private Thread callbackWorker;
    private final Simulator simulator;
    private final AtomicBoolean running = new AtomicBoolean(false);
    private final AtomicBoolean paused = new AtomicBoolean(false);
    public static final int SLEEP_INTERVAL = 50;

    public SimulationThread(Simulator simulator) {
        this.simulator = simulator;
    }

    public void interrupt() {
        if(worker != null) {
            worker.interrupt();
        }
        running.set(false);
        paused.set(false);
    }

    private void start(Runnable target) {
        if(!running.get()) {
            running.set(true);
            paused.set(false);
            worker = new Thread(target);
            worker.start();
            if(callbackWorker != null) {
                callbackWorker.start();
            }
        }
    }

    public void pause() {
        paused.set(true);
    }

    public void resume() {
        paused.set(false);
    }

    public void awaitTermination() {
        while(worker != null && worker.isAlive()) {
            try {
                Thread.sleep(SLEEP_INTERVAL);
            } catch (InterruptedException ignored) {}
        }
    }

    public void runOneLine() {
        start(this::runnableRunOneLine);
    }

    public void runLinesFromStart() {
        start(this::runnableRunLinesFromStart);
    }

    public void runFromCliInput() {
        start(this::runnableRunFromCLI);
    }

    private void runnableRunOneLine() {
        try {
            simulator.runOneLine();
        } catch (ParseException e) {
            System.err.println(e.getMessage());
        }
    }

    private void runnableRunLinesFromStart() {
        try {
            simulator.runLinesFromPC(paused);
        } catch (ParseException e) {
            System.err.println(e.getMessage());
        }
    }

    private void runnableRunFromCLI() {
        Scanner scanner = new Scanner(System.in);
        System.out.print("> ");
        while (scanner.hasNextLine() && !Thread.interrupted()){
            try {
                simulator.executeLine(scanner.nextLine());
            } catch (EzASM.parsing.ParseException e) {
                System.err.println(e.getMessage());
                System.err.flush();
                try {
                    Thread.sleep(SLEEP_INTERVAL);
                } catch (InterruptedException ignored) {
                    break;
                }
            }
            System.out.print("> ");
        }
    }

    public void setCompletionCallback(Runnable runnable) {
        assert runnable != null;
        callbackWorker = new Thread(() -> {
                try {
                    worker.join();
                    runnable.run();
                } catch (Exception e) {
                    System.err.println("Unable to perform callback function");
                }
                callbackWorker = null;
        });

    }
}
