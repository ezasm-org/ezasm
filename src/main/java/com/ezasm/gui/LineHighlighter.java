package com.ezasm.gui;

import com.ezasm.parsing.Lexer;

import javax.swing.text.*;
import java.awt.*;
import java.util.ArrayList;

public class LineHighlighter extends DefaultHighlighter.DefaultHighlightPainter {
    private ArrayList<Integer> line_numbers = new ArrayList<Integer>();
    public LineHighlighter(Color color, JTextComponent textComp) {
        super(color);
        try {
            Document doc = textComp.getDocument();
            String text = doc.getText(0, doc.getLength());

            int start_line = 0;
            int end_line = text.indexOf("\n",-1);
            while (true) {
                String line = text.substring(start_line,end_line);
                if (Lexer.validProgramLine(line)) {
                    line_numbers.add(end_line);
                }
                start_line = end_line + 1;
                end_line = text.indexOf("\n",end_line+1);
                if (end_line == -1) {
                    line_numbers.add(end_line);
                    break;
                }
            }
        } catch (BadLocationException e) {
            e.printStackTrace();
        }
    }

    public void highlight(JTextComponent textComp, int line_number) {

        try {
            Document doc = textComp.getDocument();
            String text = doc.getText(0, doc.getLength());


            int s = -1;
            if (line_number >1) {
                s = line_numbers.get(line_number-2) + 1;
            } else {
                s = 0;
            }

            int e = line_numbers.get(line_number);

            textComp.getHighlighter().addHighlight(s, e, this);
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
    }
}
