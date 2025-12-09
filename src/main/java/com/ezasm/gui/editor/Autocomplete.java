package com.ezasm.gui.editor;

import java.awt.event.ActionEvent;
import java.io.Serial;
import java.util.Collections;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.swing.AbstractAction;
import javax.swing.SwingUtilities;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;

import org.fife.ui.rsyntaxtextarea.RSyntaxTextArea;

/**
 * Autocomplete listener for EzASM label names in the text editor.
 * Scans the document for labels (identifiers ending with ':') and completes them as the user types.
 * Maintains a sorted keyword list and uses binary search for prefix matching.
 * Two modes: INSERT (normal typing) and COMPLETION (label completion active).
 */
public class Autocomplete implements DocumentListener {

    /**
     * Represents the autocomplete mode state.
     * INSERT: normal typing, Tab inserts a tab character.
     * COMPLETION: label completion active, Tab accepts it.
     */
    private enum Mode {
        INSERT, COMPLETION
    };

    private final RSyntaxTextArea textField;
    private final List<String> keywords;
    private final Pattern lineRegex = Pattern.compile("[ \\t]*[_a-zA-Z][_a-zA-Z0-9]*:");
    private Mode mode = Mode.INSERT;

    /**
     * Constructs an Autocomplete listener.
     * @param textField the RSyntaxTextArea to apply autocomplete to.
     * @param keywords a mutable list of keywords (labels) to maintain and search.
     */
    public Autocomplete(RSyntaxTextArea textField, List<String> keywords) {
        this.textField = textField;
        this.keywords = keywords;
        Collections.sort(keywords);
    }

    /**
     * Called when document style attributes change. No-op for this listener.
     * @param ev the DocumentEvent (unused).
     */
    @Override
    public void changedUpdate(DocumentEvent ev) {
    }

    /**
     * Called when text is removed from the document.
     * Rebuilds the keyword list by scanning the entire document for labels.
     * @param ev the DocumentEvent (position and length of removed text).
     */
    @Override
    public void removeUpdate(DocumentEvent ev) {
        keywords.clear();
        String content = textField.getText();
        // find the new keywords and add them
        Matcher to_add = lineRegex.matcher(content);
        while (to_add.find()) {
            String label = to_add.group().strip().replace(" ", "").replace(":", "");
            keywords.add(label);
        }
        Collections.sort(keywords);
    }

    /**
     * Called when text is inserted into the document.
     * Updates the keyword list and attempts to complete the current word if it matches a label prefix.
     * @param ev the DocumentEvent (position, length, and type of inserted text).
     */
    @Override
    public void insertUpdate(DocumentEvent ev) {
        String content = textField.getText();
        int pos = ev.getOffset();
        int len = ev.getLength();
        // Get the original line without the change
        int lineStart = 0;
        int lineEnd = content.length();

        for (int i = pos - 1; i >= 0; i--) {
            if (content.charAt(i) == '\n') {
                lineStart = i + 1;
            }
        }
        for (int i = pos + len; i < content.length(); i++) {
            if (content.charAt(i) == '\n') {
                lineEnd = i;
            }
        }
        String previousLine = content.substring(lineStart, pos) + content.substring(pos + len, lineEnd);

        // Find the old keywords and delete them
        Matcher toRemove = lineRegex.matcher(previousLine);
        while (toRemove.find()) {
            String label = toRemove.group().strip().replace(" ", "").replace(":", "");
            keywords.remove(label);
        }

        String new_line = content.substring(lineStart, lineEnd);
        // find the new keywords and add them
        Matcher toAdd = lineRegex.matcher(new_line);
        while (toAdd.find()) {
            String label = toAdd.group().strip().replace(" ", "").replace(":", "");
            keywords.add(label);
        }
        Collections.sort(keywords);

        // Find where the word starts
        int w;
        for (w = pos; w >= 0; w--) {
            if (!Character.isLetterOrDigit(content.charAt(w)) && content.charAt(w) != '_') {
                break;
            }
        }

        // Too few chars
        if (pos - w < 2) {
            return;
        }

        String prefix = content.substring(w + 1).toLowerCase();
        int n = Collections.binarySearch(keywords, prefix);
        if (n < 0 && -n <= keywords.size()) {
            String match = keywords.get(-n - 1);
            if (match.startsWith(prefix)) {
                // A completion is found
                String completion = match.substring(pos - w);
                // We cannot modify Document from within notification, so we submit a task that does the change later
                SwingUtilities.invokeLater(new CompletionTask(completion, pos + 1));
            }
        } else {
            // Nothing found
            mode = Mode.INSERT;
        }
    }

    /**
     * Action triggered by the Tab key.
     * If mode is COMPLETION: accepts the completion and inserts a space, then switches to INSERT.
     * Otherwise: inserts a tab character.
     */
    public class CommitAction extends AbstractAction {

        @Serial
        private static final long serialVersionUID = -2173840931874903219L;

        @Override
        public void actionPerformed(ActionEvent ev) {
            if (mode == Mode.COMPLETION) {
                int pos = textField.getSelectionEnd();
                StringBuilder sb = new StringBuilder(textField.getText());
                sb.insert(pos, " ");
                textField.setText(sb.toString());
                textField.setCaretPosition(pos + 1);
                mode = Mode.INSERT;

            } else {
                textField.replaceSelection("\t");
            }
        }
    }

    /**
     * Runnable task that inserts the completion text into the document on the EDT.
     * Sets caret and selection to highlight the inserted text, then switches to COMPLETION mode.
     */
    private class CompletionTask implements Runnable {
        private final String completion;
        private final int position;

        /**
         * Constructs a CompletionTask.
         * @param completion the text to insert (remainder of matched label).
         * @param position the position in the document to insert at.
         */
        CompletionTask(String completion, int position) {
            this.completion = completion;
            this.position = position;
        }

        /**
         * Inserts the completion text and selects it.
         * Switches the mode to COMPLETION.
         */
        @Override
        public void run() {
            StringBuffer sb = new StringBuffer(textField.getText());
            sb.insert(position, completion);
            textField.setText(sb.toString());
            textField.setCaretPosition(position + completion.length());
            textField.moveCaretPosition(position);
            mode = Mode.COMPLETION;
        }
    }

    /**
     * Finds the next line in a given string starting at the given position.
     *
     * @param s   The given string to search.
     * @param pos the position to expand from.
     * @return The full line the given position is in.
     */
    private static String getLineAt(String s, int pos) {
        int start = 0;
        int end = 0;
        for (int i = pos; i >= 0; i--) {
            if (s.charAt(i) == '\n') {
                start = i + 1;
            }
        }

        for (int i = pos; i <= s.length(); i++) {
            if (s.charAt(i) == '\n') {
                end = i;
            }
        }

        return s.substring(start, end);
    }
}
