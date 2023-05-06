package com.ezasm.gui.util;

import org.fife.ui.rsyntaxtextarea.RSyntaxTextArea;

import javax.swing.*;
import java.io.PrintStream;

/**
 * Patches a bug in the Java implementation of pasting into JTextArea leading to errors printed to System.err.
 */
public class PatchedRSyntaxTextArea extends RSyntaxTextArea {

    /**
     * Constructs the base RSyntaxTextArea.
     */
    public PatchedRSyntaxTextArea() {
        super();
        setCodeFoldingEnabled(false);
        // Remove unused popup folding option
        // TODO find a better way to accomplish this
        getPopupMenu().remove(getPopupMenu().getComponent(10));
        getPopupMenu().remove(getPopupMenu().getComponent(9));
    }

    /**
     * A workaround to deal with <a href="https://bugs.java.com/bugdatabase/view_bug.do?bug_id=6606476">this issue</a>.
     * Prevents an error message on pasting certain text formats into the editor.
     */
    @Override
    public void paste() {
        PrintStream oldErrorStream = System.err;
        System.setErr(NullOutputStream.NULL_PRINT_STREAM);

        super.paste();

        System.setErr(oldErrorStream);
    }
}
