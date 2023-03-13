package com.ezasm.gui.util;

import org.fife.ui.rsyntaxtextarea.RSyntaxTextArea;

import java.io.PrintStream;

/**
 * Patches a bug in the Java implementation of pasting into JTextArea leading to errors printed to System.err.
 */
public class PatchedRSyntaxTextArea extends RSyntaxTextArea {

    private static final PrintStream NULL_PRINT_STREAM = new PrintStream(new NullOutputStream());

    /**
     * Constructs the base RSyntaxTextArea.
     */
    public PatchedRSyntaxTextArea() {
        super();
    }

    /**
     * A workaround to deal with <a href="https://bugs.java.com/bugdatabase/view_bug.do?bug_id=6606476">this issue</a>.
     * Prevents an error message on pasting certain text formats into the editor.
     */
    @Override
    public void paste() {
        PrintStream oldErrorStream = System.err;
        System.setErr(NULL_PRINT_STREAM);

        super.paste();

        System.setErr(oldErrorStream);
    }
}
