package com.ezasm.gui.editor;

import javax.swing.*;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.text.Highlighter;

import com.ezasm.gui.Window;
import com.ezasm.gui.util.EditorTheme;
import com.ezasm.gui.util.IThemeable;
import com.ezasm.gui.util.PatchedRSyntaxTextArea;
import com.ezasm.simulation.Registers;
import org.fife.ui.autocomplete.*;
import org.fife.ui.rsyntaxtextarea.*;
import org.fife.ui.rtextarea.RTextScrollPane;
import org.fife.ui.rtextarea.ToolTipSupplier;

import java.awt.*;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Objects;

import static com.ezasm.gui.util.EditorTheme.applyFontAndTheme;

import static com.ezasm.gui.editor.LineHighlighter.removeHighlights;

/**
 * The editor pane within the GUI. Allows the user to type code or edit loaded code.
 */

public class EzEditorPane extends JPanel implements IThemeable {
    private final PatchedRSyntaxTextArea textArea;
    // private AutoCompletion ac;
    // private JCheckBoxMenuItem cellRenderingItem;
    // private JCheckBoxMenuItem alternateRowColorsItem;
    // private JCheckBoxMenuItem showDescWindowItem;
    // private JCheckBoxMenuItem paramAssistanceItem;
    private final RTextScrollPane scrollPane;
    private LineHighlighter highlighter;
    private String openFilePath;
    private boolean fileSaved;

    private static final String EZASM_TOKEN_MAKER_NAME = "text/ezasm";

    private static final Dimension MIN_SIZE = new Dimension(600, 400);
    private static final Dimension MAX_SIZE = new Dimension(600, 2000);
    Autocomplete autoComplete;
    private static final String COMMIT_ACTION = "commit";

    private ArrayList<String> keywords = new ArrayList<>();

    /**
     * Creates a text edit field using RSyntaxTextArea features.
     */
    public EzEditorPane() {
        super();

        ((AbstractTokenMakerFactory) TokenMakerFactory.getDefaultInstance()).putMapping(EZASM_TOKEN_MAKER_NAME,
                EzTokenMaker.class.getName());
        textArea = new PatchedRSyntaxTextArea();
        textArea.setSyntaxEditingStyle(EZASM_TOKEN_MAKER_NAME);
        textArea.setTabSize(2);
        textArea.setCodeFoldingEnabled(false);
        textArea.getDocument().addDocumentListener(new EditorDocumentListener());

        fileSaved = true;

        // CompletionProvider provider = createCompletionProvider();
        // Install auto-completion onto our text area.
        // ac = new AutoCompletion(provider);
        // ac.setListCellRenderer(new EzASMCellRenderer());
        // ac.setShowDescWindow(true);
        // ac.setParameterAssistanceEnabled(true);

        // ac.setAutoCompleteEnabled(true);
        // ac.setAutoActivationEnabled(true);
        // ac.setAutoCompleteSingleChoices(true);
        // ac.setAutoActivationDelay(800);
        // ac.setTriggerKey(KeyStroke.getKeyStroke("ctrl SPACE"));
        // ac.install(textArea);

        scrollPane = new RTextScrollPane(textArea);
        scrollPane.setLineNumbersEnabled(true);
        scrollPane.setMinimumSize(textArea.getSize());
        scrollPane.setPreferredSize(textArea.getPreferredSize());

        setMaximumSize(MAX_SIZE);
        setLayout(new BorderLayout());
        add(scrollPane);

        highlighter = new LineHighlighter(Window.getInstance().getTheme().yellow(), textArea);

        textArea.setFocusTraversalKeysEnabled(false);

        keywords = new ArrayList<String>();
        autoComplete = new Autocomplete(textArea, keywords);
        textArea.getDocument().addDocumentListener(autoComplete);

        textArea.getInputMap().put(KeyStroke.getKeyStroke("TAB"), COMMIT_ACTION);
        textArea.getActionMap().put(COMMIT_ACTION, autoComplete.new CommitAction());

    }

