package com.ezasm.gui.settings;

import com.ezasm.util.FileIO;
import com.ezasm.gui.Window;
import static com.ezasm.gui.menubar.MenuActions.save;

import java.io.File;
import java.util.Timer;
import java.util.TimerTask;

public class AutoSave {
    private class Task extends TimerTask {
        public void run() {
            File fileToUpdate = new File(Window.getInstance().getEditor().getOpenFilePath());
            if (fileToUpdate.exists()) {
                save();
            }

        }
    }

    private Task task = new Task();

    private Timer time = new Timer();
    private int sec_Interval;
    private int mil_Interval;
    private int def_mil_Interval = 10000;
    private int def_sec_Interval = 10;
    private boolean ran = false;

    public AutoSave() {
        this.mil_Interval = def_mil_Interval;
        this.sec_Interval = def_sec_Interval;
    }

    /**
     * Auto called Save function to save file periodically
     *
     */

    public void run(boolean sw, int sec) {
        if (sw) {
            if (!ran) {
                this.sec_Interval = sec;
                this.mil_Interval = sec * 1000;
                time.schedule(this.task, 0, this.mil_Interval);
                this.ran = true;
            } else {
                this.task.cancel();
                this.task = new Task();
                this.sec_Interval = sec;
                this.mil_Interval = sec * 1000;
                time.schedule(this.task, 0, this.mil_Interval);
            }

        } else {
            if (ran) {
                this.task.cancel();
                this.task = new Task();
            }
        }
    }
}
