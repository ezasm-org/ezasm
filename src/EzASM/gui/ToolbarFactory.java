package EzASM.gui;

import EzASM.parsing.ParseException;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 * A toolbar factory to add to the application with simulation functionality (i.e. Run program, Run single line, etc.).
 */
public class ToolbarFactory {

    private static final String START   =  "  Start  ";
    private static final String PAUSE   =  "  Pause  ";
    private static final String RESUME  = "  Resume  ";
    private static final String STEP    = "   Step   ";
    private static final String RESTART = " Restart ";

    private static final ToolbarActionListener actionListener = new ToolbarActionListener();

    private static JButton startButton;
    private static JButton pauseButton;
    private static JButton resumeButton;
    private static JButton stepButton;
    private static JButton restartButton;


    /**
     * Generate the toolbar if it does not already exist and initialize its buttons.
     *
     * @return the generated or existing toolbar.
     */
    public static JToolBar makeToolbar() {
        JToolBar toolbar = new JToolBar("Simulation Toolbar");
        toolbar.setFloatable(false);

        addButton(toolbar, START);
        addButton(toolbar, PAUSE);
        addButton(toolbar, RESUME);
        addButton(toolbar, STEP);

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
            case PAUSE -> pauseButton = button;
            case RESUME -> resumeButton = button;
            case RESTART -> restartButton = button;
        }
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
                case PAUSE -> pause();
                case RESUME -> resume();
                case RESTART -> restart();
                default -> System.err.printf("Button '%s' not yet implemented", e.getActionCommand());
            }
        }

        private static void step() {
            try {
                Window.getInstance().getSimulator().runOneLine();
            } catch (ParseException ex) {
                // TODO handle
                throw new RuntimeException(ex);
            }
        }

        private static void start() {
            stepButton.setEnabled(false);
            startButton.setEnabled(false);
            pauseButton.setEnabled(true);
            resumeButton.setEnabled(false);

            // TODO parse and start the simulation
            // Run the content of the current file
        }

        private static void pause() {
            // Should be STARTED
            stepButton.setEnabled(true);
            startButton.setEnabled(false);
            pauseButton.setEnabled(false);
            resumeButton.setEnabled(true);
            // TODO pause the simulation
        }

        private static void resume() {
            // Should be PAUSED
            stepButton.setEnabled(true);
            startButton.setEnabled(false);
            pauseButton.setEnabled(true);
            resumeButton.setEnabled(false);
            // TODO pause the simulation
        }

        private static void restart() {
            // TODO dispose of currently running code

            start();
        }
    }

}