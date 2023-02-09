package com.ezasm.gui;

import com.ezasm.instructions.impl.TerminalInstructions;
import com.ezasm.parsing.Lexer;
import com.ezasm.simulation.ISimulator;
import com.ezasm.Config;
import com.ezasm.Theme;
import com.ezasm.parsing.ParseException;

import javax.swing.*;
import java.awt.*;

/**
 * The main graphical user interface of the program. A singleton which holds all the necessary GUI components and one
 * relevant simulator.
 */
public class Window {

    private static Window instance;
    private final ISimulator simulator;

    private Config config;
    private JFrame app;
    private JToolBar toolbar;
    private JMenuBar menubar;
    private EditorPane editor;
    private RegisterTable table;

    protected Window(ISimulator simulator, Config config) {
        instance = this;
        this.simulator = simulator;
        this.config = config;
        initialize();
    }

    /**
     * Return the existing instance. Returns null if a simulator has not yet been provided through
     * <code>instantiate</code>.
     *
     * @return the instance.
     */
    public static Window getInstance() {
        return instance;
    }

    /**
     * Generate the singleton Window instance if it does not exist.
     *
     * @param simulator the simulator to use.
     * @param config    the program configuration.
     */
    public static void instantiate(ISimulator simulator, Config config) {
        if (instance == null)
            new Window(simulator, config);
    }

    /**
     * Tells the caller whether the instance has been initialized.
     *
     * @return true if the instance has been initialized, false otherwise.
     */
    public static boolean hasInstance() {
        return instance != null;
    }

    /**
     * Helper initialization function. Initialized UI elements.
     */
    private void initialize() {
        app = new JFrame("EzASM Simulator");
        app.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        app.setMinimumSize(new Dimension(800, 600));

        menubar = MenubarFactory.makeMenuBar();
        toolbar = ToolbarFactory.makeToolbar();
        editor = new EditorPane();
        table = new RegisterTable(simulator.getRegisters());

        app.setJMenuBar(menubar);
        app.add(toolbar, BorderLayout.PAGE_START);
        app.add(editor, BorderLayout.CENTER);
        app.add(table, BorderLayout.EAST);

        ToolbarFactory.setButtonsEnabled(true);

        applyConfiguration(config);

        app.validate();
        app.pack();
        app.setVisible(true);
    }

    public void applyConfiguration(Config config) {
        Theme theme = switch (config.getTheme()) {
        case "Dark" -> Theme.Dracula;
        case "Purple" -> Theme.Purple;
        case "Light" -> Theme.Light;
        default -> Theme.Light;
        };
        app.getContentPane().setBackground(theme.getBackground());
        Font font = new Font(Config.DEFAULT_FONT, Font.PLAIN, config.getFontSize());
        table.applyTheme(font, theme);
        ToolbarFactory.applyTheme(font, theme, toolbar);
        editor.applyTheme(font, theme);
        SimulatorGUIActions.setInstructionDelayMS(config.getSimSpeed());
    }

    /**
     * Returns the current simulator in use.
     *
     * @return the current simulator in use.
     */
    public ISimulator getSimulator() {
        return simulator;
    }

    /**
     * Updates all UI elements if they exist.
     */
    public static void updateAll() {
        if (instance == null || instance.table == null)
            return;
        SwingUtilities.invokeLater(() -> {
            instance.table.update();
        });

    }

    /**
     * Parses the current text content of the editor pane.
     *
     * @throws ParseException if there are any errors lexing the given text.
     */
    public void parseText() throws ParseException {
        simulator.resetAll();
        updateAll();
        simulator.addLines(Lexer.parseLines(editor.getText(), 0));
    }

    /**
     * Handles the program completion and displays a message to the user about the status of the program.
     */
    public void handleProgramCompletion() {
        TerminalInstructions.clearBuffer();
        System.out.println();
        if (simulator.isError()) {
            System.out.println("** Program terminated due to an error **");
        } else if (simulator.isDone()) {
            System.out.println("** Program terminated normally **");
        } else {
            System.out.println("** Program terminated forcefully **");
        }
    }

    /**
     * Sets the text of the editor to the given content.
     *
     * @param content the text to set the text within the editor to.
     */
    public void setText(String content) {
        editor.setText(content);
    }

    /**
     * Gets the text content of the text editor.
     *
     * @return the text content of the text editor.
     */
    public String getText() {
        return editor.getText();
    }

    /**
     * Enable or disable the ability of the user to edit the text pane. Text cannot be selected while this is the set to
     * false.
     *
     * @param value true to enable, false to disable.
     */
    public void setEditable(boolean value) {
        editor.setEditable(value);
    }

    /**
     * Gets the truth value of whether the editor can be typed in.
     *
     * @return true if the editor can be typed in currently, false otherwise.
     */
    public boolean getEditable() {
        return editor.getEditable();
    }

    /**
     * Handles the parse exception by printing the message to the terminal.
     *
     * @param e the exception to handle.
     */
    public void handleParseException(Exception e) {
        System.err.println(e.getMessage());
    }
}
