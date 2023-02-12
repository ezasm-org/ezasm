package com.ezasm.gui;

import javax.swing.*;

import com.ezasm.Theme;
import org.fife.ui.rsyntaxtextarea.AbstractTokenMakerFactory;
import org.fife.ui.rsyntaxtextarea.RSyntaxTextArea;
import org.fife.ui.rsyntaxtextarea.SyntaxConstants;
import org.fife.ui.rsyntaxtextarea.TokenMakerFactory;
import org.fife.ui.rtextarea.RTextScrollPane;

import java.awt.*;

/**
 * The editor pane within the GUI. Allows the user to type code or edit loaded code.
 */
public class EditorPane extends JPanel implements IThemeable {

    private final RSyntaxTextArea textArea;
    private final RTextScrollPane scrollPane;
    private static final Dimension MIN_SIZE = new Dimension(600, 400);
    private static final Dimension MAX_SIZE = new Dimension(600, 2000);

    /**
     * Creates a text edit field using RSyntaxTextArea features.
     */
    public EditorPane() {
        super();

        AbstractTokenMakerFactory atmf = (AbstractTokenMakerFactory) TokenMakerFactory.getDefaultInstance();
        atmf.putMapping("text/ezasm", "com.ezasm.gui.EzASMTokenMaker");
        textArea = new RSyntaxTextArea();
        textArea.setSyntaxEditingStyle("text/ezasm");
        //textArea.setSyntaxEditingStyle(SyntaxConstants.SYNTAX_STYLE_ASSEMBLER_X86);
        textArea.setCodeFoldingEnabled(false);

        textArea.setTabSize(2);

        scrollPane = new RTextScrollPane(textArea);
        scrollPane.setLineNumbersEnabled(true);
        scrollPane.setMinimumSize(textArea.getSize());
        scrollPane.setPreferredSize(textArea.getPreferredSize());
        setMaximumSize(MAX_SIZE);
        setLayout(new BorderLayout());
        add(scrollPane);
    }

    /**
     * Applies the proper theming to the editor area
     */
    public void applyTheme(Font font, Theme theme) {
        textArea.setBackground(theme.getBackground());
        textArea.setForeground(theme.getForeground());
        textArea.setCaretColor(theme.getForeground());
        textArea.setCurrentLineHighlightColor(theme.getCurrentLine());
        scrollPane.setBackground(theme.getBackground());
        scrollPane.setForeground(theme.getForeground());
        scrollPane.setFont(font);
        textArea.setFont(font);
        textArea.setEditable(true);
        textArea.setLineWrap(false);
        textArea.setMinimumSize(MIN_SIZE);
        textArea.setDisabledTextColor(Color.DARK_GRAY);
    }

    /**
     * Sets the editable state of the text field based on the boolean given.
     *
     * @param value true to be editable, false to not be editable.
     */
    public void setEditable(boolean value) {
        textArea.setEnabled(value);
    }

    /**
     * Gets the truth value of whether the editor can be typed in.
     *
     * @return true if the editor can be typed in currently, false otherwise.
     */
    public boolean getEditable() {
        return textArea.isEnabled();
    }

    /**
     * Gets the text content of the text editor.
     *
     * @return the text content of the text editor.
     */
    public String getText() {
        return textArea.getText();
    }

    /**
     * Sets the text of the editor to the given content.
     *
     * @param content the text to set the text within the editor to.
     */
    public void setText(String content) {
        textArea.setText(content);
    }
}
