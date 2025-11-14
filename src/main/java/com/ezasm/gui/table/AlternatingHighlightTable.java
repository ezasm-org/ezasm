package com.ezasm.gui.table;

import java.awt.Color;
import java.awt.Component;
import java.awt.Font;
import java.util.HashSet;
import java.util.Set;

import javax.swing.table.TableCellRenderer;

import com.ezasm.gui.util.EditorTheme;
import com.ezasm.gui.util.IThemeable;

/**
 * An extension of JTable with colors that alternate based on whether a row has an even index or an odd index, and
 * highlights specific rows
 */
public class AlternatingHighlightTable extends AlternatingColorTable implements IThemeable {

    private Color highlight;
    private Color changedTextColor;
    private final Set<Integer> flashingRows = new HashSet<>();

    public AlternatingHighlightTable(EditorTheme theme) {
        super(theme);
    }

    @Override
    public Component prepareRenderer(TableCellRenderer renderer, int row, int column) {
        Component component = super.prepareRenderer(renderer, row, column);

        if (flashingRows.contains(row)) {
            component.setBackground(highlight);
            component.setForeground(changedTextColor);
        } else {
            component.setForeground(this.getForeground());
        }

        return component;
    }

    @Override
    public void applyTheme(Font font, EditorTheme theme) {
        super.applyTheme(font, theme);

        this.highlight = theme.yellow();
        this.changedTextColor = Color.RED;
        repaint();
    }

    public void flashRow(int row) {
        flashingRows.add(row);
        repaint(getCellRect(row, 0, true));
    }

    public void clearFlash(int row) {
        flashingRows.remove(row);
        repaint(getCellRect(row, 0, true));
    }

    public void clearAllFlashes() {
        flashingRows.clear();
        repaint();
    }
}
