package com.ezasm.gui.table;

import com.ezasm.gui.util.EditorTheme;
import com.ezasm.gui.util.IThemeable;

import javax.swing.*;
import javax.swing.table.JTableHeader;
import java.awt.*;

public class RowHeaderRenderer extends JLabel implements ListCellRenderer<Object>, IThemeable {

    private final JTable table;

    public RowHeaderRenderer(JTable table) {
        this.table = table;
        JTableHeader header = table.getTableHeader();
        setHorizontalAlignment(CENTER);
        setBorder(UIManager.getBorder("TableHeader.cellBorder"));
        setForeground(header.getForeground());
        setBackground(header.getBackground());
        setFont(header.getFont());
        setOpaque(true);
    }

    @Override
    public Component getListCellRendererComponent(JList list, Object value, int index, boolean isSelected,
            boolean cellHasFocus) {
        if (value == null) {
            setText("");
        } else {
            setText(value.toString());
        }
        return this;
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
