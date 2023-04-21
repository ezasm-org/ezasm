package com.ezasm.gui.util;

import com.ezasm.gui.Window;
import com.ezasm.gui.editor.EzEditorPane;

import javax.swing.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

import static com.ezasm.gui.menubar.MenuActions.*;
import static com.ezasm.gui.util.DialogFactory.promptYesNoCancelDialog;

public class WindowCloseListener extends WindowAdapter {

    public WindowCloseListener() {
        super();
    }

    @Override
    public void windowClosing(WindowEvent e) {
        boolean exit = true;
        for (EzEditorPane editorPane : Window.getInstance().getEditorPanes().getEditors()) {
            if (editorPane.getFileSaved() || (editorPane.getText().equals("") && editorPane.isFileAnonymous())) {
                continue;
            }
            Window.getInstance().getEditorPanes().switchToFile(editorPane.getOpenFilePath());
            int resp = promptYesNoCancelDialog("Exiting...", "Your changes to '" + editorPane.getOpenFilePath()
                    + "' have not been saved. Would you like to save them?");

            if (resp == JOptionPane.YES_OPTION) {
                if (editorPane.isFileAnonymous()) {
                    save();
                } else {
                    saveAs();
                }
            } else if (resp == JOptionPane.NO_OPTION) {
                // Do not save, close
            } else if (resp == JOptionPane.CANCEL_OPTION || resp == JOptionPane.CLOSED_OPTION) {
                exit = false;
                break;
            }

        }
        if (exit) {
            System.exit(0);
        }
    }
}
