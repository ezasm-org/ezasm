package com.ezasm.gui.console;

import com.ezasm.gui.Window;
import com.ezasm.gui.util.IThemeable;
import com.ezasm.gui.util.Theme;

import javax.swing.*;
import javax.swing.text.*;
import java.awt.*;
import java.awt.event.*;

public class Console extends JPanel implements IThemeable {

    private final ConsoleTextArea textArea;
    private final JScrollPane scrollPane;

    private final ConsoleInputStream inputStream;
    private final ConsoleOutputStream outputStream;
    private final ConsoleErrorOutputStream errorStream;

    private int fixedTextEnd;

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

    @Override
    public void applyTheme(Font font, Theme theme) {
        textArea.setForeground(theme.foreground());
        textArea.setBackground(theme.background());
        textArea.setSelectionColor(theme.selection());
        textArea.setSelectedTextColor(theme.foreground());
        textArea.setCaretColor(theme.foreground());
        textArea.setFont(font);

        theme.applyThemeScrollbar(scrollPane.getVerticalScrollBar());
        theme.applyThemeScrollbar(scrollPane.getHorizontalScrollBar());
    }

    public void reset() {
        textArea.setText("");
        inputStream.resetBuffer();
        fixedTextEnd = 0;
    }

    private String getRemainingString() {
        try {
            return textArea.getText(fixedTextEnd, textArea.getText().length() - fixedTextEnd);
        } catch (BadLocationException ignored) {
        }
        return "";
    }

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

    private static AttributeSet getForegroundAttributeSet() {
        StyleContext style = StyleContext.getDefaultStyleContext();
        return style.addAttribute(SimpleAttributeSet.EMPTY, StyleConstants.Foreground,
                Window.getInstance().getTheme().foreground());
    }

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

    void writeTextFromSystemOut(String text) {
        // TODO maybe use a different color for user input and console
        writeTextWithColor(text, Window.getInstance().getTheme().comment());
    }

    void writeTextFromSystemError(String text) {
        writeTextWithColor(text, Window.getInstance().getTheme().red());
    }

    public ConsoleInputStream getInputStream() {
        return inputStream;
    }

    public ConsoleOutputStream getOutputStream() {
        return outputStream;
    }

    public ConsoleErrorOutputStream getErrorStream() {
        return errorStream;
    }

    private static boolean isRealCharacter(char c) {
        return (c >= 0x04 && c <= 0x7F) || Character.isWhitespace(c);
    }
}
