package com.ezasm.gui.console;

import com.ezasm.gui.util.IThemeable;
import com.ezasm.gui.util.Theme;
import com.ezasm.instructions.implementation.TerminalInstructions;

import javax.swing.*;
import javax.swing.text.BadLocationException;
import java.awt.*;
import java.awt.event.*;
import java.io.PrintStream;

public class Console extends JPanel implements IThemeable {

    private final ConsoleTextArea textArea;
    private final JScrollPane scrollPane;

    private final ConsoleInputStream inputStream;
    private final ConsoleOutputStream outputStream;

    private int fixedTextEnd;

    public Console() {
        super();
        textArea = new ConsoleTextArea();
        scrollPane = new JScrollPane(textArea);

        inputStream = new ConsoleInputStream();
        outputStream = new ConsoleOutputStream(this);

        System.setIn(inputStream);
        System.setOut(new PrintStream(outputStream));
        System.setErr(new PrintStream(outputStream));
        
        TerminalInstructions.streams().setInputStream(inputStream);
        TerminalInstructions.streams().setOutputStream(outputStream);

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

    public void reset() {
        textArea.setText("");
        inputStream.resetBuffer();
        fixedTextEnd = 0;
    }

    String getRemainingString() {
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
            }
            if (textArea.getCaretPosition() <= fixedTextEnd && c == '\b'
                    && textArea.getSelectionStart() == textArea.getSelectionEnd()) {
                // in case of a backspace on the newline, discard it
                keyEvent.consume();
            }
            if (keyEvent.getID() == KeyEvent.KEY_TYPED && c == '\n' && pos >= fixedTextEnd ) {
                // Legal newline character
                String toBuffer = getRemainingString();
                inputStream.addToBuffer(toBuffer);
                fixedTextEnd += toBuffer.length();
            }
        }
        return false;
    }

    void insert(String string) {
        textArea.insert(string, fixedTextEnd);
        fixedTextEnd += string.length();
    }

    public ConsoleInputStream getInputStream() {
        return inputStream;
    }

    public ConsoleOutputStream getOutputStream() {
        return outputStream;
    }

    private static boolean isRealCharacter(char c) {
        return (c >= 0x04 && c <= 0x7F) || Character.isWhitespace(c);
    }
}
