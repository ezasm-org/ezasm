package com.ezasm.gui.console;

import com.ezasm.gui.util.IThemeable;
import com.ezasm.gui.util.EditorTheme;

import javax.swing.*;
import java.awt.*;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;

/**
 * Represents a console in which a user can type input into and read output from.
 */
public class Console extends JPanel implements IThemeable {

    private final ConsoleTextArea textArea;
    private final JScrollPane scrollPane;

    private final ConsoleInputStream inputStream;
    private final ConsoleOutputStream outputStream;
    private final ConsoleErrorOutputStream errorStream;

    private Color outputStreamColor;
    private Color errorStreamColor;

    /**
     * Creates a user interface console.
     */
    public Console() {
        super();
        textArea = new ConsoleTextArea(this);
        scrollPane = new JScrollPane(textArea);

        inputStream = new ConsoleInputStream();
        outputStream = new ConsoleOutputStream(this);
        errorStream = new ConsoleErrorOutputStream(this);

        outputStreamColor = Color.GRAY;
        errorStreamColor = Color.RED;

        setLayout(new BorderLayout());
        add(scrollPane);
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
        textArea.applyTheme(font, editorTheme);

        outputStreamColor = editorTheme.comment();
        errorStreamColor = editorTheme.red();

        editorTheme.applyThemeScrollbar(scrollPane.getVerticalScrollBar());
        editorTheme.applyThemeScrollbar(scrollPane.getHorizontalScrollBar());
    }

    /**
     * Resets the content in the console and its streams.
     */
    public void reset() {
        inputStream.resetBuffer();
        textArea.reset();
    }

    /**
     * Writes text as if it were going to System.out.
     *
     * @param text the text to write.
     */
    public void writeTextFromOutputStream(String text) {
        // TODO maybe use a different color for user input and console output than current
        textArea.writeTextWithColor(text, outputStreamColor);
    }

    /**
     * Writes text as if it were going to System.err.
     *
     * @param text the text to write.
     */
    public void writeTextFromErrorStream(String text) {
        textArea.writeTextWithColor(text, errorStreamColor);
    }

    /**
     * Puts the given text into the input stream buffer.
     *
     * @param text the text to add to the input stream buffer.
     */
    public void writeTextToInputStream(String text) {
        inputStream.addToBuffer(text);
    }

    /**
     * Gets the input stream representing input to the console.
     *
     * @return the input stream representing input to the console.
     */
    public ConsoleInputStream getInputStream() {
        return inputStream;
    }

    /**
     * Gets the output stream representing output to the console.
     *
     * @return the output stream representing output to the console.
     */
    public ConsoleOutputStream getOutputStream() {
        return outputStream;
    }

    /**
     * Gets the error output stream representing error output to the console.
     *
     * @return the error output stream representing error output to the console.
     */
    public ConsoleErrorOutputStream getErrorStream() {
        return errorStream;
    }
}
