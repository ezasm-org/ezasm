package com.ezasm.gui.console;

import com.ezasm.gui.util.IThemeable;
import com.ezasm.gui.util.Theme;

import javax.swing.*;
import java.awt.*;
import java.io.OutputStream;
import java.io.PrintStream;

public class ConsoleTextArea extends JTextPane implements IThemeable {

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

    @Override
    public void applyTheme(Font font, Theme theme) {
        setForeground(theme.foreground());
        setBackground(theme.background());
        setSelectionColor(theme.selection());
        setSelectedTextColor(theme.foreground());
        setCaretColor(theme.foreground());
        setFont(font);
    }

    private static class NullOutputStream extends OutputStream {
        @Override
        public void write(int b) { // intentionally left blank
        }
    }
}
