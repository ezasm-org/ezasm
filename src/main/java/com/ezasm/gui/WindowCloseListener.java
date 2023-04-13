package com.ezasm.gui;

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
        if (!Window.getInstance().getEditor().getFileSaved()) {
            if (Window.getInstance().getEditor().getText().equals("")
                    && Window.getInstance().getEditor().getOpenFilePath().equals("")) {
                System.exit(0);
            }
            int resp = promptYesNoCancelDialog("Exiting...", "Your changes have not been saved."
                    + System.getProperty("line.separator") + "Would you like to save them?");

            if (resp == 0) {
                if (!Window.getInstance().getEditor().getOpenFilePath().equals("")) {
                    save();
                } else {
                    saveAs();
                }
                System.exit(0);
            } else if (resp == 1) {
                System.exit(0);
            } else {
                // cancel closing operation
            }
        } else {
            System.exit(0);
        }
    }
}
