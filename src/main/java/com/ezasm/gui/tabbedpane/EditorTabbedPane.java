package com.ezasm.gui.tabbedpane;

import com.ezasm.gui.Window;
import com.ezasm.gui.editor.EzEditorPane;

import java.awt.*;
import java.io.File;
import java.util.ArrayList;

public class EditorTabbedPane extends ClosableTabbedPane {

    public EditorTabbedPane() {
        super();
        EzEditorPane newEditor = new EzEditorPane();
        addTab(newEditor, null, "New Document.ez", "");
    }

    @Override
    public void removeTab(int index) {
        super.removeTab(index);
        if (getTabCount() == 0) { // Prevent there being no open editors
            addTab(getNewThemedEditor(), null, "New Document.ez", "");
        }
    }

    @Override
    public EzEditorPane getComponentAt(int index) {
        return (EzEditorPane) super.getComponentAt(index);
    }

    @Override
    public EzEditorPane getSelectedComponent() {
        return (EzEditorPane) super.getSelectedComponent();
    }

    public int indexOfFile(String path) {
        for (int i = 0; i < getTabCount(); i++) {
            if (getComponentAt(i).getOpenFilePath().equals(path)) {
                return i;
            }
        }
        return -1;
    }

    public EzEditorPane openFile(File fileIn) {
        EzEditorPane newEditor;
        int index = indexOfFile(fileIn.getPath());
        if (index == -1) {
            newEditor = getNewThemedEditor();
            addTab(newEditor, null, fileIn.getName(), "");
        } else {
            newEditor = getComponentAt(index);
        }
        setActiveTab(newEditor);
        return newEditor;
    }

    /**
     * Gets the instance's editor panes.
     *
     * @return the instance's editor panes.
     */
    public ArrayList<EzEditorPane> getEditors() {
        Component[] components = getTabs();
        ArrayList<EzEditorPane> res = new ArrayList<>();
        for (Component component : components) {
            if (component instanceof EzEditorPane editor) {
                res.add(editor);
            }
        }
        return res;
    }

    private static EzEditorPane getNewThemedEditor() {
        EzEditorPane newEditor = new EzEditorPane();
        newEditor.applyTheme(Window.getInstance().getConfig().getFont(), Window.getInstance().getTheme());
        return newEditor;
    }
}