    /**
     * Themes the syntax text area according to the given font and theme.
     */
    private void themeSyntaxTextArea(Font font, EditorTheme newEditorTheme) {
        try {
            // Load a good default theme for things we are not setting
            org.fife.ui.rsyntaxtextarea.Theme theme = org.fife.ui.rsyntaxtextarea.Theme
                    .load(getClass().getResourceAsStream("/org/fife/ui/rsyntaxtextarea/themes/idea.xml"));

            // Background and caret theme
            theme.bgColor = newEditorTheme.background();
            theme.marginLineColor = newEditorTheme.background();
            theme.caretColor = newEditorTheme.foreground();

            // Selection does not override the foreground color
            theme.selectionBG = newEditorTheme.selection();
            theme.useSelectionFG = false;

            // Line number background theme
            theme.gutterBackgroundColor = newEditorTheme.modifyAwayFromBackground(newEditorTheme.background());
            theme.gutterBorderColor = theme.gutterBackgroundColor;

            // Line number theme
            theme.lineNumberFont = font.getFontName();
            theme.lineNumberFontSize = font.getSize();
            theme.lineNumberColor = newEditorTheme.modifyAwayFromBackground(newEditorTheme.background(), 3);
            theme.currentLineNumberColor = newEditorTheme.modifyAwayFromBackground(newEditorTheme.background(), 5);
            theme.currentLineHighlight = newEditorTheme.currentLine();

            // Token-specific theme
            theme.scheme.getStyle(Token.NULL).foreground = newEditorTheme.foreground();
            theme.scheme.getStyle(Token.IDENTIFIER).foreground = newEditorTheme.foreground();
            theme.scheme.getStyle(Token.SEPARATOR).foreground = newEditorTheme.foreground();
            theme.scheme.getStyle(Token.WHITESPACE).foreground = newEditorTheme
                    .modifyAwayFromBackground(newEditorTheme.currentLine());
            theme.scheme.getStyle(Token.COMMENT_EOL).foreground = newEditorTheme.comment();
            theme.scheme.getStyle(Token.LITERAL_CHAR).foreground = newEditorTheme.green();
            theme.scheme.getStyle(Token.LITERAL_STRING_DOUBLE_QUOTE).foreground = newEditorTheme.green();
            theme.scheme.getStyle(Token.LITERAL_NUMBER_DECIMAL_INT).foreground = newEditorTheme.orange();
            theme.scheme.getStyle(Token.RESERVED_WORD).foreground = newEditorTheme.cyan();
            theme.scheme.getStyle(Token.VARIABLE).foreground = newEditorTheme.pink();
            theme.scheme.getStyle(Token.ERROR_IDENTIFIER).foreground = newEditorTheme.red();
            theme.scheme.getStyle(Token.ERROR_NUMBER_FORMAT).foreground = newEditorTheme.red();

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
    public void applyTheme(Font font, EditorTheme editorTheme) {
        themeSyntaxTextArea(font, editorTheme);
        setFont(textArea, font);
        applyFontAndTheme(scrollPane, font, editorTheme);
        editorTheme.applyThemeScrollbar(scrollPane.getHorizontalScrollBar());
        editorTheme.applyThemeScrollbar(scrollPane.getVerticalScrollBar());
        recolorHighlights(editorTheme);
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
        textArea.setCaretPosition(0);
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

    public boolean getFileSaved() {
        return this.fileSaved;
    }

    public void setFileSaved(boolean value) {
        this.fileSaved = value;
    }

    /**
     * Highlights a given line number and clears old highlight
     */
    public void updateHighlight() {
        removeHighlights(textArea);
        highlighter.highlight(textArea, Window.getInstance().getSimulator());
    }

    /**
     * Reset a highlighter by removing all highlights and reinit-ing with a new color. Resets line counts. Should be
     * called each program start
     */
    public void resetHighlighter() {
        removeHighlights(textArea);
        highlighter = new LineHighlighter(Window.getInstance().getTheme().yellow(), textArea);
    }

    /**
     * Recolor the current highlight in accordance with the provided theme
     *
     * @param editorTheme the theme to recolor to
     */
    private void recolorHighlights(EditorTheme editorTheme) {
        // Store the old highlights
        Highlighter highlight = textArea.getHighlighter();
        Highlighter.Highlight[] highlights = highlight.getHighlights();

        // clear old highlights
        removeHighlights(textArea);

        // init new highlighter with new color
        if (highlights.length > 0) {
            highlighter = new LineHighlighter(editorTheme.yellow(), textArea);
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

    /**
     * <<<<<<< HEAD:src/main/java/com/ezasm/gui/editor/EditorPane.java Create a simple provider that adds some
     * Java-related completions.
     */
    // private CompletionProvider createCompletionProvider() {
    //
    // // A DefaultCompletionProvider is the simplest concrete implementation
    // // of CompletionProvider. This provider has no understanding of
    // // language semantics. It simply checks the text entered up to the
    // // caret position for a match against known completions. This is all
    // // that is needed in the majority of cases.
    // DefaultCompletionProvider provider = new DefaultCompletionProvider();
    //
    // // Add completions for all Java keywords. A BasicCompletion is just
    // // a straightforward word completion.
    // provider.addCompletion(new BasicCompletion(provider, "abstract"));
    // provider.addCompletion(new BasicCompletion(provider, "assert"));
    // provider.addCompletion(new BasicCompletion(provider, "break"));
    // provider.addCompletion(new BasicCompletion(provider, "case"));
    // // ... etc ...
    // provider.addCompletion(new BasicCompletion(provider, "transient"));
    // provider.addCompletion(new BasicCompletion(provider, "try"));
    // provider.addCompletion(new BasicCompletion(provider, "void"));
    // provider.addCompletion(new BasicCompletion(provider, "volatile"));
    // provider.addCompletion(new BasicCompletion(provider, "while"));
    //
    // // Add a couple of "shorthand" completions. These completions don't
    // // require the input text to be the same thing as the replacement text.
    // provider.addCompletion(new ShorthandCompletion(provider, "sysout",
    // "System.out.println(", "System.out.println("));
    // provider.addCompletion(new ShorthandCompletion(provider, "syserr",
    // "System.err.println(", "System.err.println("));
    //
    // return provider;
    //
    // }
    /**
     * Set tab size from the setting changes
     *
     * @param size the size of tab to change to
     */
    public void resizeTabSize(int size) {
        textArea.setTabSize(size);
    }
}
