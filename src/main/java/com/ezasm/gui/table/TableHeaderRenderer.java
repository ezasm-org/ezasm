package com.ezasm.gui.table;

import com.ezasm.gui.util.EditorTheme;
import com.ezasm.gui.util.IThemeable;

import javax.swing.*;
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
