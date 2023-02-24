package com.ezasm.gui.menubar;

import com.ezasm.gui.settings.SettingsPopup;
import com.ezasm.instructions.implementation.TerminalInstructions;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import static com.ezasm.gui.menubar.MenuActions.*;

/**
 * A factory which generates a menu bar to add to the application.
 */
public class MenubarFactory {

    // Menu options to be referenced elsewhere
    private static final String LOAD = "Open File";
    private static final String SAVE = "Save";
    private static final String SAVE_AS = "Save as...";
    private static final String EXIT = "Exit";
    private static final String CONFIG = "Config";
    private static final String INPUT_FILE = "Choose Input File";
    private static final String OUTPUT_FILE = "Choose Output File";
    private static final String RESET_INPUT_REDIRECT = "Reset Input Redirect";
    private static final String RESET_OUTPUT_REDIRECT = "Reset Output Redirect";

    private static final MenuActionListener actionListener = new MenuActionListener();

    private static JMenuBar menubar;
    private static JMenu redirectionMenu;

    /**
     * Generate the menu bar if it does not already exist and initialize its menus and menu items.
     *
     * @return the generated or existing menu bar.
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

        addMenuItem(menu, LOAD);
        addMenuItem(menu, SAVE);
        addMenuItem(menu, SAVE_AS);
        menu.addSeparator();
        addMenuItem(menu, EXIT);

        /*
         * SETTINGS
         */
        menu = new JMenu("Settings");
        menu.getAccessibleContext().setAccessibleDescription("Popups related to settings");
        menubar.add(menu);
        addMenuItem(menu, CONFIG);

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

    // Helper method to automate tasks I complete for every menu item (action
    // listener, color, register)
    private static void addMenuItem(JMenu menu, String name) {
        JMenuItem item = new JMenuItem(name);
        item.addActionListener(actionListener);
        menu.add(item);
    }

    public static void setRedirectionEnable(boolean state) {
        redirectionMenu.setEnabled(state);
    }

    /**
     * Helper action listener class to handle options in the menu.
     */
    private static class MenuActionListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            switch (e.getActionCommand()) {
            // File
            case SAVE_AS -> saveAs();
            case SAVE -> save();
            case LOAD -> load();
            case EXIT -> System.exit(0);

            // Settings
            case CONFIG -> SettingsPopup.instantiate();

            // IO Direction
            case INPUT_FILE -> selectInputFile();
            case OUTPUT_FILE -> selectOutputFile();
            case RESET_INPUT_REDIRECT -> {
                TerminalInstructions.setInputStream(TerminalInstructions.DEFAULT_INPUT_STREAM);
            }
            case RESET_OUTPUT_REDIRECT -> {
                TerminalInstructions.setOutputStream(TerminalInstructions.DEFAULT_OUTPUT_STREAM);
            }

            // Unimplemented
            default -> System.err.printf("'%s' action is not yet defined\n", e.getActionCommand());
            }
        }
    }

}
