package com.ezasm.gui.editor;

import javax.swing.*;
import javax.swing.text.Highlighter;

import com.ezasm.gui.Window;
import com.ezasm.gui.util.IThemeable;
import com.ezasm.simulation.Registers;
import org.fife.ui.rsyntaxtextarea.*;
import org.fife.ui.rtextarea.RTextScrollPane;

import java.awt.*;
import java.io.IOException;
import java.util.Objects;

import static com.ezasm.gui.util.Theme.applyFontAndTheme;

import static com.ezasm.gui.editor.LineHighlighter.removeHighlights;

/**
 * The editor pane within the GUI. Allows the user to type code or edit loaded code.
 */
public class EditorPane extends JPanel implements IThemeable {

    private final RSyntaxTextArea textArea;
    private final RTextScrollPane scrollPane;
    private LineHighlighter highlighter;
    private String openFilePath;

    private static final String EZASM_TOKEN_MAKER_NAME = "text/ezasm";

    private static final Dimension MIN_SIZE = new Dimension(600, 400);
    private static final Dimension MAX_SIZE = new Dimension(600, 2000);

    /**
     * Creates a text edit field using RSyntaxTextArea features.
     */
    public EditorPane() {
        super();

        ((AbstractTokenMakerFactory) TokenMakerFactory.getDefaultInstance()).putMapping(EZASM_TOKEN_MAKER_NAME,
                EzTokenMaker.class.getName());
        textArea = new RSyntaxTextArea();
        textArea.setSyntaxEditingStyle(EZASM_TOKEN_MAKER_NAME);
        textArea.setTabSize(2);
        textArea.setCodeFoldingEnabled(false);

        scrollPane = new RTextScrollPane(textArea);
        scrollPane.setLineNumbersEnabled(true);
        scrollPane.setMinimumSize(textArea.getSize());
        scrollPane.setPreferredSize(textArea.getPreferredSize());

        setMaximumSize(MAX_SIZE);
        setLayout(new BorderLayout());
        add(scrollPane);

        String theme = Window.getInstance().getConfig().getTheme();
        highlighter = new LineHighlighter(com.ezasm.gui.util.Theme.getTheme(theme).yellow(), textArea);
    }

    /**
     * Themes the syntax text area according to the given font and theme.
     */
    private void themeSyntaxTextArea(Font font, com.ezasm.gui.util.Theme newTheme) {
        try {
            // Load a good default theme for things we are not setting
            org.fife.ui.rsyntaxtextarea.Theme theme = org.fife.ui.rsyntaxtextarea.Theme
                    .load(getClass().getResourceAsStream("/org/fife/ui/rsyntaxtextarea/themes/idea.xml"));

            // Background and caret theme
            theme.bgColor = newTheme.background();
            theme.marginLineColor = newTheme.background();
            theme.caretColor = newTheme.foreground();

            // Selection does not override the foreground color
            theme.selectionBG = newTheme.selection();
            theme.useSelectionFG = false;

            // Line number background theme
            theme.gutterBackgroundColor = newTheme.modifyAwayFromBackground(newTheme.background());
            theme.gutterBorderColor = theme.gutterBackgroundColor;

            // Line number theme
            theme.lineNumberFont = font.getFontName();
            theme.lineNumberFontSize = font.getSize();
            theme.lineNumberColor = newTheme.modifyAwayFromBackground(newTheme.background(), 3);
            theme.currentLineNumberColor = newTheme.modifyAwayFromBackground(newTheme.background(), 5);
            theme.currentLineHighlight = newTheme.currentLine();

            // Token-specific theme
            theme.scheme.getStyle(Token.NULL).foreground = newTheme.foreground();
            theme.scheme.getStyle(Token.IDENTIFIER).foreground = newTheme.foreground();
            theme.scheme.getStyle(Token.SEPARATOR).foreground = newTheme.foreground();
            theme.scheme.getStyle(Token.WHITESPACE).foreground = newTheme
                    .modifyAwayFromBackground(newTheme.currentLine());
            theme.scheme.getStyle(Token.COMMENT_EOL).foreground = newTheme.comment();
            theme.scheme.getStyle(Token.LITERAL_CHAR).foreground = newTheme.green();
            theme.scheme.getStyle(Token.LITERAL_STRING_DOUBLE_QUOTE).foreground = newTheme.green();
            theme.scheme.getStyle(Token.LITERAL_NUMBER_DECIMAL_INT).foreground = newTheme.orange();
            theme.scheme.getStyle(Token.RESERVED_WORD).foreground = newTheme.cyan();
            theme.scheme.getStyle(Token.VARIABLE).foreground = newTheme.pink();
            theme.scheme.getStyle(Token.ERROR_IDENTIFIER).foreground = newTheme.red();
            theme.scheme.getStyle(Token.ERROR_NUMBER_FORMAT).foreground = newTheme.red();

            theme.apply(textArea);
            setFont(textArea, font);
            textArea.revalidate();
        } catch (IOException e) { // Never happens
            e.printStackTrace();
        }
    }

