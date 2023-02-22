package com.ezasm.gui;

import com.ezasm.gui.editor.EditorPane;
import com.ezasm.gui.menubar.MenubarFactory;
import com.ezasm.gui.toolbar.SimulatorGUIActions;
import com.ezasm.gui.toolbar.ToolbarFactory;
import com.ezasm.gui.settings.Config;
import com.ezasm.gui.util.Theme;
import com.ezasm.instructions.implementation.TerminalInstructions;
import com.ezasm.parsing.Lexer;
import com.ezasm.simulation.ISimulator;
import com.ezasm.parsing.ParseException;
import com.ezasm.simulation.Registers;
import com.ezasm.util.FileReader;

import javax.swing.*;
import java.awt.*;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import static com.ezasm.gui.util.DialogFactory.promptWarningDialog;

/**
 * The main graphical user interface of the program. A singleton which holds all the necessary GUI components and one
 * relevant simulator.
 */
public class Window {

    private static Window instance;
    private final ISimulator simulator;

    private Config config;
    private JFrame app;
    private JPanel panel;
    private JToolBar toolbar;
    private JMenuBar menubar;
    private EditorPane editor;
    private RegisterTable table;

    private String loadedFile;
    private String inputFilePath, outputFilePath;
    private InputStream inputStream = TerminalInstructions.DEFAULT_INPUT_STREAM;
    private OutputStream outputStream = TerminalInstructions.DEFAULT_OUTPUT_STREAM;

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
     * Generate the singleton Window instance if it does not exist. Sets the input/output streams for our
     * TerminalInstructions to files
     *
     * @param simulator      the simulator to use.
     * @param config         the program configuration.
     * @param inputFilePath  the desired file to use for the InputStream.
     * @param outputFilePath the desired file to use for the OutputStream.
     */
    public static void instantiate(ISimulator simulator, Config config, String inputFilePath, String outputFilePath) {
        if (instance == null) {
            new Window(simulator, config);
            setInputStream(new File(inputFilePath));
            setOutputStream(new File(outputFilePath));
        }
    }

    /**
     * Sets the input stream for our TerminalInstructions to files
     *
     * @param inputFile the desired file to use for the InputStream.
     */
    public static void setInputStream(File inputFile) {
        try {
            instance.inputStream = new FileReader(inputFile);
            instance.inputFilePath = inputFile.getPath();

        } catch (IOException e) {
            promptWarningDialog("Error Reading File",
                    String.format("There was an error reading from '%s'\nOperation cancelled", inputFile.getName()));
        }
        TerminalInstructions.streams().setInputStream(instance.inputStream);
    }

    /**
     * Sets the input stream for our TerminalInstructions to files
     *
     * @param outputFile the desired file to use for the InputStream.
     */
    public static void setOutputStream(File outputFile) {
        try {
            outputFile.createNewFile();
            instance.outputStream = new FileOutputStream(outputFile);
            instance.outputFilePath = outputFile.getPath();
        } catch (IOException e) {
            promptWarningDialog("Error Writing File",
                    String.format("There was an error writing to '%s'\nOperation cancelled", outputFile.getName()));
        }
        TerminalInstructions.streams().setOutputStream(instance.outputStream);
    }

    public static String getInputFilePath() {
        return instance.inputFilePath;
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
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (Exception e) {
            System.err.println("Unable to set look and feel");
        }

        app = new JFrame("EzASM Simulator");
        app.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        app.setMinimumSize(new Dimension(800, 600));
        panel = new JPanel();

        menubar = MenubarFactory.makeMenuBar();
        toolbar = ToolbarFactory.makeToolbar();
        editor = new EditorPane();
        table = new RegisterTable(simulator.getRegisters());

        app.setJMenuBar(menubar);
        panel.setLayout(new BorderLayout());
        panel.add(toolbar, BorderLayout.PAGE_START);
        panel.add(editor, BorderLayout.CENTER);
        panel.add(table, BorderLayout.EAST);

        ToolbarFactory.setButtonsEnabled(true);

        applyConfiguration(config);

        app.getContentPane().add(panel);
        app.validate();
        app.pack();
        app.setVisible(true);
    }

    public void applyConfiguration(Config config) {
        Theme theme = Theme.getTheme(config.getTheme());
        Font font = new Font(Config.DEFAULT_FONT, Font.PLAIN, config.getFontSize());

        panel.setBackground(theme.background());
        table.applyTheme(font, theme);
        ToolbarFactory.applyTheme(font, theme, toolbar);
        editor.applyTheme(font, theme);
        SimulatorGUIActions.setInstructionDelayMS(config.getSimSpeed());
        this.config = config;
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
    public static void updateRegisters() {
        if (instance == null || instance.table == null) {
            return;
        }
        instance.table.update();
    }

    /**
     * Update tells the EditorPane to update the line highlighter.
     */
    public static void updateHighlight() {
        if (instance == null || instance.editor == null) {
            return;
        }
        instance.editor.updateHighlight();
    }

    /**
     * Parses the current text content of the editor pane.
     *
     * @throws ParseException if there are any errors lexing the given text.
     */
    public void parseText() throws ParseException {
        simulator.resetAll();
        updateRegisters();
        simulator.addLines(Lexer.parseLines(editor.getText()));
        instance.editor.resetHighlighter();
    }

    /**
     * Handles the program completion and displays a message to the user about the status of the program.
     */
    public void handleProgramCompletion() {
        if (simulator.isError()) {
            System.out.println("** Program terminated due to an error **");
        } else if (simulator.isDone()) {
            System.out.printf("** Program terminated with exit code %d **\n",
                    simulator.getRegisters().getRegister(Registers.R0).getLong());
        } else {
            System.out.println("** Program terminated forcefully **");
        }
        editor.resetHighlighter();
    }

    /**
     * Sets the currently viewed file in the window.
     *
     * @param filePath the path of the file to set.
     */
    public void setLoadedFile(String filePath) {
        loadedFile = filePath;
    }

    /**
     * Gets the path to the file loaded in the window.
     */
    public String getLoadedFile() {
        if (loadedFile != null) {
            return loadedFile;
        } else {
            return "";
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

    /**
     * Returns the current theme being used.
     *
     * @return The current theme object
     */
    public static Theme currentTheme() {
        if (instance == null) {
            return null;
        }
        return Theme.getTheme(getInstance().config.getTheme());
    }

    /**
     * Resets the editor highlighter, updating color to current theme and clearing all highlights
     */
    public static void resetHighlight() {
        if (!Window.hasInstance()) {
            return;
        }
        Window.getInstance().editor.resetHighlighter();
    }
}
