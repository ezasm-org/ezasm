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


    /**
     * Constructs a JTable where the color alternates based on whether the line number is even or odd given a theme.
     *
     * @param editorTheme the theme to select colors from.
     */
    public AlternatingHighlightTable(EditorTheme theme) {
        super(theme);
    }

    /**
     * Gets the data necessary to render the cell, then returns the prepared cell's component.
     *
     * @param renderer the <code>TableCellRenderer</code> to prepare.
     * @param row      the row of the cell to render, where 0 is the first row.
     * @param column   the column of the cell to render, where 0 is the first column.
     * @return the cell component specified.
     */
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

    /**
     * Applies the given theme and font to the component itself, the tabbed pane, and all subcomponents of the tabbed
     * pane. If the components are IThemable, uses their IThemable#applyTheme method to do so. Also, if a subcomponent
     * should be highlighted, the appropriate theme colors are applied and the table is refreshed.
     *
     * @param font        the font to apply.
     * @param editorTheme the theme to apply.
     */
    @Override
    public void applyTheme(Font font, EditorTheme theme) {
        super.applyTheme(font, theme);

        this.highlight = theme.yellow();
        this.changedTextColor = Color.RED;
        repaint();
    }

    /**
     * Adds the given row to the subcomponents that should be highlighted
     *
     * @param row        a row to be highlighted
     */
    public void flashRow(int row) {
        flashingRows.add(row);
        repaint(getCellRect(row, 0, true));
    }

     /**
     * Removes the given row from the subcomponents that should be highlighted
     *
     * @param row        a row to be unhighlighted
     */
    public void clearFlash(int row) {
        flashingRows.remove(row);
        repaint(getCellRect(row, 0, true));
    }

     /**
     * After all steps have finished, clears all highlighted rolws
     *
     */
    public void clearAllFlashes() {
        flashingRows.clear();
        repaint();
    }
}
