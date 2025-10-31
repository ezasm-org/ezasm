package com.ezasm.gui.editor;

import com.ezasm.gui.util.PatchedRSyntaxTextArea;
import com.ezasm.parsing.Lexer;
import com.ezasm.simulation.Registers;
import com.ezasm.simulation.Simulator;
import com.ezasm.gui.Window;

import javax.swing.text.*;
import java.awt.*;
import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;

public class LineHighlighter extends DefaultHighlighter.DefaultHighlightPainter {

    public final HashMap<String, ArrayList<Integer>> lineStartOffsets;
    public final HashMap<String, ArrayList<Integer>> lineEndOffsets;

    /**
     * Constructs the line highlighter.
     *
     * @param color      The color of the highlighter.
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
     * @param simulator the program simulator.
     */
    public void highlight(Simulator simulator) {
        int lineNumber = (int) simulator.getRegisters().getRegister(Registers.PC).getLong();
        int fid = (int) simulator.getRegisters().getRegister(Registers.FID).getLong();

        String currentFile = simulator.getFile(fid);
        int currentFileIndex = Window.getInstance().getEditorPanes().indexOfFile(currentFile);
        if (currentFileIndex == -1) { // check to see if anonymous
            File f = new File(currentFile);
            currentFile = f.getName();
        }
        currentFileIndex = Window.getInstance().getEditorPanes().indexOfFile(currentFile);
        if (currentFileIndex == -1) { // file must not exist
            return;
        }

        if (!lineStartOffsets.containsKey(currentFile)) {
            parseFileLines(Window.getInstance().getEditorPanes().getComponentAt(currentFileIndex));
        }

        Window.getInstance().getEditorPanes().switchToFile(currentFile);
        PatchedRSyntaxTextArea textArea = Window.getInstance().getEditor().getTextArea();

        final String fileForEDT = currentFile;
        final int lineForEDT = lineNumber;
        final PatchedRSyntaxTextArea textAreaForEDT = textArea;

        
        javax.swing.SwingUtilities.invokeLater(() -> {
            synchronized (this) {
                try {
                    int start = lineStartOffsets.get(fileForEDT).get(lineForEDT);
                    int end = lineEndOffsets.get(fileForEDT).get(lineForEDT);
                    // sanity checks
                    if (start < 0 || end < 0 || start > end)
                        return;
                    textAreaForEDT.getHighlighter().addHighlight(start, end, this);
                    if (textAreaForEDT.getCaret() != null) {
                        textAreaForEDT.setCaretPosition(start);
                    }
                    textAreaForEDT.repaint();
                } catch (BadLocationException ex) {
                    // Log the location info for debugging and print stack trace
                    System.out.println("BadLocationException at file=" + fileForEDT + " lineNumber=" + lineForEDT);
                    ex.printStackTrace();
                }
            }
        });
    }
}
