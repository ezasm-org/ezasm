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

    public MemoryTable(Memory memory) {
        super();
        this.table = new JTable();
        this.scrollPane = new JScrollPane(table);
        table.setModel(new MemoryTableModel(memory, 8, 20));
        table.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);

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
        table.setRowHeight(font.getSize() + 2);
        table.getTableHeader().setOpaque(false);

        DefaultTableCellRenderer rightRenderer = new DefaultTableCellRenderer();
        rightRenderer.setHorizontalAlignment(JLabel.RIGHT);
        for (int i = 0; i < table.getColumnCount(); ++i) {
            table.getColumnModel().getColumn(i).setPreferredWidth(20 + (14 * font.getSize()));
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
                return memory.read(offset + (col * rows + row) * memory.wordSize).toHexString();
            } catch (ReadOutOfBoundsException e) {
                return RawData.emptyBytes(memory.wordSize).toHexString();
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
            return "" + new RawData(offset + ((long) column * rows) * memory.wordSize).toHexString();
        }
    }
}
