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

    /**
     * Invoked when text is inserted into the document.
     *
     * @param e the document event
     */
    @Override
    public void insertUpdate(DocumentEvent e) {
        editorPane.checkIfDirty();
        updateSavedState();
        editorPane.updateUndoRedoState();
    }
    /**
     * Invoked when text is removed from the document.
     *
     * @param e the document event
     */
    @Override
    public void removeUpdate(DocumentEvent e) {
        editorPane.checkIfDirty();
        updateSavedState();
        editorPane.updateUndoRedoState();
    }
    /**
     * Invoked when a document attribute or style change occurs.
     *
     * @param e the document event
     */
    @Override
    public void changedUpdate(DocumentEvent e) {
        editorPane.checkIfDirty();
        updateSavedState();
        editorPane.updateUndoRedoState();
    }

    /**
     * Sets the editor's global saved state to false (unsaved),
     * indicating that the current buffer has been modified.
     */
    private void updateSavedState() {Window.getInstance().getEditor().setFileSaved(false);}
}

