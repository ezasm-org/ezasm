package com.ezasm.gui.menubar;

import com.ezasm.gui.Window;

import java.io.File;
import java.util.TimerTask;

/**
 * TimerTask for timer to run "Save" function for autosave
 */
public class TaskTimer extends TimerTask {
    public void run() {
        File fileToUpdate = new File(Window.getInstance().getEditor().getOpenFilePath());
        if (fileToUpdate.exists()) {
            System.out.println("Save once!");
        }

    }
}
