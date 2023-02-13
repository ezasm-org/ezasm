package com.ezasm.gui;

import com.ezasm.parsing.Lexer;

import javax.swing.text.*;
import java.awt.*;
import java.util.ArrayList;

import static com.ezasm.Config.THEME;

public class LineHighlighter extends DefaultHighlighter.DefaultHighlightPainter {
    private final ArrayList<Integer> startLineNums = new ArrayList<Integer>();
    private final ArrayList<Integer> endLineNums = new ArrayList<Integer>();

    public LineHighlighter(Color color, JTextComponent textComp) {
        super(color);
        try {
            Document doc = textComp.getDocument();
            String text = doc.getText(0, doc.getLength());

            int start_line = 0;
            int end_line = text.indexOf("\n", -1);
            while (true) {
                String line = text.substring(start_line, end_line);
                if (Lexer.validProgramLine(line)) {
                    startLineNums.add(start_line);
                    endLineNums.add(end_line);
                }
                start_line = end_line + 1;
                end_line = text.indexOf("\n", end_line + 1);
                if (end_line == -1) {
                    startLineNums.add(start_line);
                    endLineNums.add(text.length());
                    break;
                }
            }
        } catch (BadLocationException e) {
            e.printStackTrace();
        }

    }

    public void highlight(JTextComponent textComp, int line_number) {
        System.out.println(line_number);
        try {
            textComp.getHighlighter().addHighlight(startLineNums.get(line_number), endLineNums.get(line_number), this);
            textComp.repaint();
        } catch (BadLocationException e) {
            e.printStackTrace();
        }

    }

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
