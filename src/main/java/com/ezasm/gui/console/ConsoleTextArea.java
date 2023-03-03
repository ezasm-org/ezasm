package com.ezasm.gui.console;

import org.fife.ui.rtextarea.RTextArea;
import org.fife.ui.rtextarea.RUndoManager;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;

public class ConsoleTextArea extends RTextArea {

    public ConsoleTextArea() {
        super();
    }

    @Override
    protected RUndoManager createUndoManager() {
        RUndoManager undoManager = super.createUndoManager();
        undoManager.setLimit(0);
        return undoManager;
    }

    @Override
    public void paste() {
        beginAtomicEdit();
        if (isEnabled() && isEditable()) {
            // TODO PASTE WITHOUT FORMATTING?
            ActionMap map = getActionMap();
            Action action = null;

            if (map != null) {
                action = map.get("paste");
            }
            if (action == null) {
                action = TransferHandler.getPasteAction();
            }

            System.out.println(action.getValue(Action.NAME));
            action.actionPerformed(new ActionEvent(this, ActionEvent.ACTION_PERFORMED,
                    (String) action.getValue(Action.NAME)));
        }
        endAtomicEdit();
    }

    /**
     * Creates the right-click popup menu. Subclasses can override this method
     * to replace or augment the popup menu returned.
     *
     * @return The popup menu.
     * @see #setPopupMenu(JPopupMenu)
     * @see #configurePopupMenu(JPopupMenu)
     * @see #createPopupMenuItem(Action)
     */
    protected JPopupMenu createPopupMenu() {
        JPopupMenu menu = new JPopupMenu();
        menu.add(createPopupMenuItem(copyAction));
        return menu;
    }
}
