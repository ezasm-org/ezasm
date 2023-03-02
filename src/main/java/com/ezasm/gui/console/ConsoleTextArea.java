package com.ezasm.gui.console;

import org.fife.ui.rtextarea.RTextArea;
import org.fife.ui.rtextarea.RUndoManager;

public class ConsoleTextArea extends RTextArea {

    public ConsoleTextArea() {
        super();
        setPopupMenu(null);
    }

    @Override
    protected RUndoManager createUndoManager() {
        RUndoManager undoManager = super.createUndoManager();
        undoManager.setLimit(0);
        return undoManager;
    }

}
