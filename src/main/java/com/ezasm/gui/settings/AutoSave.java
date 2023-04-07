package com.ezasm.gui.settings;

import com.ezasm.gui.Window;
import com.ezasm.gui.menubar.MenuActions;

import java.io.File;
import java.util.Timer;
import java.util.TimerTask;

/**
 * An automatic file saver that supports saving the user's currently open file if it is not anonymous.
 */
public class AutoSave {

    private Timer timer;
    private SaveTask saveTask;

    /**
     * Constructs the automatic file saver
     */
    public AutoSave() {
        timer = new Timer();
        saveTask = new SaveTask();
    }

    /**
     * Automatically called Save function to save file periodically.
     */
    public void toggleRunning(boolean start, int intervalSeconds) {
        this.timer.cancel();
        this.timer = new Timer();
        this.saveTask.cancel();
        this.saveTask = new SaveTask();

        if (start) {
            int intervalMS = intervalSeconds * 1000;
            timer.schedule(this.saveTask, 0, intervalMS);
        }
    }

    /**
     * Represents a timer task for saving a file if it is not anonymous.
     */
    private static class SaveTask extends TimerTask {
        @Override
        public void run() {
            File fileToUpdate = new File(Window.getInstance().getEditor().getOpenFilePath());
            if (fileToUpdate.exists()) {
                MenuActions.save();
            }
        }
    }
}
