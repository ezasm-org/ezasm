package com.ezasm.gui.console;

import com.ezasm.gui.util.IThemeable;
import com.ezasm.gui.util.EditorTheme;
import com.ezasm.gui.util.NullOutputStream;

import javax.swing.*;
import javax.swing.text.*;
import java.awt.*;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.io.PrintStream;

/**
 * Represents a GUI console text box which a user will type into. To be used by the Console. Patches an issue with
 * pasting that causes uncontrollable system error text. Allows for colored text to be inserted. User input is only
 * confirmed when the user presses enter.
 */
public class ConsoleTextArea extends JTextPane implements IThemeable {

    private final Console console;
    private Color userInputColor;
    private int fixedTextEnd;

    /**
     * Constructs the text area to be used in a Console.
     */
    public ConsoleTextArea(Console console) {
        super();
        this.console = console;
        this.userInputColor = Color.BLACK;
        this.fixedTextEnd = 0;
        configureKeyListener();
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

        userInputColor = editorTheme.foreground();
    }

    /**
     * Resets the content in the console and its streams.
     */
    public void reset() {
        setText("");
        fixedTextEnd = 0;
    }

    /**
     * Gets the string remaining after the fixed text.
     *
     * @return Gets the string remaining after the fixed text.
     */
    private String getRemainingString() {
        try {
            return getText(fixedTextEnd, getText().length() - fixedTextEnd + 1);
        } catch (BadLocationException ignored) {
        }
        return "";
    }

    /**
     * Gets the attribute set corresponding to the theme's foreground.
     *
     * @return the attribute set corresponding to the theme's foreground.
     */
    private static AttributeSet getColoredAttributeSet(Color color) {
        StyleContext style = StyleContext.getDefaultStyleContext();
        return style.addAttribute(SimpleAttributeSet.EMPTY, StyleConstants.Foreground, color);
    }

    /**
     * Writes text to the console with a given color.
     *
     * @param newText the text to write.
     * @param color   the color of the text.
     */
    public void writeTextWithColor(String newText, Color color) {
        try {
            getDocument().insertString(fixedTextEnd, newText, getColoredAttributeSet(color));
            fixedTextEnd += newText.length();
            setCaretPosition(getText().length());
        } catch (BadLocationException ignored) {
        }
    }

    /**
     * Checks if the given character is a typable character.
     *
     * @param c the character to check.
     * @return true if the given character is a typable character, false otherwise.
     */
    private static boolean isRealCharacter(char c) {
        return Character.isDefined(c);
    }

    /**
     * Configures the key-listener to discard irrelevant keypresses and keep final output from being modified.
     */
    private void configureKeyListener() {
        addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                discardIllegalCharacterTyped(e);
                int pos = getSelectionStart();
                if (pos >= fixedTextEnd && e.getKeyCode() == '\n') {
                    // Legal newline character entered, save previously given text as final input
                    String toBuffer = getRemainingString();
                    console.writeTextToInputStream(toBuffer);
                    fixedTextEnd += toBuffer.length();
                }
            }

            @Override
            public void keyTyped(KeyEvent e) {
                discardIllegalCharacterTyped(e);
                // Character typed was legal and will result in a character displayed:
                // ensure that it is entered with the proper foreground color
                if (isRealCharacter(e.getKeyChar())) {
                    setCharacterAttributes(getColoredAttributeSet(userInputColor), true);
                }
            }
        });
    }

    /**
     * Given a key event, consumes the key event if it should not be allowed to occur.
     *
     * @param e the key event to consider.
     */
    private void discardIllegalCharacterTyped(KeyEvent e) {
        char c = e.getKeyChar();
        int pos = getSelectionStart();
        if (e.getKeyCode() == KeyEvent.VK_C && e.isControlDown()) {
            // Copy attempt, allow this
            return;
        }
        if (pos < fixedTextEnd && isRealCharacter(c)) {
            // Trying to edit text that has already been submitted
            e.consume();
        } else if (pos <= fixedTextEnd && c == '\b') {
            // in case of a backspace on the protected newline, discard it
            e.consume();
        }
    }

}
