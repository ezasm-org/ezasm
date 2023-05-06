package com.ezasm.gui.table;

import com.ezasm.simulation.Registers;

import javax.swing.table.AbstractTableModel;

/**
 * Helper model class to inform the RegisterTable's TableModel of how to construct and read from itself.
 */
class RegisterTableModel extends AbstractTableModel {

    private static final String[] columns = { "Register", "Value" };

    private final Registers registers;

    /**
     * Models a table based on registers.
     *
     * @param registers the registers to base the table model off.
     */
    public RegisterTableModel(Registers registers) {
        super();
        this.registers = registers;
    }

    /**
     * Gets the number of registers.
     *
     * @return the number of registers.
     */
    public int getRowCount() {
        return registers.getRegisters().length;
    }

    /**
     * Gets the number of columns for this model (2).
     *
     * @return the number of columns for this model (2).
     */
    public int getColumnCount() {
        return columns.length;
    }

    /**
     * Gets the string value at a certain row/column.
     *
     * @param row the row of the desired cell.
     * @param col the column of the desired cell.
     * @return the value contained in that cell: register name if column 0, register value if column 1.
     */
    public Object getValueAt(int row, int col) {
        if (col == 0) {
            // labels
            return "$" + Registers.getRegisterName(row);
        } else if (col == 1) {
            // values
            return registers.getRegister(row).getLong();
        } else {
            // Error
            throw new RuntimeException();
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
        return columns[column];
    }
}
