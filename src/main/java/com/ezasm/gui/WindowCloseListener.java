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
        // Exit without asking if the file has already been saved OR the file is anonymous and empty
        if (Window.getInstance().getEditor().getFileSaved() || Window.getInstance().getEditor().getText().equals("")
                && Window.getInstance().getEditor().getOpenFilePath().equals("")) {
            System.exit(0);
        }

        int resp = promptYesNoCancelDialog("Exiting...",
                "Your changes have not been saved.\nWould you like to save them?");

        if (resp == 0) {
            // Chose Yes
            if (Window.getInstance().getEditor().getOpenFilePath().equals("")) { // Anonymous file
                saveAs();
            } else { // Known file
                save();
            }
            System.exit(0);
        } else if (resp == 1) {
            // Chose No
            System.exit(0);
        } else {
            // Chose to Cancel the closing operation
        }
    }
}
