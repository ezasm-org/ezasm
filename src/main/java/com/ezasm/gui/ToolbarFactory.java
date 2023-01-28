package com.ezasm.gui;

import com.ezasm.parsing.ParseException;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 * A toolbar factory to add to the application with simulation functionality (i.e. Run program, Run single line, etc.).
 */
public class ToolbarFactory {

    private static final String START   =  "  Start  ";
    private static final String STOP    = "   Stop   ";
    private static final String PAUSE   =  "  Pause  ";
    private static final String RESUME  = "  Resume  ";
    private static final String STEP    = "   Step   ";
    private static final String RESET   =  "  Reset  ";

    private static final ToolbarActionListener actionListener = new ToolbarActionListener();

    private static JButton startButton;
    private static JButton stopButton;
    private static JButton pauseButton;
    private static JButton resumeButton;
    private static JButton stepButton;
    private static JButton resetButton;


    /**
     * Generate the toolbar if it does not already exist and initialize its buttons.
     *
     * @return the generated or existing toolbar.
     */
    public static JToolBar makeToolbar() {
        JToolBar toolbar = new JToolBar("Simulation Toolbar");
        toolbar.setFloatable(false);

        addButton(toolbar, START);
        addButton(toolbar, STOP);
        addButton(toolbar, PAUSE);
        addButton(toolbar, RESUME);
        addButton(toolbar, STEP);
        addButton(toolbar, RESET);

        toolbar.validate();

        return toolbar;
    }

    /**
     * Sets the state of the buttons from a static context.
     * Only acts if makeToolbar() has been called prior to this.
     *
     * @param state the new state of the buttons.
     */
    public static void setButtonsEnabled(boolean state) {
        if(stepButton != null && startButton != null && pauseButton != null) {
            stepButton.setEnabled(state);
            startButton.setEnabled(state);
            pauseButton.setEnabled(false);
        }
    }

    // Helper method to automate the tasks completed for all buttons on the toolbar (disable, add action listener, etc.)
    private static void addButton(JToolBar toolbar, String text) {
        JButton button = new JButton(text);
        button.setEnabled(false);
        button.addActionListener(actionListener);
        toolbar.add(button);

        switch (text) {
            case STEP -> stepButton = button;
            case START -> startButton = button;
            case STOP -> stopButton = button;
            case PAUSE -> pauseButton = button;
            case RESUME -> resumeButton = button;
            case RESET -> resetButton = button;
        }
    }

    public static void handleProgramCompletion() {
        stepButton.setEnabled(true);
        startButton.setEnabled(true);
        stopButton.setEnabled(false);
        pauseButton.setEnabled(false);
        resumeButton.setEnabled(false);
        resetButton.setEnabled(true);

        Window.getInstance().setEditable(true);
    }

    /**
     * Helper action listener class to handle options in the toolbar.
     */
    private static class ToolbarActionListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            switch (e.getActionCommand()) {
                case STEP -> step();
                case START -> start();
                case STOP -> stop();
                case PAUSE -> pause();
                case RESUME -> resume();
                case RESET -> reset();
                default -> System.err.printf("Button '%s' not yet implemented", e.getActionCommand());
            }
        }

        private static void step() {
            if(Window.getInstance().getEditable()) {
                Window.getInstance().setEditable(false);
                try {
                    Window.getInstance().parseText();
                    System.out.println("** Program starting **");
                } catch (ParseException e) {
                    Window.getInstance().setEditable(true);
                    Window.getInstance().handleParseException(e);
                }
            }
            if(Window.getInstance().getSimulator().isDone()) {
                Window.getInstance().setEditable(true);
                Window.getInstance().handleProgramCompletion();
                return;
            }
            try {
                Window.getInstance().getSimulator().runOneLine();
                resetButton.setEnabled(true);
            } catch (ParseException e) {
                Window.getInstance().handleParseException(e);
                System.out.println("** Program terminated abnormally **");
            }
        }

        private static void start() {
            stepButton.setEnabled(false);
            startButton.setEnabled(false);
            stopButton.setEnabled(true);
            pauseButton.setEnabled(true);
            resumeButton.setEnabled(false);
            resetButton.setEnabled(true);

            // Run the content of the current file
            try {
                if(Window.getInstance().getEditable()) {
                    Window.getInstance().setEditable(false);
                    Window.getInstance().parseText();
                    System.out.println("** Program starting **");
                }
                Window.getInstance().getSimulationThread().setCompletionCallback(() -> Window.getInstance().handleProgramCompletion());
                Window.getInstance().getSimulationThread().runLinesFromPC();
            } catch (ParseException e) {
                stop();
                Window.getInstance().handleParseException(e);
            }
        }

        private static void stop() {
            // Should be started
            Window.getInstance().getSimulationThread().interrupt();
            Window.getInstance().getSimulationThread().awaitTermination();
            Window.updateAll(-1);

            handleProgramCompletion();
        }

        private static void pause() {
            // Should be STARTED
            stepButton.setEnabled(true);
            startButton.setEnabled(false);
            stopButton.setEnabled(true);
            pauseButton.setEnabled(false);
            resumeButton.setEnabled(true);
            resetButton.setEnabled(true);

            Window.getInstance().getSimulationThread().pause();
        }

        private static void resume() {
            // Should be PAUSED
            stepButton.setEnabled(true);
            startButton.setEnabled(false);
            stopButton.setEnabled(true);
            pauseButton.setEnabled(true);
            resumeButton.setEnabled(false);
            resetButton.setEnabled(true);

            Window.getInstance().getSimulationThread().resume();
        }

        private static void reset() {
            Window.getInstance().getSimulationThread().interrupt();
            Window.getInstance().getSimulationThread().awaitTermination();

            Window.getInstance().getSimulator().resetAll();
            Window.updateAll(-1);
            handleProgramCompletion();
        }

    }

}