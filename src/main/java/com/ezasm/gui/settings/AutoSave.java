package com.ezasm.gui.settings;

import com.ezasm.util.FileIO;
import com.ezasm.gui.Window;

import java.io.File;
import java.util.Timer;
import java.util.TimerTask;

public class AutoSave {
    private TimerTask task = new TimerTask(){
        public void run() {
            File fileToUpdate = new File(Window.getInstance().getEditor().getOpenFilePath());
                    if (fileToUpdate.exists()) {
                        System.out.println("Save once!");
                    }
    
        }
    };
    private Timer time = new Timer();
    private int Interval;
    private int def_Interval = 10;

    public AutoSave(){
        this.Interval = def_Interval * 1000;
    }

    /**
     * Auto called Save function to save file periodically
     *
     */

    public void run(boolean sw, int sec){
        if(sw){
            this.task.cancel();
            this.Interval = sec * 1000;
            time.schedule(this.task, 0, this.Interval);
        }
        else{
            this.task.cancel();
            System.out.println("cancel!");
        }
        
    }

}
