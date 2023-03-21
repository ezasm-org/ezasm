package com.ezasm.gui;

import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.Scanner;

import static com.ezasm.gui.menubar.MenuActions.*;
import static com.ezasm.gui.util.DialogFactory.promptYesNoCancelDialog;
import static com.ezasm.gui.util.DialogFactory.promptYesNoDialog;

public class WindowCloseListener extends WindowAdapter {

    public WindowCloseListener() {
        super();
    }

    @Override
    public void windowClosing(WindowEvent e) {
        if (!Window.getInstance().getEditor().getFileSaved()) {
            if(Window.getInstance().getEditor().getText().equals("")
                    && Window.getInstance().getEditor().getOpenFilePath().equals("")) {
                System.exit(0);
            }
            int resp = promptYesNoCancelDialog("Exiting...",
                    "Your changes have not been saved." + System.getProperty("line.separator") + "Would you like to save them?");

            if (resp == 0) {
                if (!Window.getInstance().getEditor().getOpenFilePath().equals("")) {
                    save();
                } else {
                    String pastText = "";
                    File file = new File(Window.getInstance().getEditor().getOpenFilePath());
                    try {
                        Scanner in = new Scanner(new FileReader(file));
                        StringBuilder sb = new StringBuilder();
                        while(in.hasNext()){
                            sb.append(in.next());
                        }
                        in.close();
                        pastText = sb.toString();
                    } catch (FileNotFoundException ex) {
                        throw new RuntimeException(ex);
                    }

                    System.out.println(pastText);

                    //saveAs();
                }
                System.exit(0);
            } else if (resp == 1) {
                System.exit(0);
            } else {
                //cancel closing operation
            }
        } else {
            System.exit(0);
        }
    }
}
