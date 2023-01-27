package com.ezasm.gui;

import javax.swing.text.*;
import java.awt.*;

public class LineHighlighter extends DefaultHighlighter.DefaultHighlightPainter {
    public LineHighlighter(Color color) {
        super(color);
    }

    public static void highlight(JTextComponent textComp, int line_number, LineHighlighter painter) {
        try {
//            removeHighlights(textComp);

            Highlighter hilite = textComp.getHighlighter();
            Document doc = textComp.getDocument();
            String text = doc.getText(0, doc.getLength());
            int start_line = 0;
            int end_line = text.indexOf("\n",0);
            for (int n = 1; n < line_number; n++) {
                start_line = end_line;
                end_line = text.indexOf("\n",end_line+1);
            }
            System.out.print(start_line);
            System.out.print(" ");
            System.out.println(end_line);
            hilite.addHighlight(start_line, end_line, painter);
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
