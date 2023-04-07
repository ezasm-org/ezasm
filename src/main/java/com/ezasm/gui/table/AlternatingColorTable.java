package com.ezasm.gui.table;

import com.ezasm.gui.util.EditorTheme;
import com.ezasm.gui.util.IThemeable;

import javax.swing.*;
import javax.swing.table.TableCellRenderer;
import java.awt.*;

/**
 * An extension of JTable with colors that alternate based on whether a row has an even index or an odd index.
 */
public class AlternatingColorTable extends JTable implements IThemeable {

    private Color evens;
    private Color odds;

    /**
     * Constructs a JTable where the color alternates based on whether the line number is even or odd given a theme.
     *
     * @param editorTheme the theme to select colors from.
     */
    public AlternatingColorTable(EditorTheme editorTheme) {
        this(editorTheme.currentLine(), editorTheme.background());
    }

    /**
     * Constructs a JTable where the color alternates based on whether the line number is even or odd given a theme.
     *
     * @param evens the background color for even numbered rows.
     * @param odds  the background color for odd numbered rows.
     */
    public AlternatingColorTable(Color evens, Color odds) {
        super();
        this.evens = evens;
        this.odds = odds;
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
        if (row % 2 == 0) {
            component.setBackground(evens);
        } else {
            component.setBackground(odds);
        }
        return component;
    }

    /**
     * Applies the given theme and font to the component itself, the tabbed pane, and all subcomponents of the tabbed
     * pane. If the components are IThemable, uses their IThemable#applyTheme method to do so.
     *
     * @param font        the font to apply.
     * @param editorTheme the theme to apply.
     */
    @Override
    public void applyTheme(Font font, EditorTheme editorTheme) {
        EditorTheme.applyFontTheme(this, font, editorTheme);
        EditorTheme.applyFontTheme(tableHeader, font, editorTheme);
        evens = editorTheme.currentLine();
        odds = editorTheme.background();
    }
}
