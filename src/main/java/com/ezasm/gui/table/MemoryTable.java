package com.ezasm.gui.table;

import com.ezasm.gui.util.EditorTheme;
import com.ezasm.gui.util.IThemeable;
import com.ezasm.simulation.Memory;
import com.ezasm.util.RawData;

import javax.swing.*;
import javax.swing.table.DefaultTableCellRenderer;
import java.awt.*;

public class MemoryTable extends JPanel implements IThemeable {

    private final AlternatingColorTable table;
    private final JScrollPane scrollPane;
    private final JList<Object> rowHeader;

    private static final int ROWS = 32;
    private static final int COLUMNS = 16;

    private int offset;

    public MemoryTable(Memory memory) {
        super();
        this.table = new AlternatingColorTable(EditorTheme.Light);
        this.scrollPane = new JScrollPane(table);
        this.offset = memory.initialHeapPointer();
        table.setModel(new MemoryTableModel(memory, ROWS, COLUMNS));
        table.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);

        Object[] rows = new Object[ROWS];
        for (int i = 0; i < ROWS; ++i) {
            rows[i] = (new RawData(offset + (long) i * Memory.wordSize() * COLUMNS)).toHexString();
        }

        rowHeader = new JList<>(new SimpleListModel(rows));
        rowHeader.setCellRenderer(new RowHeaderRenderer(table));
        scrollPane.setRowHeaderView(rowHeader);

        scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
        scrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
        scrollPane.setWheelScrollingEnabled(true);

        setLayout(new BorderLayout());
        add(scrollPane);
    }

    /**
     * Applies the proper theming to the memory table and text within.
     */
    public void applyTheme(Font font, EditorTheme editorTheme) {
        scrollPane.setBackground(editorTheme.currentLine());
        scrollPane.getViewport().setBackground(editorTheme.currentLine());
        editorTheme.applyThemeScrollbar(scrollPane.getVerticalScrollBar());
        editorTheme.applyThemeScrollbar(scrollPane.getHorizontalScrollBar());
        EditorTheme.applyFontAndThemeBorderless(this, font, editorTheme);

        table.applyTheme(font, editorTheme);

        if (rowHeader.getCellRenderer() instanceof RowHeaderRenderer rowHeaderRenderer) {
            rowHeaderRenderer.applyTheme(font, editorTheme);
        }

        rowHeader.setBackground(editorTheme.currentLine());
        rowHeader.setForeground(editorTheme.foreground());

        table.setIntercellSpacing(new Dimension(2, 2));
        table.setRowHeight(font.getSize() + 2);

        int width = 20 + (Memory.wordSize() * 2 * font.getSize());

        rowHeader.setFixedCellWidth(width);
        rowHeader.setFixedCellHeight(table.getRowHeight());

        DefaultTableCellRenderer rightRenderer = new DefaultTableCellRenderer();
        rightRenderer.setHorizontalAlignment(JLabel.RIGHT);
        for (int i = 0; i < table.getColumnCount(); ++i) {
            table.getColumnModel().getColumn(i).setPreferredWidth(width);
            table.getColumnModel().getColumn(i).setCellRenderer(rightRenderer);
        }
    }

    /**
     * Forcibly refreshes the display of the table.
     */
    public void update() {
        SwingUtilities.invokeLater(table::updateUI);
    }
}
