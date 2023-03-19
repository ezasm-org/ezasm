package com.ezasm.gui;

import com.ezasm.gui.editor.EditorPane;
import com.ezasm.gui.menubar.MenuActions;
import com.ezasm.gui.menubar.MenubarFactory;
import com.ezasm.gui.toolbar.SimulatorGUIActions;
import com.ezasm.gui.toolbar.ToolbarFactory;
import com.ezasm.gui.settings.Config;
import com.ezasm.gui.util.DialogFactory;
import com.ezasm.gui.util.Theme;
import com.ezasm.instructions.implementation.TerminalInstructions;
import com.ezasm.parsing.Lexer;
import com.ezasm.simulation.Simulator;
import com.ezasm.parsing.ParseException;
import com.ezasm.simulation.Registers;
import com.ezasm.util.FileIO;
import com.ezasm.util.RandomAccessFileStream;

import javax.swing.*;

import java.awt.*;
import java.io.File;
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
    private final Simulator simulator;

    private Config config;
    private JFrame app;
    private JPanel panel;
    private JToolBar toolbar;
    private JMenuBar menubar;
    private EditorPane editor;
    private RegisterTable registerTable;

    private InputStream inputStream = TerminalInstructions.DEFAULT_INPUT_STREAM;
    private OutputStream outputStream = TerminalInstructions.DEFAULT_OUTPUT_STREAM;

    protected Window(Simulator simulator, Config config) {
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
    public static void instantiate(Simulator simulator, Config config) {
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
    public static void instantiate(Simulator simulator, Config config, String inputFilePath, String outputFilePath) {
        if (instance == null) {
            new Window(simulator, config);
            instance.setInputStream(new File(inputFilePath));
            instance.setOutputStream(new File(outputFilePath));
        }
    }

    /**
     * Sets the input stream for our TerminalInstructions to files.
     *
     * @param inputFile the desired file to use for the InputStream.
     */
    public void setInputStream(File inputFile) {
        try {
            inputStream = new RandomAccessFileStream(inputFile);
        } catch (IOException e) {
            promptWarningDialog("Error Reading File",
                    String.format("There was an error reading from '%s'\nOperation cancelled", inputFile.getName()));
        }
        TerminalInstructions.streams().setInputStream(inputStream);
    }

    /**
     * Sets the input stream for our TerminalInstructions to files.
     *
     * @param outputFile the desired file to use for the InputStream.
     */
    public void setOutputStream(File outputFile) {
        try {
            outputFile.createNewFile();
            outputStream = new FileOutputStream(outputFile);
        } catch (IOException e) {
            promptWarningDialog("Error Writing File",
                    String.format("There was an error writing to '%s'\nOperation cancelled", outputFile.getName()));
        }
        TerminalInstructions.streams().setOutputStream(outputStream);
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
        try {
            app.setIconImage(FileIO.loadImage("icons/logo/EzASM.png"));
        } catch (IOException e) {
            System.err.println("Could not load icon");
        }
        panel = new JPanel();

        menubar = MenubarFactory.makeMenuBar();
        toolbar = ToolbarFactory.makeToolbar();
        editor = new EditorPane();
        registerTable = new RegisterTable(simulator.getRegisters());

        app.setJMenuBar(menubar);
        panel.setLayout(new BorderLayout());
        panel.add(toolbar, BorderLayout.PAGE_START);
        panel.add(editor, BorderLayout.CENTER);
        panel.add(registerTable, BorderLayout.EAST);

        ToolbarFactory.setButtonsEnabled(true);

        applyConfiguration(config);

        app.getContentPane().add(panel);
        app.validate();
        app.pack();
        app.setVisible(true);
    }

    public void applyConfiguration(Config config) {
        this.config = config;
        Theme theme = Theme.getTheme(config.getTheme());
        Font font = new Font(Config.DEFAULT_FONT, Font.PLAIN, config.getFontSize());

        panel.setBackground(theme.background());
        registerTable.applyTheme(font, theme);
        ToolbarFactory.applyTheme(font, theme, toolbar);
        editor.applyTheme(font, theme);
        SimulatorGUIActions.setInstructionDelayMS(config.getSimSpeed());
    }

    /**
     * Gets the instance's configuration object.
     *
     * @return the instance's configuration object.
     */
    public Config getConfig() {
        return config;
    }

    /**
     * Gets the instance's editor pane.
     *
     * @return the instance's editor pane.
     */
    public EditorPane getEditor() {
        return editor;
    }

    /**
     * Gets the instance's editor pane.
     *
     * @return the instance's editor pane.
     */
    public RegisterTable getRegisterTable() {
        return registerTable;
    }

    /**
     * Returns the current simulator in use.
     *
     * @return the current simulator in use.
     */
    public Simulator getSimulator() {
        return simulator;
    }

    /**
     * Parses the current text content of the editor pane.
     *
     * @throws ParseException if there are any errors lexing the given text.
     */
    public void parseText() throws ParseException {
        simulator.resetAll();
        registerTable.update();
        simulator.addLines(Lexer.parseLines(editor.getText()), new File(editor.getOpenFilePath()));
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
     * Handles the parse exception by printing the message to the terminal.
     *
     * @param e the exception to handle.
     */
    public void handleParseException(Exception e) {
        System.err.println(e.getMessage());
    }
}
