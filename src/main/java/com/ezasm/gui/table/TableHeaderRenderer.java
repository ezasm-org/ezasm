package com.ezasm.gui.table;

import com.ezasm.gui.util.EditorTheme;
import com.ezasm.gui.util.IThemeable;

import javax.swing.*;
import javax.swing.border.Border;
import javax.swing.table.DefaultTableCellRenderer;
import java.awt.*;

/**
 * Column header renderer to bypass windows LAF issues.
 */
public class TableHeaderRenderer extends DefaultTableCellRenderer implements IThemeable {

    /**
     * Sets up basic information about the renderer based on the given table.
     *
     * @param table the table to take default settings from.
     */
    public TableHeaderRenderer(JTable table) {
        setHorizontalAlignment(CENTER);
        setBorder(UIManager.getBorder("TableHeader.cellBorder"));
        setForeground(table.getForeground());
        setBackground(table.getBackground());
        setFont(table.getFont());
        setOpaque(true);
    }

    /**
     * Adds a border in the cell rendering step.
     *
     * @param table      the table rendering.
     * @param value      the value to assign at the cell.
     * @param isSelected whether the cell is selected.
     * @param hasFocus   whether the cell has focus.
     * @param row        the row of the cell.
     * @param column     the column of the cell.
     * @return the generated Component.
     */
    @Override
    public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus,
            int row, int column) {
        JComponent component = (JComponent) super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row,
                column);
        component.setBorder(UIManager.getBorder("TableHeader.cellBorder"));
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
        setForeground(editorTheme.foreground());
        setBackground(editorTheme.background());
        setFont(font);
    }
}
