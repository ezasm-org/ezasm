package com.ezasm.gui.editor;

import javax.swing.*;
import javax.swing.text.DefaultEditorKit;
import javax.swing.text.Highlighter;
import javax.swing.AbstractAction;
import java.awt.event.ActionEvent;

import com.ezasm.gui.Window;
import com.ezasm.gui.menubar.MenuActions;
import com.ezasm.gui.tabbedpane.EditorTabbedPane;
import com.ezasm.gui.tabbedpane.JClosableComponent;
import com.ezasm.gui.util.EditorTheme;
import com.ezasm.gui.util.IThemeable;
import com.ezasm.gui.util.PatchedRSyntaxTextArea;
import org.fife.ui.rsyntaxtextarea.*;
import org.fife.ui.rtextarea.RTextScrollPane;

import java.awt.*;
import java.io.IOException;
import java.util.ArrayList;

import static com.ezasm.gui.util.EditorTheme.applyFontThemeBorderless;

import static com.ezasm.gui.util.DialogFactory.promptYesNoCancelDialog;

import javax.swing.undo.UndoManager;

/**
 * The editor pane within the GUI. Allows the user to type code or edit loaded code.
 */
public class EzEditorPane extends JClosableComponent implements IThemeable {

    private final PatchedRSyntaxTextArea textArea;

    private final RTextScrollPane scrollPane;
    private LineHighlighter highlighter;
    private String openFilePath;
    private boolean fileSaved;

    private static final String EZASM_TOKEN_MAKER_NAME = "text/ezasm";

