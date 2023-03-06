package com.ezasm.gui.console;

import javax.swing.*;
import java.io.OutputStream;
import java.io.PrintStream;

public class ConsoleTextArea extends JTextPane {

    private static final PrintStream NULL_PRINT_STREAM = new PrintStream(new NullOutputStream());

    public ConsoleTextArea() {
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

    private static class NullOutputStream extends OutputStream {
        @Override
        public void write(int b) { // intentionally left blank
        }
    }
}
