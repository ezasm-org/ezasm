package com.ezasm.gui.table;

import com.ezasm.gui.util.EditorTheme;
import com.ezasm.gui.util.IThemeable;

import javax.swing.*;
import javax.swing.table.TableCellRenderer;
import java.awt.*;

public class AlternatingColorTable extends JTable implements IThemeable {

    private Color evens;
    private Color odds;

    public AlternatingColorTable(EditorTheme editorTheme) {
        this.evens = editorTheme.currentLine();
        this.odds = editorTheme.background();
    }

    @Override
    public Component prepareRenderer(TableCellRenderer renderer, int row, int column) {
        Component component = super.prepareRenderer(renderer, row, column);
        if (row % 2 == 0) {
            component.setBackground(evens);
        } else {
            component.setBackground(odds);
        }
        return component;
    }

    @Override
    public void applyTheme(Font font, EditorTheme editorTheme) {
        EditorTheme.applyFontTheme(this, font, editorTheme);
        EditorTheme.applyFontTheme(tableHeader, font, editorTheme);
        evens = editorTheme.currentLine();
        odds = editorTheme.background();
    }
}
