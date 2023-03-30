package com.ezasm.gui.table;

import com.ezasm.gui.util.EditorTheme;
import com.ezasm.gui.util.IThemeable;
import com.ezasm.simulation.Memory;
import com.ezasm.simulation.exception.ReadOutOfBoundsException;
import com.ezasm.util.RawData;

import javax.swing.*;
import javax.swing.table.AbstractTableModel;
import javax.swing.table.DefaultTableCellRenderer;
import java.awt.*;

public class MemoryTable extends JPanel implements IThemeable {

    private final JTable table;
    private final JScrollPane scrollPane;
    private final JList<Object> rowHeader;

    private static final int ROWS = 16;
    private static final int COLUMNS = 16;

    private int offset;

    public MemoryTable(Memory memory) {
        super();
        this.table = new JTable();
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
        scrollPane.setPreferredSize(table.getPreferredSize());
        scrollPane.setWheelScrollingEnabled(true);

        setLayout(new BorderLayout());
        add(scrollPane);
    }

    /**
     * Applies the proper theming to the memory table and text within.
     */
    public void applyTheme(Font font, EditorTheme editorTheme) {
        scrollPane.setBackground(editorTheme.background());
        editorTheme.applyThemeScrollbar(scrollPane.getVerticalScrollBar());
        editorTheme.applyThemeScrollbar(scrollPane.getHorizontalScrollBar());
        EditorTheme.applyFontAndTheme(this, font, editorTheme);
        EditorTheme.applyFontAndTheme(table, font, editorTheme);
        EditorTheme.applyFontAndTheme(table.getTableHeader(), font, editorTheme);
        EditorTheme.applyFontAndTheme(rowHeader, font, editorTheme);

        table.setRowHeight(font.getSize() + 2);

        if (rowHeader.getCellRenderer() instanceof RowHeaderRenderer rowHeaderRenderer) {
            rowHeaderRenderer.applyTheme(font, editorTheme);
        }

        int width = 20 + (Memory.wordSize() * 2 * font.getSize());

        rowHeader.setFixedCellWidth(width);
        rowHeader.setFixedCellHeight(table.getRowHeight());
        rowHeader.setBorder(table.getBorder());

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

    /**
     * Helper model class to inform the TableModel of how to construct and read from itself.
     */
    private class MemoryTableModel extends AbstractTableModel {

        private final Memory memory;
        private int offset;
        private int rows;
        private int cols;

        public MemoryTableModel(Memory memory, int rows, int columns) {
            super();
            this.memory = memory;
            this.rows = rows;
            this.cols = columns;
            this.offset = memory.currentHeapPointer();
        }

        @Override
        public int getRowCount() {
            return rows;
        }

        @Override
        public int getColumnCount() {
            return cols;
        }

        @Override
        public Object getValueAt(int row, int col) {
            try {
                return memory.read(offset + (row * cols + col) * Memory.wordSize()).toHexString();
            } catch (ReadOutOfBoundsException e) {
                return RawData.emptyBytes(Memory.wordSize()).toHexString();
            }
        }

        @Override
        public void setValueAt(Object aValue, int rowIndex, int columnIndex) {
            fireTableCellUpdated(rowIndex, columnIndex);
        }

        @Override
        public boolean isCellEditable(int row, int col) {
            return false;
        }

        @Override
        public String getColumnName(int column) {
            return "+" + Long.toHexString((long) column * Memory.wordSize());
        }
    }
}
