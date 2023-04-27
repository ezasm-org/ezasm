package com.ezasm.gui.table;

import com.ezasm.simulation.Memory;
import com.ezasm.simulation.exception.ReadOutOfBoundsException;
import com.ezasm.util.RawData;

import javax.swing.table.AbstractTableModel;

/**
 * Helper model class to inform the MemoryTable's TableModel of how to construct and read from itself.
 */
class MemoryTableModel extends AbstractTableModel {

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

    /**
     * Assigns a new offset address of memory for the table to view.
     *
     * @param offset the new offset address.
     */
    public void setOffset(int offset) {
        this.offset = offset;
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
            return memory.read(offset + (row * cols + col) * Memory.getWordSize()).toHexString();
        } catch (ReadOutOfBoundsException e) {
            return RawData.emptyBytes(Memory.getWordSize()).toHexString();
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
        return "+" + Long.toHexString((long) column * Memory.getWordSize());
    }
}
