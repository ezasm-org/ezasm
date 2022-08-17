package EzASM.gui;

import EzASM.Simulator;

import javax.swing.*;
import java.awt.*;

/**
 * The main graphical user interface of the program.
 * A singleton which holds all the necessary GUI components.
 */
public class Window {

    private static Window instance;
    private final Simulator simulator;

    private JFrame app;

    protected Window(Simulator simulator) {
        this.simulator = simulator;
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
     * Tells the caller whether the instance has been initialized.
     *
     * @return true if the instance has been initialized, false otherwise.
     */
    public static boolean hasInstance() {
        return instance != null;
    }

    /**
     * Instantiates the GUI with the given simulator.
     *
     * @param simulator the simulator to use.
     */
    public static void instantiate(Simulator simulator) {
        instance = new Window(simulator);
    }

    /**
     * Helper initialization function. Initialized UI elements.
     */
    private void initialize() {
        app = new JFrame("EzASM Simulator");
        app.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        app.setMinimumSize(new Dimension(800, 600));

        app.setJMenuBar(MenubarFactory.makeMenuBar());
        app.add(ToolbarFactory.makeToolbar(), BorderLayout.PAGE_START);

        //app.add(main content, BorderLayout.CENTER);

        app.pack();
        app.setVisible(true);
    }

    public Simulator getSimulator() {
        return simulator;
    }

}
