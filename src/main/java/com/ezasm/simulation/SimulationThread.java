package com.ezasm.simulation;

import com.ezasm.instructions.exception.InstructionDispatchException;
import com.ezasm.parsing.Lexer;
import com.ezasm.parsing.Line;
import com.ezasm.parsing.ParseException;

import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;
import java.util.concurrent.atomic.AtomicBoolean;

/**
 * A thread for modifying a Simulator. Used to ensure that the current thread does not get blocked
 * while executing code. Can only maintain the currently running task; does not have a queue of
 * tasks.
 */
public class SimulationThread {

    private Thread worker;
    private Thread callbackWorker;
    private final ISimulator simulator;
    private final AtomicBoolean running = new AtomicBoolean(false);
    private final AtomicBoolean paused = new AtomicBoolean(false);

    /**
     * The sleep interval to wait before doing another action. Used in the pause busy-wait.
     */
    public static final int SLEEP_INTERVAL = 50;

    /**
     * Constructs a simulation thread based on the given simulator.
     *
     * @param simulator the simulator to act on.
     */
    public SimulationThread(ISimulator simulator) {
        this.simulator = simulator;
    }

    /**
     * Interrupt the worker thread which should quickly stop its execution.
     */
    public void interrupt() {
        if (worker != null) {
            worker.interrupt();
        }
        running.set(false);
        paused.set(false);
    }

    /**
     * Starts the thread if it is not already running.
     *
     * @param target the content to run on the thread.
     */
    private void start(Runnable target) {
        if (!running.get()) {
            running.set(true);
            paused.set(false);
            worker = new Thread(target);
            worker.start();
            if (callbackWorker != null && callbackWorker.getState() == Thread.State.NEW) {
                callbackWorker.start();
            }
        }
    }

    /**
     * Pauses the current thread's execution.
     */
    public void pause() {
        paused.set(true);
    }

    /**
     * Resumes the current thread's execution.
     */
    public void resume() {
        paused.set(false);
    }

    /**
     * Awaits the termination of the worker thread by busy-waiting.
     */
    public void awaitTermination() {
        while (worker != null && worker.isAlive()) {
            try {
                Thread.sleep(SLEEP_INTERVAL);
            } catch (InterruptedException ignored) {
            }
        }
    }

    /**
     * Runs one line from the simulation on the thread.
     */
    public void runOneLine() {
        start(this::runnableRunOneLine);
    }

    /**
     * Runs the entire program from the current PC.
     */
    public void runLinesFromPC() {
        start(this::runnableRunLinesFromPC);
    }

    /**
     * Runs the simulation from a CLI interface.
     */
    public void runFromCliInput() {
        start(this::runnableRunFromCLI);
    }

    /**
     * Runs one line of the simulation.
     */
    private void runnableRunOneLine() {
        try {
            simulator.executeLineFromPC();
        } catch (InstructionDispatchException e) {
            System.err.println(e.getMessage());
        }
    }

    /**
     * Runs the rest of the program on the simulation.
     */
    private void runnableRunLinesFromPC() {
        try {
            simulator.executeProgramFromPC();
        } catch (InstructionDispatchException e) {
            System.err.println(e.getMessage());
        }
    }

    /**
     * Runs a command line interface version of the program.
     */
    private void runnableRunFromCLI() {
        Scanner scanner = new Scanner(System.in);
        Map<String, Integer> labels = new HashMap<>();
        int lineNumber = 0;

        System.out.print("> ");
        while (scanner.hasNextLine() && !Thread.interrupted()) {
            try {
                Line line = Lexer.parseLine(scanner.nextLine(), labels, lineNumber);
                simulator.runLine(line);
            } catch (ParseException | InstructionDispatchException e) {
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

    /**
     * Sets the callback function of the current thread to the given function. Only activates the
     * callback function on the termination of the next thread; no subsequent threads will use this
     * callback function.
     *
     * @param runnable the callback function to execute when the next worker thread dies.
     */
    public void setCompletionCallback(Runnable runnable) {
        assert runnable != null;
        callbackWorker = new Thread(() -> {
            try {
                worker.join();
                running.set(false);
                paused.set(false);
                runnable.run();
            } catch (Exception e) {
                System.err.println("Unable to perform callback function");
            }
            callbackWorker = null;
        });

    }
}
