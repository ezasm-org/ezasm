package com.ezasm.gui.editor;

import com.ezasm.gui.Window;

import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;

public class EditorDocumentListener implements DocumentListener {

    @Override
    public void insertUpdate(DocumentEvent e) {
        updateSavedState();
    }

    @Override
    public void removeUpdate(DocumentEvent e) {
        updateSavedState();
    }

    @Override
    public void changedUpdate(DocumentEvent e) {
        updateSavedState();
    }

    private void updateSavedState() {
        Window.getInstance().getEditor().setFileSaved(false);
    }
}
