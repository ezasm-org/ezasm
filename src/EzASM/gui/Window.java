package EzASM.gui;

import EzASM.simulation.SimulationThread;
import EzASM.simulation.Simulator;
import EzASM.parsing.ParseException;

import javax.swing.*;
import java.awt.*;

/**
 * The main graphical user interface of the program.
 * A singleton which holds all the necessary GUI components and one relevant simulator.
 */
public class Window {

    private static Window instance;
    private final Simulator simulator;
    private final SimulationThread simulationThread;

    private JFrame app;
    private JToolBar toolbar;
    private JMenuBar menubar;
    private EditorPane editor;
    private RegisterTable table;

    protected Window(Simulator simulator) {
        instance = this;
        this.simulator = simulator;
        this.simulationThread = new SimulationThread(this.simulator);
        initialize();
    }

    /**
     * Generate the singleton instance if it does not exist.
     * Just returns the existing instance otherwise.
     * Returns null if a simulator has not yet been provided.
     * @return the instance.
     */
    public static Window getInstance() {
        if(instance == null) return null;
        return instance;
    }

    /**
     * Generate the singleton instance if it does not exist.
     * Just returns the existing instance otherwise.
     * @param simulator the simulator to use.
     * @return the instance.
     */
    public static Window getInstance(Simulator simulator) {
        if(instance == null) new Window(simulator);
        return instance;
    }

    /**
     * Tells the caller whether the instance has been initialized.
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

        app.validate();
        app.pack();
        app.setVisible(true);
    }

    /**
     * Returns the current simulator in use.
     * @return the current simulator in use.
     */
    public Simulator getSimulator() {
        return simulator;
    }

    /**
     * Updates all UI elements if they exist.
     */
    public static void updateAll() {
        if(instance == null || instance.table == null) return;
        instance.table.update();
    }

    /**
     * Parses the current text content of the editor pane.
     * @throws ParseException if there are any errors lexing the given text.
     */
    public void parseText() throws ParseException {
        simulator.resetAll();
        updateAll();
        simulator.readMultiLineString(editor.getText());
    }

    /**
     * Gets the instance's simulation thread for use in staring the async execution of the
     * simulator's instructions.
     * @return the SimulationThread instance relevant.
     */
    public SimulationThread getSimulationThread() {
        return simulationThread;
    }

    /**
     * Handles the program completion and displays a message to the user about the status of the program.
     */
    public void handleProgramCompletion() {
        ToolbarFactory.handleProgramCompletion();
        if(simulator.isErrored()) {
            System.out.println("** Program terminated due to an error **");
        } else if(simulator.isDone()) {
            System.out.println("** Program terminated normally **");
        } else {
            System.out.println("** Program terminated forcefully **");
        }
    }

    /**
     * Sets the text of the editor to the given content.
     * @param content the text to set the text within the editor to.
     */
    public void setText(String content) {
        editor.setText(content);
    }

    /**
     * Gets the text content of the text editor.
     * @return the text content of the text editor.
     */
    public String getText() {
        return editor.getText();
    }

    /**
     * Enable or disable the ability of the user to edit the text pane.
     * Text cannot be selected while this is the set to false.
     * @param value true to enable, false to disable.
     */
    public void setEditable(boolean value) {
        editor.setEditable(value);
    }

    /**
     * Gets the truth value of whether the editor can be typed in.
     * @return true if the editor can be typed in currently, false otherwise.
     */
    public boolean getEditable() {
        return editor.getEditable();
    }
}
