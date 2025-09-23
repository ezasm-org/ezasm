package com.ezasm.gui.settings;

import com.ezasm.gui.Window;
import com.ezasm.gui.editor.EzEditorPane;
import com.ezasm.gui.menubar.MenuActions;

import java.util.Timer;
import java.util.TimerTask;

/**
 * An automatic file saver that supports saving the user's currently open file if it is not anonymous.
 */
public class AutoSave {

    private Timer timer;
    private SaveTask saveTask;

    /**
     * Constructs the automatic file saver.
     */
    public AutoSave() {
        timer = new Timer();
        saveTask = new SaveTask();
    }

    /**
     * Automatically calls Save function to save file periodically.
     */
    public void toggleRunning(boolean start, int intervalSeconds) {
        if (intervalSeconds <= 0) {
            throw new IllegalArgumentException("Interval must be greater than 0 seconds.");
        }
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
     * Represents a timer task for saving all editors if they are not anonymous.
     */
    private static class SaveTask extends TimerTask {
        @Override
        public void run() {
            for (EzEditorPane editorPane : Window.getInstance().getEditorPanes().getEditors()) {
                if (!editorPane.isFileAnonymous()) {
                    MenuActions.save(editorPane);
                }
            }
        }
    }
}
