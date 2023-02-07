package com.ezasm.gui;

import com.ezasm.parsing.ParseException;
import com.ezasm.simulation.SimulationException;

import static com.ezasm.gui.ToolbarFactory.*;

public class SimulatorGUIActions {

    enum State {
        IDLE,
        STOPPED,
        RUNNING,
        PAUSED
    }

    private static final long LOOP_BUSY_WAIT_MS = 50L;

    private static Thread worker;
    private static State state = State.IDLE;
    private static long instructionDelayMS = 500L;

    private static void setState(State newState) {
        state = newState;

        boolean isDone = state == State.IDLE || state == State.STOPPED;

        Window.getInstance().setEditable(isDone);
        startButton.setEnabled(isDone);
        stopButton.setEnabled(!isDone);
        stepButton.setEnabled(state != State.RUNNING);
        pauseButton.setEnabled(state == State.RUNNING);
        resumeButton.setEnabled(state == State.PAUSED);
        resetButton.setEnabled(state != State.IDLE);
    }

    private static void handleProgramCompletion() {
        if (state != State.IDLE) {
            Window.getInstance().handleProgramCompletion();
        }
    }

    static void step() {
        if (state == State.IDLE || state == State.STOPPED) {
            setState(State.PAUSED);
            try {
                Window.getInstance().parseText();
                System.out.println("** Program starting **");
                startWorker();
            } catch (ParseException e) {
                setState(State.IDLE);
                Window.getInstance().setEditable(true);
                Window.getInstance().handleParseException(e);
            }
        } else {
            try {
                Window.getInstance().getSimulator().executeLineFromPC();
                Window.updateAll();
            } catch (SimulationException e) {
                setState(State.STOPPED);
                System.err.println(e.getMessage());
                handleProgramCompletion();
            }
        }
        if (Window.getInstance().getSimulator().isDone() && state == State.PAUSED) {
            setState(State.STOPPED);
        }
    }

    static void start() {
        try {
            Window.getInstance().setEditable(false);
            Window.getInstance().parseText();
            setState(State.RUNNING);
            System.out.println("** Program starting **");
            startWorker();
        } catch (ParseException e) {
            stop();
            Window.getInstance().handleParseException(e);
        }
    }

    static void stop() {
        setState(State.STOPPED);
        killWorker();
        awaitWorkerTermination();
        handleProgramCompletion();
    }

    static void pause() {
        setState(State.PAUSED);
    }

    static void resume() {
        setState(State.RUNNING);
    }

    static void reset() {
        if (state != State.STOPPED) {
            killWorker();
            awaitWorkerTermination();
            handleProgramCompletion();
        }
        setState(State.IDLE);
        Window.getInstance().getSimulator().resetAll();
        Window.updateAll();
    }

    private static void simulationLoop() {
        while (!Window.getInstance().getSimulator().isDone() && (state == State.RUNNING || state == State.PAUSED)) {
            try {
                Window.getInstance().getSimulator().executeLineFromPC();
                Window.updateAll();
            } catch (SimulationException e) {
                Window.getInstance().handleParseException(e);
                setState(State.STOPPED);
                break;
            }
            try {
                Thread.sleep(instructionDelayMS);
                while (state == State.PAUSED) { // busy wait
                    Thread.sleep(LOOP_BUSY_WAIT_MS);
                }
            } catch (InterruptedException e) {
                return;
            }
        }
        onSimulatorCompleteInLoop();
    }

    private static void onSimulatorCompleteInLoop() {
        setState(State.STOPPED);
        handleProgramCompletion();
    }

    private static void startWorker() {
        worker = new Thread(SimulatorGUIActions::simulationLoop);
        worker.start();
    }

    private static void killWorker() {
        worker.interrupt();
    }

    private static void awaitWorkerTermination() {
        try {
            worker.join();
        } catch (InterruptedException ignored) {
        }
    }

}
