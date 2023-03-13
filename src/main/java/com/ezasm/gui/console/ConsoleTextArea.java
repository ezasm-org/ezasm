package com.ezasm.gui.console;

import com.ezasm.gui.util.IThemeable;
import com.ezasm.gui.util.EditorTheme;
import com.ezasm.gui.util.NullOutputStream;

import javax.swing.*;
import java.awt.*;
import java.io.PrintStream;

/**
 * Represents a GUI console text box which a user will type into. To be used by the Console. Patches an issue with
 * pasting that causes uncatchable system error text generation.
 */
public class ConsoleTextArea extends JTextPane implements IThemeable {

    private static final PrintStream NULL_PRINT_STREAM = new PrintStream(new NullOutputStream());

    /**
     * Constructs the text area to be used in a Console.
     */
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

    /**
     * Applies the given theme and font to the component itself, the tabbed pane, and all subcomponents of the tabbed
     * pane. If the components are IThemable, uses their IThemable#applyTheme method to do so.
     *
     * @param font        the font to apply.
     * @param editorTheme the theme to apply.
     */
    @Override
    public void applyTheme(Font font, EditorTheme editorTheme) {
        setForeground(editorTheme.foreground());
        setBackground(editorTheme.background());
        setSelectionColor(editorTheme.selection());
        setSelectedTextColor(editorTheme.foreground());
        setCaretColor(editorTheme.foreground());
        setFont(font);
    }
}
