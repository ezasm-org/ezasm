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
            if (!editorPane.close()) {
                exit = false;
                break;
            }

        }
        if (exit) {
            System.exit(0);
        }
    }
}
