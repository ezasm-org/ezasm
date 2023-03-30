package com.ezasm.gui.table;

import com.ezasm.gui.util.EditorTheme;
import com.ezasm.gui.util.IThemeable;

import javax.swing.*;
import java.awt.*;

public class RowHeaderRenderer extends JLabel implements ListCellRenderer<Object>, IThemeable {

    private final JTable table;

    public RowHeaderRenderer(JTable table) {
        this.table = table;
        setHorizontalAlignment(CENTER);
        setBorder(table.getBorder());
        setForeground(table.getForeground());
        setBackground(table.getBackground());
        setFont(table.getFont());
    }

    @Override
    public Component getListCellRendererComponent(JList list, Object value, int index, boolean isSelected, boolean cellHasFocus) {
        if (value == null) {
            setText("");
        } else {
            setText(value.toString());
        }
        return this;
    }

    @Override
    public void applyTheme(Font font, EditorTheme editorTheme) {
        EditorTheme.applyFontAndTheme(this, font, editorTheme);
    }
}