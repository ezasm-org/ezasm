package com.ezasm.gui.util;

import javax.swing.*;
import java.io.File;

/**
 * Creates blocking dialogs to deliver a message to the user.
 */
public class DialogFactory {

    /**
     * Prompts the user to if they would like to overwrite the specified file if it exists.
     *
     * @param file the file in question.
     * @return true if the file can be overwritten, false otherwise.
     */
    public static boolean promptOverwriteDialog(File file) {
        if (file.exists()) {
            return promptYesNoDialog(
                    String.format("The file '%s' already exits.\nWould you like to overwrite it?", file.getName()),
                    "File Already Exists");
        }
        return true;
    }

    /**
     * Creates a prompt dialog asking the user a Yes/No question.
     *
     * @param title   the title of the dialog.
     * @param message the message to display on the dialog.
     * @return true if the user selected yes, false otherwise
     */
    public static boolean promptYesNoDialog(String title, String message) {
        int confirmDialogOption = JOptionPane.showConfirmDialog(null, message, title, JOptionPane.YES_NO_OPTION,
                JOptionPane.QUESTION_MESSAGE);
        return confirmDialogOption == JOptionPane.YES_OPTION;
    }

    /**
     * Creates an error dialog to display the error message.
     *
     * @param title   the title of the dialog.
     * @param message the message to display on the dialog.
     */
    public static void promptErrorDialog(String title, String message) {
        JOptionPane.showMessageDialog(null, message, title, JOptionPane.ERROR_MESSAGE);
    }

    /**
     * Creates an warning dialog to display the warning message.
     *
     * @param title   the title of the dialog.
     * @param message the message to display on the dialog.
     */
    public static void promptWarningDialog(String title, String message) {
        JOptionPane.showMessageDialog(null, message, title, JOptionPane.WARNING_MESSAGE);
    }

}
