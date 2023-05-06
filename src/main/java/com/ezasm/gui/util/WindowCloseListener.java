package com.ezasm.gui.util;

import com.ezasm.gui.Window;
import com.ezasm.gui.editor.EzEditorPane;

import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

/**
 * A WindowAdapter used when attempting to close the application. Checks to see if files are saved.
 */
public class WindowCloseListener extends WindowAdapter {

    public WindowCloseListener() {
        super();
    }

    /**
     * Attempt to close the application. If anything needs to be saved, switch to it and prompt the user to save it.
     *
     * @param e the close event.
     */
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
