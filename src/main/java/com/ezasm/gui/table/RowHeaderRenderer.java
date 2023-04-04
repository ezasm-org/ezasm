package com.ezasm.gui.table;

import com.ezasm.gui.util.EditorTheme;
import com.ezasm.gui.util.IThemeable;

import javax.swing.*;
import javax.swing.table.JTableHeader;
import java.awt.*;

/**
 * A list cell renderer specified for rendering row headers on a given table.
 */
public class RowHeaderRenderer extends JLabel implements ListCellRenderer<Object>, IThemeable {

    public RowHeaderRenderer(JTable table) {
        JTableHeader header = table.getTableHeader();
        setHorizontalAlignment(CENTER);
        setBorder(UIManager.getBorder("TableHeader.cellBorder"));
        setForeground(header.getForeground());
        setBackground(header.getBackground());
        setFont(header.getFont());
        setOpaque(true);
    }

    /**
     * Returns a component configured for the given display options.
     *
     * @param list         The JList we're painting.
     * @param value        The value returned by list.getModel().getElementAt(index).
     * @param index        The cells index.
     * @param isSelected   True if the specified cell was selected.
     * @param cellHasFocus True if the specified cell has the focus.
     * @return the configured component.
     */
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
