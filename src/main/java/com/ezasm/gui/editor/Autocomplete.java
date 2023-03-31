package com.ezasm.gui.editor;

import org.fife.ui.rsyntaxtextarea.RSyntaxTextArea;

import java.awt.event.ActionEvent;
import java.util.Collections;
import java.util.List;

import javax.swing.AbstractAction;
import javax.swing.JTextField;
import javax.swing.SwingUtilities;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.text.BadLocationException;
import java.util.regex.*;

public class Autocomplete implements DocumentListener {

    private static enum Mode {
        INSERT, COMPLETION
    };

    private RSyntaxTextArea textField;
    private final List<String> keywords;
    private Mode mode = Mode.INSERT;
    private Pattern line_finder = Pattern.compile("[ \\t]*[_a-zA-Z][_a-zA-Z0-9]*:");

    public Autocomplete(RSyntaxTextArea textField, List<String> keywords) {
        this.textField = textField;
        this.keywords = keywords;
        Collections.sort(keywords);
    }

    @Override
    public void changedUpdate(DocumentEvent ev) {
    }

    @Override
    public void removeUpdate(DocumentEvent ev) {
        keywords.clear();
        String content = textField.getText();
        // find the new keywords and add them
        Matcher to_add = line_finder.matcher(content);
        while (to_add.find()) {
            String label = to_add.group().strip().replace(" ", "").replace(":", "");
            keywords.add(label);
        }
        Collections.sort(keywords);
    }

    @Override
    public void insertUpdate(DocumentEvent ev) {
        String content = textField.getText();
        int pos = ev.getOffset();
        int len = ev.getLength();
        // Get the original line without the change
        int line_start = 0;
        int line_end = content.length();

        for (int i = pos - 1; i >= 0; i--) {
            if (content.charAt(i) == '\n') {
                line_start = i + 1;
            }
        }
        for (int i = pos + len; i < content.length(); i++) {
            if (content.charAt(i) == '\n') {
                line_end = i;
            }
        }
        String old_line = content.substring(line_start, pos) + content.substring(pos + len, line_end);

        // Find the old keywords and delete them
        Matcher to_remove = line_finder.matcher(old_line);
        while (to_remove.find()) {
            String label = to_remove.group().strip().replace(" ", "").replace(":", "");
            keywords.remove(label);
        }

        String new_line = content.substring(line_start, line_end);
        // find the new keywords and add them
        Matcher to_add = line_finder.matcher(new_line);
        while (to_add.find()) {
            String label = to_add.group().strip().replace(" ", "").replace(":", "");
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
                // We cannot modify Document from within notification,
                // so we submit a task that does the change later
                SwingUtilities.invokeLater(new CompletionTask(completion, pos + 1));
            }
        } else {
            // Nothing found
            mode = Mode.INSERT;
        }
    }

    public class CommitAction extends AbstractAction {
        /**
         *
         */
        private static final long serialVersionUID = 5794543109646743416L;

        @Override
        public void actionPerformed(ActionEvent ev) {
            if (mode == Mode.COMPLETION) {
                int pos = textField.getSelectionEnd();
                StringBuffer sb = new StringBuffer(textField.getText());
                sb.insert(pos, " ");
                textField.setText(sb.toString());
                textField.setCaretPosition(pos + 1);
                mode = Mode.INSERT;

            } else {
                textField.replaceSelection("\t");
            }
        }
    }

    private class CompletionTask implements Runnable {
        private String completion;
        private int position;

        CompletionTask(String completion, int position) {
            this.completion = completion;
            this.position = position;
        }

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
     *
     * @param s   The given string to search
     * @param pos the position to exapnd from
     * @return The full line the given position is in
     */
    private String expand_to_line(String s, int pos) {
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
