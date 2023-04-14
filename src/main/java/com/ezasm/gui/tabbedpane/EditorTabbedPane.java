package com.ezasm.gui.tabbedpane;

import com.ezasm.gui.Window;
import com.ezasm.gui.editor.EzEditorPane;

import java.awt.*;
import java.io.File;
import java.util.ArrayList;

/**
 * Represents a tab selection of closable editor panes.
 */
public class EditorTabbedPane extends ClosableTabbedPane {

    /**
     * The base new file name.
     */
    public static final String NEW_FILE_NAME = "New Document";

    /**
     * The base new file extension.
     */
    public static final String NEW_FILE_SUFFIX = ".ez";

    /**
     * A prefix for anonymous files internally.
     */
    public static final String NEW_FILE_PREFIX = "//";

    /**
     * Keeps track of the number of new files opened so that the names never collide.
     */
    private int newFileNumber = 1;

    /**
     * Constructs a new tabbed pane of closable editor windows. This pane is guaranteed to never be empty.
     */
    public EditorTabbedPane() {
        super();
        newFile();
    }

    /**
     * Removes the tab at the given index, opening a new anonymous tab if the tabbed pane would become empty.
     *
     * @param index the index of the component to remove.
     */
    @Override
    public void removeTab(int index) {
        super.removeTab(index);
        if (getTabCount() == 0) {
            // Prevent there being no open editors
            newFile();
        }
    }

    /**
     * Gets the editor pane at a specified index.
     *
     * @param index the index to seek a component at.
     * @return the editor pane at a specified index.
     */
    @Override
    public EzEditorPane getComponentAt(int index) {
        return (EzEditorPane) super.getComponentAt(index);
    }

    /**
     * Gets the currently selected editor pane.
     *
     * @return the currently selected editor pane.
     */
    @Override
    public EzEditorPane getSelectedComponent() {
        return (EzEditorPane) super.getSelectedComponent();
    }

    /**
     * Gets the index of an editor pane given the file path. Returns -1 if the given file path is not open.
     *
     * @param path the file path to query.
     * @return the index of an editor pane given the file path, or -1 if the given file path is not open.
     */
    public int indexOfFile(String path) {
        for (int i = 0; i < getTabCount(); i++) {
            if (getComponentAt(i).getOpenFilePath().equals(path)) {
                return i;
            }
        }
        return -1;
    }

    /**
     * Opens the given file as an editor. If the file is already open, switches tabs to view that file.
     *
     * @param fileIn the file to open.
     * @return the file editor opened or switched to.
     */
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
     * Generates a new anonymous file editor which is empty.
     *
     * @return the newly created file editor.
     */
    public EzEditorPane newFile() {
        EzEditorPane newEditor = getNewThemedEditor();
        newFileNumber++;
        addTab(newEditor, null, newEditor.getOpenFilePath(), "");
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

    /**
     * Gets a new file edit themed to the current window instance.
     *
     * @return the new themed file editor.
     */
    private EzEditorPane getNewThemedEditor() {
        EzEditorPane newEditor = new EzEditorPane();
        newEditor.setOpenFilePath(NEW_FILE_PREFIX + NEW_FILE_NAME + newFileNumber + NEW_FILE_SUFFIX);
        newEditor.applyTheme(Window.getInstance().getConfig().getFont(), Window.getInstance().getTheme());
        return newEditor;
    }
}
