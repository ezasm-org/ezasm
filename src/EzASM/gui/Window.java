package EzASM.gui;

import EzASM.SimulationThread;
import EzASM.Simulator;
import EzASM.parsing.ParseException;

import javax.swing.*;
import java.awt.*;

/**
 * The main graphical user interface of the program.
 * A singleton which holds all the necessary GUI components and one relevant simulator.
 */
public class Window {

    private static Window instance;
    private Simulator simulator;
    private SimulationThread simulationThread;

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
     * Just return the existing instance otherwise.
     *
     * @return the instance.
     */
    public static Window getInstance() {
        if(instance == null) return null;
        return instance;
    }

    /**
     * Generate the singleton instance if it does not exist.
     * Just return the existing instance otherwise.
     *
     * @param simulator the simulator to use.
     * @return the instance.
     */
    public static Window getInstance(Simulator simulator) {
        if(instance == null) new Window(simulator);
        return instance;
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

        app.validate();
        app.pack();
        app.setVisible(true);
    }

    /**
     * Sets the simulator used by the GUI if ever needed.
     *
     * @param simulator the simulator to use.
     */
    public void setSimulator(Simulator simulator) {
        this.simulator = simulator;
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
     * Updates all UI elements if they exist.
     */
    public static void updateAll() {
        if(instance == null || instance.table == null) return;
        instance.table.update();
    }

    public void parseText() throws ParseException {
        simulator.resetAll();
        updateAll();
        simulator.readMultiLineString(editor.getText());
    }

    public SimulationThread getSimulationThread() {
        return simulationThread;
    }

    public void setEditable(boolean value) {
        editor.setEditable(value);
    }

    public boolean getEditable() {
        return editor.getEditable();
    }

    public void handleProgramCompletion() {
        ToolbarFactory.handleProgramCompletion();
        System.out.println("** Program finished **");
        editor.setEditable(true);
    }

    public void setText(String content) {
        editor.setText(content);
    }

    public String getText() {
        return editor.getText();
    }
}