    private static final Dimension MAX_SIZE = new Dimension(600, 2000);
    Autocomplete autoComplete;
    private static final String COMMIT_ACTION = "commit";
    //Undo manager
    private UndoManager undoManager = new UndoManager();
    private String savedTextSnapshot = "";
    private JMenuItem undoMenuItem;
    private JMenuItem redoMenuItem;
    /**
     * Gets the undo manager associated with this editor.
     *
     * @return the UndoManager
     */
    public UndoManager getUndoManager() {
        return undoManager;
    }
    /**
     * Creates a text edit field using RSyntaxTextArea features.
     */
    public EzEditorPane() {
        super();

        ((AbstractTokenMakerFactory) TokenMakerFactory.getDefaultInstance()).putMapping(EZASM_TOKEN_MAKER_NAME,
                EzTokenMaker.class.getName());
        textArea = new PatchedRSyntaxTextArea();
        textArea.getDocument().addUndoableEditListener(undoManager);
        textArea.setSyntaxEditingStyle(EZASM_TOKEN_MAKER_NAME);
        textArea.setTabSize(2);
        textArea.setCodeFoldingEnabled(false);
        textArea.getDocument().addDocumentListener(new EditorDocumentListener(this));

        openFilePath = EditorTabbedPane.NEW_FILE_PREFIX;
        fileSaved = true;

        scrollPane = new RTextScrollPane(textArea);
        scrollPane.setLineNumbersEnabled(true);
        scrollPane.setMinimumSize(textArea.getSize());
        scrollPane.setPreferredSize(textArea.getPreferredSize());

        setMaximumSize(MAX_SIZE);
        setLayout(new BorderLayout());
        add(scrollPane);

        highlighter = new LineHighlighter(Window.getInstance().getTheme().yellow(), this);

        textArea.setFocusTraversalKeysEnabled(false);

        ArrayList<String> keywords = new ArrayList<String>();
        autoComplete = new Autocomplete(textArea, keywords);
        textArea.getDocument().addDocumentListener(autoComplete);

        textArea.getInputMap().put(KeyStroke.getKeyStroke("TAB"), COMMIT_ACTION);
        textArea.getActionMap().put(COMMIT_ACTION, autoComplete.new CommitAction());
        //keyboard shortcut for UNDO/REDO
        textArea.getInputMap().put(KeyStroke.getKeyStroke("control Z"), "Undo");
        textArea.setPopupMenu(createCustomPopupMenu());
        textArea.getActionMap().put("Undo", new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (undoManager.canUndo()) {
                    undoManager.undo();
                    checkIfDirty();
                    updateUndoRedoState();
                }
            }

            @Override
            public boolean isEnabled() {
                return undoManager.canUndo();
            }
        });

        textArea.getInputMap().put(KeyStroke.getKeyStroke("control Y"), "Redo");
        textArea.getActionMap().put("Redo", new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (undoManager.canRedo()) {
                    undoManager.redo();
                    checkIfDirty();
                    updateUndoRedoState();
                }
            }

            @Override
            public boolean isEnabled() {
                return undoManager.canRedo();
            }
        });

    }

    //keeping track of when I can undo and redo
    /**
     * Updates the enabled state of the Undo and Redo actions
     */
    public void updateUndoRedoState() {
        Action undoAction = textArea.getActionMap().get("Undo");
        Action redoAction = textArea.getActionMap().get("Redo");

        if (undoAction != null) {
            undoAction.setEnabled(undoManager.canUndo());
        }
        if (redoAction != null) {
            redoAction.setEnabled(undoManager.canRedo());
        }

        if (undoMenuItem != null) {
            undoMenuItem.setEnabled(undoManager.canUndo());
        }
        if (redoMenuItem != null) {
            redoMenuItem.setEnabled(undoManager.canRedo());
        }
    }

    //create our own pop-up menu
    /**
     * Creates and returns a custom right-click popup menu for the text editor.
     */
    private JPopupMenu createCustomPopupMenu() {
        JPopupMenu menu = new JPopupMenu();

        undoMenuItem = new JMenuItem("Undo");
        undoMenuItem.addActionListener(e -> {
            if (undoManager.canUndo()) {
                undoManager.undo();
                checkIfDirty();
                updateUndoRedoState();
            }
        });
        menu.add(undoMenuItem);

        redoMenuItem = new JMenuItem("Redo");
        redoMenuItem.addActionListener(e -> {
            if (undoManager.canRedo()) {
                undoManager.redo();
                checkIfDirty();
                updateUndoRedoState();
            }
        });
        menu.add(redoMenuItem);

        menu.addSeparator();

        JMenuItem copy = new JMenuItem("Copy");
        copy.addActionListener(e -> textArea.copy());
        menu.add(copy);

        JMenuItem paste = new JMenuItem("Paste");
        paste.addActionListener(e -> textArea.paste());
        menu.add(paste);

        JMenuItem cut = new JMenuItem("Cut");
        cut.addActionListener(e -> textArea.cut());
        menu.add(cut);

        menu.addSeparator();

        JMenuItem selectAll = new JMenuItem("Select All");
        selectAll.addActionListener(e -> textArea.selectAll());
        menu.add(selectAll);

        return menu;
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
        } catch (IOException ignored) { // Never happens
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
        applyFontThemeBorderless(scrollPane, font, editorTheme);
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
        undoManager.discardAllEdits();
        updateUndoRedoState();
        markSavedState();
    }

    /**
     * Gets the path to the file currently open in this editor.
     *
     * @return the path to the file currently open in this editor.
     */

    public String getOpenFilePath() {
        String out = openFilePath;
        if (openFilePath.startsWith(EditorTabbedPane.NEW_FILE_PREFIX)) {
            return out.substring(EditorTabbedPane.NEW_FILE_PREFIX.length());
        }
        return out;
    }

    /**
     * Returns true if the file is an anonymous file, false otherwise.
     *
     * @return true if the file is an anonymous file, false otherwise.
     */
    public boolean isFileAnonymous() {
        return openFilePath == null || openFilePath.startsWith(EditorTabbedPane.NEW_FILE_PREFIX);
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
     * Get whether the file has been saved.
     *
     * @return true if the file has been saved, false otherwise.
     */
    public boolean getFileSaved() {
        return this.fileSaved;
    }

    /**
     * Sets whether or not the file has been saved.
     *
     * @param value whether or not the file has been saved.
     */
    public void setFileSaved(boolean value) {
        this.fileSaved = value;
    }
    /**
     * this functions helps Marks the current text content as the saved state.
     * the function helps determines whether the file has been modified since last save.
     */
    public void markSavedState() {
        this.savedTextSnapshot = getText();
        setFileSaved(true);
        updateUndoRedoState();
    }
    /**
     * The function checks if the current text content differs from the last saved state.
     * If it does, marks the file as unsaved. Otherwise, marks it as saved.
     */
    public void checkIfDirty() {
        boolean isDirty = !getText().equals(savedTextSnapshot);
        setFileSaved(!isDirty);
    }

    /**
     * Highlights a given line number and clears old highlight
     */
    public void updateHighlight() {
        getTextArea().getHighlighter().removeAllHighlights();
        highlighter.highlight(Window.getInstance().getSimulator());
    }

    /**
     * Reset a highlighter by removing all highlights and reinit-ing with a new color. Resets line counts. Should be
     * called each program start
     */
    public void resetHighlighter() {
        getTextArea().getHighlighter().removeAllHighlights();
        highlighter = new LineHighlighter(Window.getInstance().getTheme().yellow(), this);
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
        highlight.removeAllHighlights();

        // init new highlighter with new color
        if (highlights.length > 0) {
            highlighter = new LineHighlighter(editorTheme.yellow(), this);
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
     * Set tab size from the setting changes
     *
     * @param size the size of tab to change to
     */
    public void resizeTabSize(int size) {
        textArea.setTabSize(size);
    }

    /**
     * Gets the internal text area.
     *
     * @return the internal text area.
     */
    public PatchedRSyntaxTextArea getTextArea() {
        return textArea;
    }

    /**
     * The closing action of an editor pane.
     *
     * @return whether to close the editor pane GUI element.
     */
    @Override
    public boolean close() {
        if (getFileSaved()) {
            return true;
        }
        if (getText().equals("") && isFileAnonymous()) {
            return true;
        }
        int resp = promptYesNoCancelDialog("Closing File",
                "You have unsaved changes in " + getOpenFilePath() + ", would you like to save them?");
        if (resp == JOptionPane.YES_OPTION) {
            return MenuActions.save();
        } else if (resp == JOptionPane.NO_OPTION) {
            return true;
        } else if (resp == JOptionPane.CANCEL_OPTION || resp == JOptionPane.CLOSED_OPTION) {
            return false;
        }
        return false;
    }
}

