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

    private static Thread worker;
    private static State state = State.IDLE;

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

    static void handleProgramCompletion() {
        Window.getInstance().handleProgramCompletion();
    }

    public static void step() {
        if (Window.getInstance().getEditable()) {
            Window.getInstance().setEditable(false);
            try {
                Window.getInstance().parseText();
                System.out.println("** Program starting **");
            } catch (ParseException e) {
                setState(State.IDLE);
                Window.getInstance().setEditable(true);
                Window.getInstance().handleParseException(e);
            }
        }
        if (Window.getInstance().getSimulator().isDone()) {
            setState(State.STOPPED);
            handleProgramCompletion();
        } else {
            try {
                Window.getInstance().getSimulator().executeLineFromPC();
                setState(State.PAUSED);
            } catch (SimulationException e) {
                setState(State.STOPPED);
                System.err.println(e.getMessage());
                handleProgramCompletion();
            }
        }
    }

    public static void start() {
        try {
            Window.getInstance().setEditable(false);
            Window.getInstance().parseText();
            setState(State.RUNNING);
            System.out.println("** Program starting **");
        } catch (ParseException e) {
            stop();
            Window.getInstance().handleParseException(e);
        }
    }

    public static void stop() {
        setState(State.STOPPED);
        handleProgramCompletion();
    }

    public static void pause() {
        setState(State.PAUSED);
    }

    public static void resume() {
        setState(State.RUNNING);
    }

    public static void reset() {
        handleProgramCompletion();
        setState(State.IDLE);

        //Window.getInstance().getSimulationThread().interrupt();
        //Window.getInstance().getSimulationThread().awaitTermination();

        Window.getInstance().getSimulator().resetAll();
        Window.updateAll();
    }

}
