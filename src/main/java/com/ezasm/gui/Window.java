package com.ezasm.gui;

import com.ezasm.gui.console.Console;
import com.ezasm.gui.editor.EzEditorPane;
import com.ezasm.gui.menubar.MenuActions;
import com.ezasm.gui.menubar.MenubarFactory;
import com.ezasm.gui.toolbar.SimulatorGuiActions;
import com.ezasm.gui.toolbar.ToolbarFactory;
import com.ezasm.gui.tabbedpane.ClosableTabPanel;
import com.ezasm.gui.tabbedpane.ClosableTabbedPane;
import com.ezasm.gui.tabbedpane.FixedTabbedPane;
import com.ezasm.gui.settings.Config;
import com.ezasm.gui.util.EditorTheme;
import com.ezasm.instructions.implementation.TerminalInstructions;
import com.ezasm.parsing.Lexer;
import com.ezasm.simulation.Simulator;
import com.ezasm.parsing.ParseException;
import com.ezasm.simulation.Registers;
import com.ezasm.util.FileIO;
import com.ezasm.util.RandomAccessFileStream;

import javax.swing.*;
import javax.swing.plaf.basic.BasicSplitPaneUI;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.PrintStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.ListIterator;

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
    private ClosableTabbedPane editors;
    private RegisterTable registerTable;
    private FixedTabbedPane tools;
    private Console console;

    private static boolean debugMode;
    private ArrayList<EzEditorPane> editorPanes;

    private JSplitPane mainSplit;
    private JSplitPane toolSplit;

    private InputStream inputStream = TerminalInstructions.DEFAULT_INPUT_STREAM;
    private OutputStream outputStream = TerminalInstructions.DEFAULT_OUTPUT_STREAM;

    private ActionMap actionMap;
    private InputMap inputMap;

    private final KeyStroke saveKeyStroke = KeyStroke.getKeyStroke(KeyEvent.VK_S, KeyEvent.CTRL_DOWN_MASK);
    private final KeyStroke saveAsKeyStroke = KeyStroke.getKeyStroke(KeyEvent.VK_S,
            KeyEvent.CTRL_DOWN_MASK | KeyEvent.SHIFT_DOWN_MASK);
    private final KeyStroke openKeyStroke = KeyStroke.getKeyStroke(KeyEvent.VK_O, KeyEvent.CTRL_DOWN_MASK);
    private final KeyStroke loadInputKeyStroke = KeyStroke.getKeyStroke(KeyEvent.VK_I,
            KeyEvent.CTRL_DOWN_MASK | KeyEvent.SHIFT_DOWN_MASK);
    private final KeyStroke loadOutputKeyStroke = KeyStroke.getKeyStroke(KeyEvent.VK_O,
            KeyEvent.CTRL_DOWN_MASK | KeyEvent.SHIFT_DOWN_MASK);

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
    public static void instantiate(Simulator simulator, Config config, boolean dbg) {
        debugMode = dbg;
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
    public static void instantiate(Simulator simulator, Config config, boolean dbg, String inputFilePath,
            String outputFilePath) {
        debugMode = dbg;
        if (instance == null) {
            new Window(simulator, config);
            instance.setFileInputStream(new File(inputFilePath));
            instance.setFileOutputStream(new File(outputFilePath));
        }
    }

    /**
     * Sets the input stream for our TerminalInstructions to files.
     *
     * @param inputFile the desired file to use for the InputStream.
     */
    public void setFileInputStream(File inputFile) {
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
    public void setFileOutputStream(File outputFile) {
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
     * Sets the input stream for program output to the given input stream.
     *
     * @param inputStream the output stream to read from.
     */
    public void setInputStream(InputStream inputStream) {
        this.inputStream = inputStream;
        TerminalInstructions.streams().setInputStream(inputStream);
    }

    /**
     * Sets the output stream for program output to the given output stream.
     *
     * @param outputStream the output stream to write to.
     */
    public void setOutputStream(OutputStream outputStream) {
        this.outputStream = outputStream;
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

        editorPanes = new ArrayList<>();

        app = new JFrame("EzASM Simulator");
        app.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
        app.addWindowListener(new WindowCloseListener());
        app.setMinimumSize(new Dimension(800, 600));
        try {
            app.setIconImage(FileIO.loadImage("icons/logo/EzASM.png"));
        } catch (IOException e) {
            System.err.println("Could not load icon");
        }
        panel = new JPanel();

        menubar = MenubarFactory.makeMenuBar();
        editors = new ClosableTabbedPane();
        EzEditorPane newEditor = new EzEditorPane();
        editors.addTab(newEditor, null, "New Document.ez", "");
        editorPanes.add(newEditor);
        toolbar = ToolbarFactory.makeToolbar();
        registerTable = new RegisterTable(simulator.getRegisters());

        console = new Console();
        setInputStream(console.getInputStream());
        setOutputStream(console.getOutputStream());
        // TfileNameODO maybe make this configurable to allow them to use their terminal which they ran this with if
        // they want
        System.setIn(inputStream);
        System.setOut(new PrintStream(outputStream));
        if (!debugMode) {
            System.setErr(new PrintStream(console.getErrorStream()));
        }

        tools = new FixedTabbedPane();
        tools.addTab(console, null, "Console", "Your Console");

        mainSplit = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, editors, registerTable);
        mainSplit.setResizeWeight(0.8);
        mainSplit.setUI(new BasicSplitPaneUI());
        mainSplit.setBorder(null);
        toolSplit = new JSplitPane(JSplitPane.VERTICAL_SPLIT, mainSplit, tools);
        toolSplit.setResizeWeight(0.75);
        toolSplit.setUI(new BasicSplitPaneUI());
        toolSplit.setBorder(null);

        app.setJMenuBar(menubar);
        panel.setLayout(new BorderLayout());
        panel.add(toolbar, BorderLayout.PAGE_START);
        panel.add(toolSplit, BorderLayout.CENTER);

        ToolbarFactory.setButtonsEnabled(true);

        applyConfiguration(config);

        app.getContentPane().add(panel);
        app.validate();
        app.pack();
        app.setVisible(true);

        inputMap = app.getRootPane().getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW);
        actionMap = app.getRootPane().getActionMap();

        registerKeystroke("saveAction", saveKeyStroke, MenuActions::save);
        registerKeystroke("saveAsAction", saveAsKeyStroke, MenuActions::saveAs);
        registerKeystroke("openAction", openKeyStroke, MenuActions::load);
        registerKeystroke("loadInputAction", loadInputKeyStroke, MenuActions::selectInputFile);
        registerKeystroke("loadOutputAction", loadOutputKeyStroke, MenuActions::selectOutputFile);
    }

    /**
     * Registers a hotkey and the associated action
     *
     * @param actionName name of action (internal only)
     * @param mnemonic   the KeyStroke in question
     * @param action     the action to perform
     */
    private void registerKeystroke(String actionName, KeyStroke mnemonic, Runnable action) {
        inputMap.put(mnemonic, actionName);
        actionMap.put(actionName, new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                action.run();
            }
        });
    }

    public EzEditorPane openNewFileTab(File fileIn) {
        EzEditorPane newEditor;
        int k = getEditorIndexOfOpenPath(fileIn.getPath());
        if (k == -1) {
            newEditor = new EzEditorPane();
            editorPanes.add(newEditor);
            editors.addTab(newEditor, null, fileIn.getName(), "");
            applyConfiguration(config);
        } else {
            if (!(editors.getComponentAt(k) instanceof EzEditorPane)) {
                return null;
            }
            newEditor = (EzEditorPane) editors.getComponentAt(k);
        }
        editors.setActiveTab(newEditor);
        return newEditor;
    }

    public ClosableTabbedPane getEditorPanes() {
        return editors;
    }

    public void applyConfiguration(Config config) {
        this.config = config;
        EditorTheme editorTheme = EditorTheme.getTheme(config.getTheme());
        Font font = new Font(Config.DEFAULT_FONT, Font.PLAIN, config.getFontSize());

        tools.applyTheme(font, editorTheme);
        mainSplit.setBackground(editorTheme.background());
        panel.setBackground(editorTheme.background());
        registerTable.applyTheme(font, editorTheme);
        ToolbarFactory.applyTheme(font, editorTheme, toolbar);
        editors.applyTheme(font, editorTheme);
        for (Component jc : getEditors()) {
            if (jc instanceof EzEditorPane) {
                EzEditorPane ez = (EzEditorPane) jc;
                ez.resizeTabSize(config.getTabSize());
            }
        }
        SimulatorGuiActions.setInstructionDelayMS(config.getSimSpeed());
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
     * Gets the theme stored in the instance configuration.
     *
     * @return the theme stored in the instance configuration.
     */
    public EditorTheme getTheme() {
        return EditorTheme.getTheme(config.getTheme());
    }

    /**
     * Gets this instance's console object.
     *
     * @return the instance's console object.
     */
    public Console getConsole() {
        return this.console;
    }

    /**
     * Gets the instance's editor pane.
     *
     * @return the instance's editor pane.
     */
    public EzEditorPane getEditor() {
        JComponent jc = editors.getSelectedComponent();
        if (jc instanceof EzEditorPane) {
            return (EzEditorPane) jc;
        }
        return null;
    }

    /**
     * Gets the instance's editor panes.
     *
     * @return the instance's editor panes.
     */
    public ArrayList<EzEditorPane> getEditors() {
        return editorPanes;
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
        simulator.addLines(Lexer.parseLines(getEditor().getText()), new File(getEditor().getOpenFilePath()));
        instance.getEditor().resetHighlighter();
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
        getEditor().resetHighlighter();
    }

    /**
     * Sets the text of the editor to the given content.
     *
     * @param content the text to set the text within the editor to.
     */
    public void setText(String content) {
        getEditor().setText(content);
    }

    /**
     * Gets the text content of the text editor.
     *
     * @return the text content of the text editor.
     */
    public String getText() {
        return getEditor().getText();
    }

    /**
     * Enable or disable the ability of the user to edit the text pane. Text cannot be selected while this is the set to
     * false.
     *
     * @param value true to enable, false to disable.
     */
    public void setEditable(boolean value) {
        getEditor().setEditable(value);
    }

    /**
     * Gets the truth value of whether the editor can be typed in.
     *
     * @return true if the editor can be typed in currently, false otherwise.
     */
    public boolean getEditable() {
        return getEditor().getEditable();
    }

    /**
     * Handles the parse exception by printing the message to the terminal.
     *
     * @param e the exception to handle.
     */
    public void handleParseException(Exception e) {
        System.err.println(e.getMessage());
    }

    public int getEditorIndexOfOpenPath(String path) {
        ArrayList<EzEditorPane> panes = instance.getEditors();
        for (int i = 0; i < panes.size(); i++) {
            if (panes.get(i).getOpenFilePath().equals(path)) {
                return i;
            }
        }
        return -1;
    }

}
