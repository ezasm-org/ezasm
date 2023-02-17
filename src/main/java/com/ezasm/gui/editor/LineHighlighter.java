package com.ezasm.gui.editor;

import com.ezasm.parsing.Lexer;

import javax.swing.text.*;
import java.awt.*;
import java.util.ArrayList;

public class LineHighlighter extends DefaultHighlighter.DefaultHighlightPainter {
    /**
     * Start & end line numbers keep track of indicies of lines that are counted by the PC.
     */
    public final ArrayList<Integer> startLineNums = new ArrayList<Integer>();
    public final ArrayList<Integer> endLineNums = new ArrayList<Integer>();

    /**
     * Constructor
     *
     * @param color    The color of the highlighter. Should be the current theme RUN_COLOR
     * @param textComp The text component where highlights are applied should be the EditorPane
     */
    public LineHighlighter(Color color, JTextComponent textComp) {
        super(color);
        try {
            Document doc = textComp.getDocument();
            String text = doc.getText(0, doc.getLength());
            int startLine = 0;
            int endLine = text.indexOf("\n");
            if (endLine == -1) {
                endLine = text.length();
            }
            while (endLine != -1) {
                String line = text.substring(startLine, endLine);
                if (Lexer.validProgramLine(line)) {
                    startLineNums.add(startLine);
                    endLineNums.add(endLine);
                }
                startLine = endLine + 1;
                endLine = text.indexOf("\n", endLine + 1);
            }
            startLineNums.add(startLine);
            endLineNums.add(text.length());
        } catch (BadLocationException e) {
            e.printStackTrace();
        }

    }

    /**
     * For a given text component, highlight a certain line (ignore non program lines)
     *
     * @param textComp    the text component to highlight
     * @param line_number which program line to highlight
     */
    public void highlight(JTextComponent textComp, int line_number) {
        try {
            textComp.getHighlighter().addHighlight(startLineNums.get(line_number), endLineNums.get(line_number), this);
            textComp.repaint();
        } catch (BadLocationException e) {
            e.printStackTrace();
        }

    }

    /**
     * Clear a text component of all line highlights
     *
     * @param textComp the text component to clear
     */
    public static void removeHighlights(JTextComponent textComp) {
        Highlighter highlight = textComp.getHighlighter();
        Highlighter.Highlight[] highlights = highlight.getHighlights();

        for (int i = 0; i < highlights.length; i++) {
            if (highlights[i].getPainter() instanceof LineHighlighter) {
                highlight.removeHighlight(highlights[i]);
            }
        }
        textComp.repaint();
    }
}