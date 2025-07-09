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
    private boolean isByteView;

    /**
     * Models a table based a memory.
     *
     * @param memory  the memory to base the table model off.
     * @param rows    the number of rows to include.
     * @param columns the number of columns to include.
     */
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

    /**
     * Toggles table to or from byte by byte view .
     *
     * @param byteView true if setting to byte view, false if turning off byte view
     */
    public void setByteView(boolean byteView) {
        this.isByteView = byteView;
        fireTableDataChanged(); // Notify table that data has changed
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
        try {
            if(isByteView){
                int byteAddress = offset + (row * cols + col);
                RawData byteData = memory.read(byteAddress, 1); //read a single byte
                return String.format("%02X", byteData.data()[0]); // get byte from interal byte array in RawData
            }else { //word sized display (everything other than byte by byte)
                return memory.read(offset + (row * cols + col) * Memory.getWordSize()).toHexString();
            }
        } catch (ReadOutOfBoundsException e) {
            if(isByteView){
                return "  "; //two spaces for out of bounds byte
            }else {
                return RawData.emptyBytes(Memory.getWordSize()).toHexString();
            }
        }
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
        return "+" + Long.toHexString((long) column * Memory.getWordSize());
    }
}
