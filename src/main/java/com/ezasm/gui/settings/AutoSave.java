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
    private int sec_Interval;
    private int mil_Interval;
    private int def_mil_Interval = 10000;
    private int def_sec_Interval = 10;
    private boolean ran = false;

    public AutoSave(){
        this.mil_Interval = def_mil_Interval;
        this.sec_Interval = def_sec_Interval;
    }

    /**
     * Auto called Save function to save file periodically
     *
     */

    public void run(boolean sw, int sec){
        if(sw){
            if(!ran){
                this.sec_Interval = sec;
                this.mil_Interval = sec * 1000;
                time.schedule(task, 0, this.mil_Interval);
                this.ran = true;
            }
            else{
                this.task.cancel();
                this.task = new TimerTask(){
                    public void run() {
                        File fileToUpdate = new File(Window.getInstance().getEditor().getOpenFilePath());
                                if (fileToUpdate.exists()) {
                                    System.out.println("Save once!");
                                }
                
                    }
                };
                this.sec_Interval = sec;
                this.mil_Interval = sec * 1000;
                time.schedule(this.task, 0, this.mil_Interval);
            }        
            
        }
        else{
            if(!ran){
                //System.out.println("cancel!");
            }
            else{
                this.task.cancel();
                this.task = new TimerTask(){
                    public void run() {
                        File fileToUpdate = new File(Window.getInstance().getEditor().getOpenFilePath());
                                if (fileToUpdate.exists()) {
                                    System.out.println("Save once!");
                                }
                
                    }
                };
                System.out.println("cancel!");
            }                
        }     
    }
}
