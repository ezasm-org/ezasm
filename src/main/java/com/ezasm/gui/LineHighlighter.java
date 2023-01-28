package com.ezasm.gui;

import com.ezasm.parsing.Lexer;
import com.ezasm.parsing.Line;
import com.ezasm.simulation.Simulator;

import javax.swing.text.*;
import java.awt.*;
import java.util.Map;

import static java.lang.Math.max;

public class LineHighlighter extends DefaultHighlighter.DefaultHighlightPainter {
    public LineHighlighter(Color color) {
        super(color);
    }

    public static void highlight(JTextComponent textComp, LineHighlighter painter, int line_number) {
        try {
            Document doc = textComp.getDocument();
            String text = doc.getText(0, doc.getLength());

            int n = -1;
            int start_line = 0;
            int end_line = text.indexOf("\n",-1);;


            removeHighlights(textComp);

            while (n <= line_number) {
                String line = text.substring(start_line,end_line);
                if (Lexer.validProgramLine(line)) {
                    n++;
                    if (n == line_number) {
                        textComp.getHighlighter().addHighlight(start_line, end_line, painter);
                        break;
                    }
                }
                start_line = end_line + 1;
                end_line = text.indexOf("\n",end_line+1);
                if (end_line == -1) {
                    end_line = text.length();
                }
            }
        } catch (BadLocationException e) {
            e.printStackTrace();
        }


    }

    public static void removeHighlights(JTextComponent textComp) {
        Highlighter hilite = textComp.getHighlighter();
        Highlighter.Highlight[] hilites = hilite.getHighlights();

        for (int i = 0; i < hilites.length; i++) {
            if (hilites[i].getPainter() instanceof LineHighlighter) {
                hilite.removeHighlight(hilites[i]);
            }
        }
    }
}
