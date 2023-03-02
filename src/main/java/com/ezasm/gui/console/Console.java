package com.ezasm.gui.console;

import com.ezasm.gui.util.IThemeable;
import com.ezasm.gui.util.Theme;

import javax.swing.*;
import javax.swing.text.BadLocationException;
import java.awt.*;
import java.awt.event.*;

public class Console extends JPanel implements IThemeable {

    private final ConsoleTextArea textArea;
    private final JScrollPane scrollPane;
    private int fixedTextEnd;

    public Console() {
        super();
        textArea = new ConsoleTextArea();
        scrollPane = new JScrollPane(textArea);
        fixedTextEnd = 0;
        KeyboardFocusManager.getCurrentKeyboardFocusManager().addKeyEventDispatcher(this::keyEventDispatched);

        setLayout(new BorderLayout());
        add(scrollPane);
    }

    @Override
    public void applyTheme(Font font, Theme theme) {
        textArea.setCurrentLineHighlightColor(theme.currentLine());
        textArea.setForeground(theme.foreground());
        textArea.setBackground(theme.background());
        textArea.setSelectionColor(theme.selection());
        textArea.setSelectedTextColor(theme.foreground());
        textArea.setCaretColor(theme.foreground());
        textArea.setFont(font);

        theme.applyThemeScrollbar(scrollPane.getVerticalScrollBar());
        theme.applyThemeScrollbar(scrollPane.getHorizontalScrollBar());
    }

    private static boolean isRealCharacter(char c) {
        return (c >= 0x04 && c <= 0x7F) || Character.isWhitespace(c);
    }

    private boolean keyEventDispatched(KeyEvent keyEvent) {
        if(KeyboardFocusManager.getCurrentKeyboardFocusManager().getFocusOwner() == textArea) {
            char c = keyEvent.getKeyChar();
            int pos = Math.min(textArea.getCaretPosition(), textArea.getSelectionStart());
            if (pos < fixedTextEnd) {
                if (isRealCharacter(c)) {
                    // Trying to edit text that has already been submitted
                    keyEvent.consume();
                }
            } else if (textArea.getCaretPosition() <= fixedTextEnd && c == '\b' && textArea.getSelectionStart() == textArea.getSelectionEnd()) {
                // in case of a backspace on the newline, discard it
                keyEvent.consume();
            } else if (c == '\n') {
                // Legal newline character
                String toBuffer = "";
                try {
                    toBuffer = textArea.getText(fixedTextEnd, textArea.getText().length() - fixedTextEnd);
                } catch (BadLocationException ignored) { // Should never happen
                }
                fixedTextEnd += toBuffer.length();
            }
        }
        return false;
    }
}
