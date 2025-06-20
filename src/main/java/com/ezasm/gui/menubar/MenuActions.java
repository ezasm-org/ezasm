package com.ezasm.gui.menubar;

import com.ezasm.gui.Window;
import com.ezasm.gui.editor.EzEditorPane;
import com.ezasm.util.FileIO;
import javax.swing.*;

import java.io.File;
import java.io.IOException;

import static com.ezasm.gui.util.DialogFactory.promptOverwriteDialog;
import static com.ezasm.gui.util.DialogFactory.promptWarningDialog;
import static com.ezasm.util.FileIO.*;
import javax.swing.undo.UndoManager;

/**
 * Action functions for the menubar actions like Save, Save As, Open, New, etc.
 */
public class MenuActions {

    /**
     * Creates a new, empty file in the editor.
     */
    public static void newFile() {
        Window.getInstance().getEditorPanes().newFile();
    }

    /**
     * Runs the action event for save as.
     *
     * @return true if the operation was successful, false otherwise.
     */
    public static boolean saveAs() {
        JFileChooser fileChooser = createFileChooser("Save", TEXT_FILE_MASK | EZ_FILE_MASK);
        fileChooser.setSelectedFile(new File(Window.getInstance().getEditor().getOpenFilePath()));
        int fileChooserOption = fileChooser.showSaveDialog(null);
        if (fileChooserOption == JFileChooser.APPROVE_OPTION) {
            File file = fileChooser.getSelectedFile();
            boolean overwrite = promptOverwriteDialog(file);
            if (overwrite) {
                try {
                    FileIO.writeFile(file, Window.getInstance().getEditor().getText());
                    Window.getInstance().getEditor().setOpenFilePath(file.getPath());
                    Window.getInstance().getEditor().setFileSaved(true);
                    Window.getInstance().getEditorPanes().setTabName(
                            Window.getInstance().getEditorPanes().indexOfFile(file.getPath()), file.getName());
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
     * Saves the content of the given editor to the file pointed to by the editor.
     *
     * @param editorPane the file to save.
     * @return true if the operation was successful, false otherwise.
     */
    public static boolean save(EzEditorPane editorPane) {
        File fileToUpdate = new File(editorPane.getOpenFilePath());
        if (!fileToUpdate.exists()) {
            return saveAs();
        } else {
            try {
                FileIO.writeFile(fileToUpdate, editorPane.getText());
                editorPane.setFileSaved(true);
                editorPane.markSavedState();
                return true;
            } catch (IOException e) {
                promptWarningDialog("Error Saving File",
                        String.format("There was an error saving to '%s'", fileToUpdate.getName()));
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
        return save(Window.getInstance().getEditor());
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
                    boolean notOpen = Window.getInstance().getEditorPanes().indexOfFile(file.getAbsolutePath()) == -1;
                    EzEditorPane newEditor = Window.getInstance().getEditorPanes().openFile(file);

                    if (notOpen) {
                        String content = FileIO.readFile(file);
                        newEditor.setText(content);
                        newEditor.getUndoManager().discardAllEdits();
                        newEditor.setOpenFilePath(file.getPath());
                        newEditor.markSavedState();

                    }
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

}

