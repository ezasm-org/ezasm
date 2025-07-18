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
    private MemoryFormatStrategy strategy;

    /**
     * Models a table based a memory.
     *
     * @param memory  the memory to base the table model off.
     * @param rows    the number of rows to include.
     * @param columns the number of columns to include.
     * @param strat   the viewing strategy being used.
     */
    public MemoryTableModel(Memory memory, int rows, int columns, MemoryFormatStrategy strat) {
        super();
        this.memory = memory;
        this.rows = rows;
        this.cols = columns;
        this.offset = memory.currentHeapPointer();
        this.strategy = strat;
    }

    //TODO: add specs for new implementations.
    public void setStrategy(MemoryFormatStrategy strat){
        this.strategy = strat;
    }

    /**
     * Assigns a new offset address of memory for the table to view.
     *
     * @param offset the new offset address.
     */
    public void setOffset(int offset) {
        this.offset = offset;
    }

    /**
     * Gets the number of rows in the model.
     *
     * @return the number of rows in the model.
     */
    @Override
    public int getRowCount() {
        return rows;
    }

    /**
     * Gets the number of columns in the model.
     *
     * @return the number of columns in the model.
     */
    @Override
    public int getColumnCount() {
        return cols;
    }

    /**
     * Gets the memory display string for a specified cell.
     *
     * @param row the row of the cell in question.
     * @param col the column of the cell in question.
     * @return a hex string of the data contained in the corresponding memory.
     */
    @Override
    public Object getValueAt(int row, int col) {
        return strategy.getValueAt(memory, row, cols, col, offset);
        /*try {
            return memory.read(offset + (row * cols + col) * Memory.getWordSize()).toHexString();
        } catch (ReadOutOfBoundsException e) {
            return RawData.emptyBytes(Memory.getWordSize()).toHexString();
        }*/
    }

    /**
     * Manually update the value at the given cell.
     *
     * @param aValue      ignored.
     * @param rowIndex    the row of the cell to update.
     * @param columnIndex the column of the cell to update.
     */
    @Override
    public void setValueAt(Object aValue, int rowIndex, int columnIndex) {
        fireTableCellUpdated(rowIndex, columnIndex);
    }

    /**
     * Forces cells to not be editable.
     *
     * @param row ignored.
     * @param col ignored.
     * @return false.
     */
    @Override
    public boolean isCellEditable(int row, int col) {
        return false;
    }

    /**
     * Gets the name of the column to be used in a header.
     *
     * @param column the column number to find the name of.
     * @return the name of the given column.
     */
    @Override
    public String getColumnName(int column) {
        return strategy.getColumnName(column);
    }
}
