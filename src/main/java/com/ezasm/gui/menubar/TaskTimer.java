package com.ezasm.gui.menubar;

import java.util.TimerTask;

/**TimerTask for timer to run "Save" function for autosave
     */
public class TaskTimer extends TimerTask {
    public TaskTimer(){

    }
    
    public void run() {
        System.out.println("Save once!");
    }
}
