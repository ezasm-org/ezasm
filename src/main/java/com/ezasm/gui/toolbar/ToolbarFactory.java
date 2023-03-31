package com.ezasm.gui.toolbar;

import com.ezasm.gui.util.EditorTheme;
import com.ezasm.util.SystemStreams;

import static com.ezasm.gui.toolbar.SimulatorGuiActions.*;

import javax.swing.*;
import javax.swing.border.Border;

import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 * A toolbar factory to add to the application with simulation functionality (i.e. Run program, Run single line, etc.).
 */
public class ToolbarFactory {

    static final String START = "  Start  ";
    static final String STOP = "   Stop   ";
    static final String PAUSE = "  Pause  ";
    static final String RESUME = "  Resume  ";
    static final String STEP = "   Step   ";
    static final String STEP_BACK = " Step Back ";
    static final String RESET = "  Reset  ";

    private static final ToolbarActionListener actionListener = new ToolbarActionListener();

    static JButton startButton;
    static JButton stopButton;
    static JButton pauseButton;
    static JButton resumeButton;
    static JButton stepButton;
    static JButton stepBackButton;
    static JButton resetButton;

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
        addButton(toolbar, STEP_BACK);
        addButton(toolbar, RESET);

        toolbar.validate();

        return toolbar;
    }

    /**
     * Applies the proper theming to the toolbar
     */
    public static void applyTheme(Font font, EditorTheme editorTheme, JToolBar toolbar) {
        Border border = BorderFactory.createMatteBorder(1, 1, 1, 1, editorTheme.foreground());
        Border buttonBorder = BorderFactory.createMatteBorder(0, 0, 0, 1, editorTheme.foreground());
        EditorTheme.applyFontThemeBorder(toolbar, font, editorTheme, border);
        EditorTheme.applyFontThemeBorder(startButton, font, editorTheme, buttonBorder);
        EditorTheme.applyFontThemeBorder(stopButton, font, editorTheme, buttonBorder);
        EditorTheme.applyFontThemeBorder(pauseButton, font, editorTheme, buttonBorder);
        EditorTheme.applyFontThemeBorder(resumeButton, font, editorTheme, buttonBorder);
        EditorTheme.applyFontThemeBorder(stepButton, font, editorTheme, buttonBorder);
        EditorTheme.applyFontThemeBorder(stepBackButton, font, editorTheme, buttonBorder);
        EditorTheme.applyFontThemeBorder(resetButton, font, editorTheme, buttonBorder);
    }

    /**
     * Sets the state of the buttons from a static context. Only acts if makeToolbar() has been called prior to this.
     *
     * @param state the new state of the buttons.
     */
    public static void setButtonsEnabled(boolean state) {
        if (stepButton != null && startButton != null && pauseButton != null) {
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
        case STEP_BACK -> stepBackButton = button;
        case START -> startButton = button;
        case STOP -> stopButton = button;
        case PAUSE -> pauseButton = button;
        case RESUME -> resumeButton = button;
        case RESET -> resetButton = button;
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
            case STEP_BACK -> stepBack();
            case START -> start();
            case STOP -> stop();
            case PAUSE -> pause();
            case RESUME -> resume();
            case RESET -> reset();
            default -> SystemStreams.err.printf("Button '%s' not yet implemented\n", e.getActionCommand());
            }
        }

    }
}
