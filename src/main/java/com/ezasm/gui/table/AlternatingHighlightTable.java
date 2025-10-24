package com.ezasm.gui.table;

import java.awt.Color;
import java.awt.Component;
import java.awt.Font;
import java.util.HashSet;
import java.util.Set;

import javax.swing.JTable;
import javax.swing.table.DefaultTableCellRenderer;

import com.ezasm.gui.util.EditorTheme;
import com.ezasm.gui.util.IThemeable;

/**
 * An extension of JTable with colors that alternate based on whether a row has an even index or an odd index, and
 * highlights specific rows
 */
public class AlternatingHighlightTable extends JTable implements IThemeable {

    private Color evens;
    private Color odds;
    private Color highlight;
    private Color changedTextColor;
    private final Set<Integer> flashingRows = new HashSet<>();

    public AlternatingHighlightTable(EditorTheme theme) {
        super();
        installRenderer();
        setSelectionBackground(evens);
        setSelectionForeground(theme.foreground());
    }

    private void installRenderer() {
        setDefaultRenderer(Object.class, new DefaultTableCellRenderer() {
            private final DefaultTableCellRenderer defaultRenderer = new DefaultTableCellRenderer();

            @Override
            public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected,
                    boolean hasFocus, int row, int column) {
                Component comp = defaultRenderer.getTableCellRendererComponent(table, value, false, hasFocus, row,
                        column);

                if (comp instanceof DefaultTableCellRenderer) {
                    ((DefaultTableCellRenderer) comp).setOpaque(true);
                }

                // Background logic
                if (flashingRows.contains(row)) {
                    comp.setBackground(highlight);
                    comp.setForeground(changedTextColor);
                } else {
                    comp.setBackground(row % 2 == 0 ? evens : odds);
                    comp.setForeground(table.getForeground());

                }

                return comp;
            }
        });
    }

    @Override
    public void applyTheme(Font font, EditorTheme theme) {
        EditorTheme.applyFontTheme(this, font, theme);
        EditorTheme.applyFontTheme(tableHeader, font, theme);

        TableHeaderRenderer newTableHeaderRenderer = new TableHeaderRenderer(this);
        newTableHeaderRenderer.applyTheme(font, theme);
        getTableHeader().setDefaultRenderer(newTableHeaderRenderer);

        this.evens = theme.currentLine();
        this.odds = theme.background();
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
