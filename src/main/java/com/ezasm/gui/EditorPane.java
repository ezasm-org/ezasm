package com.ezasm.gui;

import javax.swing.*;
import javax.swing.text.BadLocationException;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.text.Highlighter;
import javax.swing.undo.UndoManager;

import com.ezasm.Theme;

import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

import static com.ezasm.gui.LineHighlighter.removeHighlights;

/**
 * The editor pane within the GUI. Allows the user to type code or edit loaded code.
 */
public class EditorPane extends JPanel implements IThemeable {
    LineHighlighter highlighter;
    public static final Color HIGHLIGHT_COLOR = Color.YELLOW;
    private final JTextArea textArea;
    private final LineNumber lineNumbers;
    private final LineNumberModelImpl model = new LineNumberModelImpl();
    private static final Dimension MIN_SIZE = new Dimension(600, 400);
    private static final Dimension MAX_SIZE = new Dimension(600, 2000);

    /**
     * Creates a text edit field with an "Undo Manager" to undo the user's actions with CTRL + Z or redo those undid
     * actions with CTRL + SHIFT + Z or CTRL + Y.
     */
    public EditorPane() {
        super();

        textArea = new JTextArea();
        lineNumbers = new LineNumber(model);

        textArea.setTabSize(2);
        UndoManager manager = new UndoManager();
        textArea.getDocument().addUndoableEditListener(manager);
        textArea.getDocument().addDocumentListener(new DocumentListener() {
            @Override
            public void changedUpdate(DocumentEvent e) {
                lineNumbers.adjustWidth();
            }

            @Override
            public void removeUpdate(DocumentEvent e) {
                lineNumbers.adjustWidth();
            }

            @Override
            public void insertUpdate(DocumentEvent e) {
                lineNumbers.adjustWidth();
            }

        });
        textArea.addKeyListener(new KeyListener() {
            @Override
            public void keyTyped(KeyEvent keyEvent) {
            }

            @Override
            public void keyPressed(KeyEvent keyEvent) {
                int mod = keyEvent.getModifiersEx();
                if ((mod & KeyEvent.CTRL_DOWN_MASK) == KeyEvent.CTRL_DOWN_MASK) {
                    // The "control" button is pressed
                    int key = keyEvent.getKeyCode();
                    if (key == KeyEvent.VK_Z) {
                        if ((mod & KeyEvent.SHIFT_DOWN_MASK) == KeyEvent.SHIFT_DOWN_MASK) {
                            // ctrl + shift + z : redo
                            if (manager.canRedo()) {
                                manager.redo();
                            }
                        } else if (manager.canUndo()) {
                            // ctrl + z : undo
                            manager.undo();
                        }
                    } else if (key == KeyEvent.VK_Y) {
                        // ctrl + y : redo
                        if (manager.canRedo()) {
                            manager.redo();
                        }
                    }
                }
            }

            @Override
            public void keyReleased(KeyEvent keyEvent) {
            }
        });

        JScrollPane scrollPane = new JScrollPane(textArea);
        scrollPane.setRowHeaderView(lineNumbers);
        scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
        scrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
        scrollPane.setMinimumSize(textArea.getSize());
        scrollPane.setPreferredSize(textArea.getPreferredSize());
        setMaximumSize(MAX_SIZE);
        setLayout(new BorderLayout());
        add(scrollPane);
    }

    private class LineNumberModelImpl implements ILineNumberModel {
        @Override
        public int getNumberLines() {
            return textArea.getLineCount();
        }

        @Override
        public Rectangle getLineRect(int line) {
            try {
                return textArea.modelToView2D(textArea.getLineStartOffset(line)).getBounds();
            } catch (BadLocationException e) {
                e.printStackTrace();
                return new Rectangle();
            }
        }
    }

    /**
     * Applies the proper theming to the editor area
     */
    public void applyTheme(Font font, Theme theme) {
        textArea.setBackground(theme.getBackground());
        textArea.setForeground(theme.getForeground());
        textArea.setCaretColor(theme.getForeground());
        lineNumbers.setBackground(theme.getCurrentLine());
        lineNumbers.setForeground(theme.getForeground().darker());
        lineNumbers.setFont(font);
        textArea.setFont(font);
        textArea.setEditable(true);
        textArea.setLineWrap(false);
        textArea.setMinimumSize(MIN_SIZE);
        textArea.setDisabledTextColor(Color.DARK_GRAY);


        // Store the old highlights
        Highlighter highlight = textArea.getHighlighter();
        Highlighter.Highlight[] highlights = highlight.getHighlights();

        // clear old highlights
        removeHighlights(textArea);

        // init new highlighter with new color
        if (highlights.length > 0) {
            highlighter = new LineHighlighter(theme.getRunLine(), textArea);
        }

        // add new highlights with the new color
        for (int i = 0; i < highlights.length; i++) {
            int start = highlights[i].getStartOffset();
            int end = highlights[i].getEndOffset();
            try {
                textArea.getHighlighter().addHighlight(start, end, highlighter);
            } catch(Exception e) {
                e.printStackTrace();
            }
            textArea.repaint();
        }
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
     * Highlights a given line number and clears old highlight
     *
     * @param line the line to highlight
     */
    public void updateHighlight(int line) {
        removeHighlights(textArea);
        if (line >= 0)
            highlighter.highlight(textArea, line);
    }

    /**
     * Reset a highlighter by removing all highlights
     * and reinit-ing with a new color.
     * Resets line counts.
     * Should be called each program start
     */
    public void resetHighlighter() {
        removeHighlights(textArea);
        highlighter = new LineHighlighter(Window.currentTheme().getRunLine(), textArea);
    }
}
