package com.ezasm.gui.editor;

import com.ezasm.gui.Window;

import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;

public class EditorDocumentListener implements DocumentListener {
    private final EzEditorPane editorPane;
    /**
     * Constructs an EditorDocumentListener that monitors changes to the document
     * and updates the saved state tracking in the associated editor.
     *
     * @param editorPane the editor pane to monitor for changes.
     */
    public EditorDocumentListener(EzEditorPane editorPane) {
        this.editorPane = editorPane;
    }
    @Override
    public void insertUpdate(DocumentEvent e) {
        editorPane.checkIfDirty();
        updateSavedState();
    }

    @Override
    public void removeUpdate(DocumentEvent e) {
        editorPane.checkIfDirty();
        updateSavedState();
    }

    @Override
    public void changedUpdate(DocumentEvent e) {
        editorPane.checkIfDirty();
        updateSavedState();
    }

    private void updateSavedState() {Window.getInstance().getEditor().setFileSaved(false);}
}
