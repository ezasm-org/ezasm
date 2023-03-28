package com.ezasm.gui.menubar;

import com.ezasm.gui.Window;
import com.ezasm.util.FileIO;

import javax.swing.*;

import java.awt.event.ActionEvent;
import java.io.File;
import java.io.IOException;

import static com.ezasm.gui.util.DialogFactory.promptOverwriteDialog;
import static com.ezasm.gui.util.DialogFactory.promptWarningDialog;
import static com.ezasm.util.FileIO.*;

import java.util.Timer;

/**
 * Action functions for the menubar actions like Save, Save As, Open, New, etc.
 */
public class MenuActions {

    /**
     * Runs the action event for save as.
     *
     * @return true if the operation was successful, false otherwise.
     */
    public static boolean saveAs() {
        JFileChooser fileChooser = createFileChooser("Save", TEXT_FILE_MASK | EZ_FILE_MASK);
        fileChooser.setSelectedFile(new File("code.ez"));
        int fileChooserOption = fileChooser.showSaveDialog(null);
        if (fileChooserOption == JFileChooser.APPROVE_OPTION) {
            File file = fileChooser.getSelectedFile();
            boolean overwrite = promptOverwriteDialog(file);
            if (overwrite) {
                try {
                    FileIO.writeFile(file, Window.getInstance().getEditor().getText());
                    Window.getInstance().getEditor().setOpenFilePath(file.getPath());
                    Window.getInstance().getEditor().setFileSaved(true);
                    return true;
                } catch (IOException e) {
                    promptWarningDialog("Error Saving File",
                            String.format("There was an error saving to '%s'", file.getName()));
                }
            }
        }
        return false;
    }

    /**
     * Runs the action event for save.
     *
     * @return true if the operation was successful, false otherwise.
     */
    public static boolean save() {
        File fileToUpdate = new File(Window.getInstance().getEditor().getOpenFilePath());

        if (!fileToUpdate.exists()) {
            return saveAs();
        } else {
            try {
                FileIO.writeFile(fileToUpdate, Window.getInstance().getEditor().getText());
                Window.getInstance().getEditor().setFileSaved(true);
                return true;
            } catch (IOException e) {
                promptWarningDialog("Error Saving File",
                        String.format("There was an error saving to '%s'", fileToUpdate.getName()));
            }
        }
        return false;
    }

    /**
     * Runs the action event for load.
     *
     * @return true if the operation was successful, false otherwise.
     */
    public static boolean load() {
        JFileChooser fileChooser = createFileChooser("Open File", TEXT_FILE_MASK | EZ_FILE_MASK);
        int fileChooserOption = fileChooser.showOpenDialog(null);
        if (fileChooserOption == JFileChooser.APPROVE_OPTION) {
            File file = fileChooser.getSelectedFile();
            if (file != null && file.exists() && file.canRead()) {
                try {
                    String content = FileIO.readFile(file);
                    Window.getInstance().getEditor().setText(content);
                    Window.getInstance().getEditor().setOpenFilePath(file.getPath());
                    Window.getInstance().getEditor().setFileSaved(true);
                    return true;
                } catch (IOException ex) {
                    promptWarningDialog("Error Loading File",
                            String.format("There was an error loading '%s'", file.getName()));
                }
            }
        }
        return false;
    }

    /**
     * Runs the action event for selecting an input file.
     */
    public static void selectInputFile() {
        JFileChooser fileChooser = createFileChooser("Choose an Input File", TEXT_FILE_MASK);
        int fileChooserOption = fileChooser.showOpenDialog(null);
        if (fileChooserOption == JFileChooser.APPROVE_OPTION) {
            File file = fileChooser.getSelectedFile();
            if (file != null) {
                if (file.exists() && file.canRead()) {
                    Window.getInstance().setFileInputStream(file);
                } else {
                    promptWarningDialog("Error Reading File",
                            String.format("There was an error reading from '%s'\nOperation cancelled", file.getName()));
                }
            }
        }
    }

    /**
     * Runs the action event for selecting an input file.
     */
    public static void selectOutputFile() {
        JFileChooser fileChooser = createFileChooser("Choose an Output File", TEXT_FILE_MASK);
        fileChooser.setSelectedFile(new File("output.txt"));
        int fileChooserOption = fileChooser.showSaveDialog(null);
        if (fileChooserOption == JFileChooser.APPROVE_OPTION) {
            File file = fileChooser.getSelectedFile();
            if (file != null) {
                boolean overwrite = promptOverwriteDialog(file);
                if (overwrite) {
                    Window.getInstance().setFileOutputStream(file);
                }
            }
        }
    }

    /**
     * Auto called Save function to save file periodically
     *
     */

    public static void autoSave() {
        Timer time = new Timer();
        time.schedule(new TaskTimer(), 0, 5000);
    }
}
