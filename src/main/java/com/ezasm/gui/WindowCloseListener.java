package com.ezasm.gui;

import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

import static com.ezasm.gui.menubar.MenuActions.*;
import static com.ezasm.gui.util.DialogFactory.promptYesNoDialog;

public class WindowCloseListener extends WindowAdapter {

    public WindowCloseListener(){
        super();
    }

    @Override
    public void windowClosing(WindowEvent e) {
        if(!Window.getInstance().getSaved()) {
            boolean resp = promptYesNoDialog("Exiting...",
                    "Your changes have not been saved.\nWould you like to save them?");
            if(resp){
                saveAs();
            }
        }
        System.exit(0);
    }
}
