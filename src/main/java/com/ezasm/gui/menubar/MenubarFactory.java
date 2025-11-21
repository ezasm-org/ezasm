package com.ezasm.gui.menubar;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;

import static com.ezasm.gui.menubar.MenuActions.load;
import static com.ezasm.gui.menubar.MenuActions.newFile;
import static com.ezasm.gui.menubar.MenuActions.save;
import static com.ezasm.gui.menubar.MenuActions.saveAs;
import static com.ezasm.gui.menubar.MenuActions.selectInputFile;
import static com.ezasm.gui.menubar.MenuActions.selectOutputFile;
import com.ezasm.gui.settings.AboutPopup;
import com.ezasm.gui.settings.SettingsPopup;
import com.ezasm.instructions.implementation.TerminalInstructions;
import com.ezasm.util.SystemStreams;

/**
 * A factory which generates and manages the application's menu bar. Provides a singleton menu bar with File operations
 * (new, open, save, exit), Settings access, and IO redirection controls. Uses a centralized action listener to handle
 * all menu item events via a switch statement mapping command names to actions.
 */
public class MenubarFactory {

    // Menu options to be referenced elsewhere
    private static final String NEW_FILE = "New File";
    private static final String LOAD = "Open File";
    private static final String SAVE = "Save";
    private static final String SAVE_AS = "Save as...";
    private static final String EXIT = "Exit";
    private static final String CONFIG = "Config";
    private static final String ABOUT = "About";
    private static final String INPUT_FILE = "Choose Input File";
    private static final String OUTPUT_FILE = "Choose Output File";
    private static final String RESET_INPUT_REDIRECT = "Reset Input Redirect";
    private static final String RESET_OUTPUT_REDIRECT = "Reset Output Redirect";

    private static final MenuActionListener actionListener = new MenuActionListener();

    private static JMenuBar menubar;
    private static JMenu redirectionMenu;

    /**
     * Generate the menu bar if it does not already exist and initialize its menus and menu items. Creates a singleton
     * menu bar with three main menu groups: (1) File menu - new file, open, save, save-as, exit operations (2) Settings
     * menu - direct link to settings and theme configuration (3) IO Direction menu - input/output stream redirection
     * and reset options
     *
     * @return the generated or existing menu bar (singleton pattern).
     */
    public static JMenuBar makeMenuBar() {
        if (menubar != null)
            return menubar;
        menubar = new JMenuBar();
        JMenu menu;

        /*
         * SIMULATION
         */
        menu = new JMenu("File");
        menu.getAccessibleContext().setAccessibleDescription("Actions related to the simulation");
        menubar.add(menu);

        addMenuItem(menu, NEW_FILE);
        addMenuItem(menu, LOAD);
        addMenuItem(menu, SAVE);
        addMenuItem(menu, SAVE_AS);
        menu.addSeparator();
        addMenuItem(menu, EXIT);

        /*
         * SETTINGS
         */
        /*
         * menu = new JMenu("Settings");
         * menu.getAccessibleContext().setAccessibleDescription("Popups related to settings"); menubar.add(menu);
         * addMenuItem(menu, CONFIG); addMenuItem(menu, ABOUT);
         */
        JMenuItem settingsItem = new JMenuItem("Settings");
        settingsItem.addActionListener(e -> SettingsPopup.instantiate());
        menubar.add(settingsItem);

        /*
         * IO
         */
        menu = new JMenu("IO Direction");
        redirectionMenu = menu;
        menu.getAccessibleContext().setAccessibleDescription("Popups related to input and output redirection");
        menubar.add(menu);
        addMenuItem(menu, INPUT_FILE);
        addMenuItem(menu, OUTPUT_FILE);
        addMenuItem(menu, RESET_INPUT_REDIRECT);
        addMenuItem(menu, RESET_OUTPUT_REDIRECT);

        return menubar;
    }

    /**
     * Helper method to automate common setup tasks for each menu item. Standardizes menu item creation by automatically
     * attaching the shared action listener and adding the item to the specified menu.
     *
     * @param menu the JMenu to which the item will be added.
     * @param name the display name and action command for the menu item.
     */
    private static void addMenuItem(JMenu menu, String name) {
        JMenuItem item = new JMenuItem(name);
        item.addActionListener(actionListener);
        menu.add(item);
    }

    /**
     * Enables or disables the entire IO Direction menu. Used to prevent input/output redirection operations when
     * simulation is not in an appropriate state.
     *
     * @param state true to enable the redirection menu, false to disable.
     */
    public static void setRedirectionEnable(boolean state) {
        redirectionMenu.setEnabled(state);
    }

    /**
     * Centralized action listener for all menu items in the menu bar. Dispatches menu actions to corresponding handler
     * methods in MenuActions based on the action command. Handles file operations (new, open, save, exit), settings
     * access, IO redirection, and error reporting.
     */
    private static class MenuActionListener implements ActionListener {
        /**
         * Handles menu item actions by dispatching to appropriate handler methods. Performs switch on the action
         * command to route to file operations, settings, IO redirection, or error handling.
         *
         * @param e the ActionEvent triggered by a menu item click.
         */
        @Override
        public void actionPerformed(ActionEvent e) {
            switch (e.getActionCommand()) {
            // File
            case NEW_FILE -> newFile();
            case SAVE_AS -> saveAs();
            case SAVE -> save();
            case LOAD -> load();
            case EXIT -> System.exit(0);

            // Settings
            case CONFIG -> SettingsPopup.instantiate();
            case ABOUT -> AboutPopup.openPopup();

            // IO Direction
            case INPUT_FILE -> selectInputFile();
            case OUTPUT_FILE -> selectOutputFile();
            case RESET_INPUT_REDIRECT -> {
                TerminalInstructions.streams().setInputStream(System.in);
            }
            case RESET_OUTPUT_REDIRECT -> {
                TerminalInstructions.streams().setOutputStream(System.out);
            }

            // Unimplemented
            default -> SystemStreams.err.printf("'%s' action is not yet defined\n", e.getActionCommand());
            }
        }
    }

}
