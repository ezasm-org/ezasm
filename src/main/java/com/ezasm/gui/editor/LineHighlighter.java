package com.ezasm.gui.editor;

import com.ezasm.parsing.Lexer;
import com.ezasm.simulation.Registers;
import com.ezasm.simulation.Simulator;
import com.ezasm.gui.Window;

import javax.swing.text.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;

public class LineHighlighter extends DefaultHighlighter.DefaultHighlightPainter {

    public final HashMap<String, ArrayList<Integer>> lineStartOffsets;
    public final HashMap<String, ArrayList<Integer>> lineEndOffsets;

    /**
     * Constructs the line highlighter.
     *
     * @param color    The color of the highlighter.
     * @param editorPane The text component where highlights are applied should be the EditorPane.
     */
    public LineHighlighter(Color color, EzEditorPane editorPane) {
        super(color);
        lineStartOffsets = new HashMap<>();
        lineEndOffsets = new HashMap<>();
        parseFileLines(editorPane);
    }

    private void parseFileLines(EzEditorPane editorPane) {
        ArrayList<Integer> lineNumberCursorStarts = new ArrayList<>();
        ArrayList<Integer> lineNumberCursorEnds = new ArrayList<>();

        String text = editorPane.getText();

        int offset = 0;
        for (String line : Arrays.stream(text.split("\\n")).toList()) {
            int nextOffset = offset + line.length();
            if (Lexer.validProgramLine(line)) {
                lineNumberCursorStarts.add(offset);
                lineNumberCursorEnds.add(nextOffset);
            }
            offset = nextOffset + 1;
        }

        lineStartOffsets.put(editorPane.getOpenFilePath(), lineNumberCursorStarts);
        lineEndOffsets.put(editorPane.getOpenFilePath(), lineNumberCursorEnds);
    }

    /**
     * For a given text component, highlight a certain line (ignoring non program lines).
     *
     * @param editorPane  the text component to highlight.
     * @param simulator the program simulator.
     */
    public void highlight(EzEditorPane editorPane, Simulator simulator) {
        int lineNumber = (int) simulator.getRegisters().getRegister(Registers.PC).getLong();
        int fid = (int) simulator.getRegisters().getRegister(Registers.FID).getLong();

        String currentFile = simulator.getFile(fid);
        int currentFileIndex = Window.getInstance().getEditorPanes().indexOfFile(currentFile);
        if (currentFileIndex == -1) {
            return;
        }

        if (!lineStartOffsets.containsKey(currentFile)) {
            parseFileLines(Window.getInstance().getEditorPanes().getComponentAt(currentFileIndex));
        }

        //Window.getInstance().getEditor().resetHighlighter(); // This doesn't work
        Window.getInstance().getEditorPanes().switchToFile(currentFile);

        try {
            editorPane.getTextArea().getHighlighter().addHighlight(lineStartOffsets.get(currentFile).get(lineNumber), lineEndOffsets.get(currentFile).get(lineNumber), this);
            editorPane.getTextArea().setCaretPosition(lineStartOffsets.get(currentFile).get(lineNumber));
            editorPane.repaint();
        } catch (BadLocationException e) {
            e.printStackTrace();
        }
    }

    /**
     * Clear a text component of all line highlights.
     *
     * @param editorPane the text component to clear.
     */
    public static void removeHighlights(EzEditorPane editorPane) {
        Highlighter highlight = editorPane.getTextArea().getHighlighter();
        Highlighter.Highlight[] highlights = highlight.getHighlights();

        for (int i = 0; i < highlights.length; i++) {
            if (highlights[i].getPainter() instanceof LineHighlighter) {
                highlight.removeHighlight(highlights[i]);
            }
        }
        editorPane.repaint();
    }
}
