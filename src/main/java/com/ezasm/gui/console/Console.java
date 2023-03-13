package com.ezasm.gui.console;

import com.ezasm.gui.Window;
import com.ezasm.gui.util.IThemeable;
import com.ezasm.gui.util.EditorTheme;

import javax.swing.*;
import javax.swing.text.*;
import java.awt.*;
import java.awt.event.*;

/**
 * Represents a console in which a user can type input into and read output from. The input is only confirmed when the
 * user presses enter.
 */
public class Console extends JPanel implements IThemeable {

    private final ConsoleTextArea textArea;
    private final JScrollPane scrollPane;

    private final ConsoleInputStream inputStream;
    private final ConsoleOutputStream outputStream;
    private final ConsoleErrorOutputStream errorStream;

    private int fixedTextEnd;

    /**
     * Creates a user interface console.
     */
    public Console() {
        super();
        textArea = new ConsoleTextArea();
        scrollPane = new JScrollPane(textArea);

        inputStream = new ConsoleInputStream();
        outputStream = new ConsoleOutputStream(this);
        errorStream = new ConsoleErrorOutputStream(this);

        fixedTextEnd = 0;
        KeyboardFocusManager.getCurrentKeyboardFocusManager().addKeyEventDispatcher(this::keyEventDispatched);

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

        editorTheme.applyThemeScrollbar(scrollPane.getVerticalScrollBar());
        editorTheme.applyThemeScrollbar(scrollPane.getHorizontalScrollBar());
    }

    /**
     * Resets the content in the console and its streams.
     */
    public void reset() {
        textArea.setText("");
        inputStream.resetBuffer();
        fixedTextEnd = 0;
    }

    /**
     * Gets the string remaining after the fixed text.
     *
     * @return Gets the string remaining after the fixed text.
     */
    private String getRemainingString() {
        try {
            return textArea.getText(fixedTextEnd, textArea.getText().length() - fixedTextEnd);
        } catch (BadLocationException ignored) {
        }
        return "";
    }

    /**
     * Handles the key-even dispatched event. Ensures that a user cannot overwrite or add to printed text.
     *
     * @param keyEvent the event corresponding to the key pressed/typed/released.
     * @return true if no further processing should be done on the key event.
     */
    private boolean keyEventDispatched(KeyEvent keyEvent) {
        if (KeyboardFocusManager.getCurrentKeyboardFocusManager().getFocusOwner() == textArea) {
            char c = keyEvent.getKeyChar();
            int pos = Math.min(textArea.getCaretPosition(), textArea.getSelectionStart());
            if (pos < fixedTextEnd) {
                if (isRealCharacter(c)) {
                    // Trying to edit text that has already been submitted
                    keyEvent.consume();
                }
            } else if (textArea.getCaretPosition() <= fixedTextEnd && c == '\b'
                    && textArea.getSelectionStart() == textArea.getSelectionEnd()) {
                // in case of a backspace on the newline, discard it
                keyEvent.consume();
            } else if (keyEvent.getID() == KeyEvent.KEY_TYPED && c == '\n' && pos >= fixedTextEnd) {
                // Legal newline character
                String toBuffer = getRemainingString();
                inputStream.addToBuffer(toBuffer);
                fixedTextEnd += toBuffer.length();
            } else {
                textArea.setCharacterAttributes(getForegroundAttributeSet(), true);
            }
        }
        return false;
    }

    /**
     * Gets the attribute set corresponding to the theme's foreground.
     *
     * @return the attribute set corresponding to the theme's foreground.
     */
    private static AttributeSet getForegroundAttributeSet() {
        StyleContext style = StyleContext.getDefaultStyleContext();
        return style.addAttribute(SimpleAttributeSet.EMPTY, StyleConstants.Foreground,
                Window.getInstance().getTheme().foreground());
    }

    /**
     * Writes text to the console with a given color.
     *
     * @param text  the text to write.
     * @param color the color of the text.
     */
    private void writeTextWithColor(String text, Color color) {
        StyleContext style = StyleContext.getDefaultStyleContext();
        AttributeSet attributeSet = style.addAttribute(SimpleAttributeSet.EMPTY, StyleConstants.Foreground, color);

        try {
            textArea.getDocument().insertString(fixedTextEnd, text, attributeSet);
            fixedTextEnd += text.length();
            textArea.setCaretPosition(textArea.getText().length());
        } catch (BadLocationException ignored) {
        }
    }

    /**
     * Writes text as if it were going to System.out.
     *
     * @param text the text to write.
     */
    void writeTextFromSystemOut(String text) {
        // TODO maybe use a different color for user input and console
        writeTextWithColor(text, Window.getInstance().getTheme().comment());
    }

    /**
     * Writes text as if it were going to System.err.
     *
     * @param text the text to write.
     */
    void writeTextFromSystemError(String text) {
        writeTextWithColor(text, Window.getInstance().getTheme().red());
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

    private static boolean isRealCharacter(char c) {
        return (c >= 0x04 && c <= 0x7F) || Character.isWhitespace(c);
    }
}
