package com.ezasm.gui.settings;

import com.ezasm.gui.Window;
import static com.ezasm.gui.menubar.MenuActions.save;

import java.io.File;
import java.util.Timer;
import java.util.TimerTask;

/**
 * Supports saving the user's currently open file if it is not anonymous.
 */
public class AutoSave {

    /**
     * Represents a timer task for saving a file if it is not anonymous.
     */
    private static class SaveTask extends TimerTask {
        public void run() {
            File fileToUpdate = new File(Window.getInstance().getEditor().getOpenFilePath());
            if (fileToUpdate.exists()) {
                save();
            }
        }
    }

    private Timer timer = new Timer();
    private SaveTask saveTask = new SaveTask();

    private int intervalMS;

    public AutoSave() {
        this.intervalMS = 10000;
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
            this.intervalMS = intervalSeconds * 1000;
            timer.schedule(this.saveTask, 0, this.intervalMS);
        }
    }
}
