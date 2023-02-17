package com.ezasm.gui;

import com.ezasm.util.FileIO;

import javax.swing.*;
import javax.swing.filechooser.FileSystemView;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

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

    private static final MenuActionListener actionListener = new MenuActionListener();

    private static JMenuBar menubar;

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

        return menubar;
    }

    // Helper method to automate tasks I complete for every menu item (action
    // listener, color, register)
    private static void addMenuItem(JMenu menu, String name) {
        JMenuItem item = new JMenuItem(name);
        item.addActionListener(actionListener);
        menu.add(item);
    }

    /**
     * HELPER
     *
     * Runs the action event for save as.
     */
    private static void saveAs() {
        JFileChooser fileChooser = new JFileChooser(FileSystemView.getFileSystemView());
        fileChooser.setCurrentDirectory(new File(System.getProperty("user.home")));
        fileChooser.setSelectedFile(new File("code.ez"));
        FileIO.filterFileChooser(fileChooser);
        int fileChooserOption = fileChooser.showSaveDialog(null);
        if (fileChooserOption == JFileChooser.APPROVE_OPTION) {
            File file = fileChooser.getSelectedFile();
            Window.getInstance().setLoadedFile(file.getPath());
            boolean overwrite = true;
            if (file.exists()) {
                // File exists, prompt user to overwrite
                int confirmDialogOption = JOptionPane.showConfirmDialog(null,
                        "The given file '" + file.getName() + "' already exits.\n" + "Would you like to overwrite it?",
                        "File Already Exists", JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE);
                overwrite = confirmDialogOption == JOptionPane.YES_OPTION;
            }
            if (overwrite) {
                try {
                    FileIO.writeFile(file, Window.getInstance().getText());
                } catch (IOException ex) {
                    // TODO handle
                    ex.printStackTrace();
                    throw new RuntimeException();
                }
            }
        }
    }

    /**
     * HELPER
     *
     * Runs the action event for save.
     */
    private static void save() {
        File fileToUpdate = new File(Window.getInstance().getLoadedFile());

        if (!fileToUpdate.exists()) {
            saveAs();
        } else {
            if (fileToUpdate != null && fileToUpdate.exists() && fileToUpdate.canRead()) {
                String content = Window.getInstance().getText();
                try {
                    FileWriter writer = new FileWriter(fileToUpdate.getPath());
                    writer.write(content);
                    writer.close();
                } catch (IOException ex) {
                    ex.printStackTrace();
                }
            } else {
                JOptionPane.showMessageDialog(null, "ERROR: Not able to save file.");
            }
        }
    }

    /**
     * HELPER
     *
     * Runs the action event for load.
     */
    private static void load() {
        JFileChooser fileChooser = new JFileChooser(FileSystemView.getFileSystemView());
        System.getProperty("user.home");
        fileChooser.setCurrentDirectory(new File(System.getProperty("user.home")));
        FileIO.filterFileChooser(fileChooser);
        int fileChooserOption = fileChooser.showOpenDialog(null);
        if (fileChooserOption == JFileChooser.APPROVE_OPTION) {
            File file = fileChooser.getSelectedFile();
            if (file != null && file.exists() && file.canRead()) {
                try {
                    String content = FileIO.readFile(file);
                    Window.getInstance().setText(content);
                    Window.getInstance().setLoadedFile(file.getPath());
                } catch (IOException ex) {
                    // TODO handle
                    throw new RuntimeException();
                }
            }
        }
    }

    /**
     * Helper action listener class to handle options in the menu.
     */
    private static class MenuActionListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            switch (e.getActionCommand()) {
            case SAVE_AS -> {
                saveAs();
            }

            case SAVE -> {
                save();
            }

            case LOAD -> {
                load();
            }
            case EXIT -> {
                System.exit(0);
            }
            case CONFIG -> {
                SettingsPopup.instantiate();
            }
            default -> {
                System.err.printf("'%s' action is not yet defined\n", e.getActionCommand());
            }
            }
        }
    }

}