    /**
     * Set the font for all token types.
     *
     * @param textArea The text area to modify.
     * @param font     The font to use.
     */
    private static void setFont(RSyntaxTextArea textArea, Font font) {
        if (font != null) {
            SyntaxScheme ss = textArea.getSyntaxScheme();
            ss = (SyntaxScheme) ss.clone();
            for (int i = 0; i < ss.getStyleCount(); i++) {
                if (ss.getStyle(i) != null) {
                    ss.getStyle(i).font = font;
                }
            }
            textArea.setSyntaxScheme(ss);
            textArea.setFont(font);
        }
    }

    /**
     * Applies the proper theming to the editor area.
     */
    public void applyTheme(Font font, com.ezasm.gui.util.Theme theme) {
        themeSyntaxTextArea(font, theme);
        setFont(textArea, font);
        applyFontAndTheme(scrollPane, font, theme);
        theme.applyThemeScrollbar(scrollPane.getHorizontalScrollBar());
        theme.applyThemeScrollbar(scrollPane.getVerticalScrollBar());
        recolorHighlights(theme);
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

    /**
     * Gets the path to the file currently open in this editor.
     *
     * @return the path to the file currently open in this editor.
     */
    public String getOpenFilePath() {
        return Objects.requireNonNullElse(openFilePath, "");
    }

    /**
     * Sets the path to the file currently open in this editor.
     *
     * @param openFilePath the new path to the file currently open in this editor.
     */
    public void setOpenFilePath(String openFilePath) {
        this.openFilePath = openFilePath;
    }

    /**
     * Highlights a given line number and clears old highlight
     *
     * @param line the line to highlight
     */
    public void updateHighlight() {
        removeHighlights(textArea);
        int pc = (int) Window.getInstance().getSimulator().getRegisters().getRegister(Registers.PC).getLong();
        if (pc >= 0) {
            highlighter.highlight(textArea, pc);
        }
    }

    /**
     * Reset a highlighter by removing all highlights and reinit-ing with a new color. Resets line counts. Should be
     * called each program start
     */
    public void resetHighlighter() {
        removeHighlights(textArea);
        String theme = Window.getInstance().getConfig().getTheme();
        highlighter = new LineHighlighter(com.ezasm.gui.util.Theme.getTheme(theme).yellow(), textArea);
    }

    /**
     * Recolor the current highlight in accordance with the provided theme
     *
     * @param theme the theme to recolor to
     */
    private void recolorHighlights(com.ezasm.gui.util.Theme theme) {
        // Store the old highlights
        Highlighter highlight = textArea.getHighlighter();
        Highlighter.Highlight[] highlights = highlight.getHighlights();

        // clear old highlights
        removeHighlights(textArea);

        // init new highlighter with new color
        if (highlights.length > 0) {
            highlighter = new LineHighlighter(theme.yellow(), textArea);
        }

        // add new highlights with the new color
        for (int i = 0; i < highlights.length; i++) {
            int start = highlights[i].getStartOffset();
            int end = highlights[i].getEndOffset();
            try {
                textArea.getHighlighter().addHighlight(start, end, highlighter);
            } catch (Exception e) {
                e.printStackTrace();
            }
            textArea.repaint();
        }
    }
}
