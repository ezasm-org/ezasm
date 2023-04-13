package com.ezasm.gui.table;

import com.ezasm.simulation.Registers;

import javax.swing.table.AbstractTableModel;

/**
 * Helper model class to inform the RegisterTable's TableModel of how to construct and read from itself.
 */
class RegisterTableModel extends AbstractTableModel {

    private static final String[] columns = { "Register", "Value" };

    private final Registers registers;

    public RegisterTableModel(Registers registers) {
        super();
        this.registers = registers;
    }

    public int getRowCount() {
        return registers.getRegisters().length;
    }

    public int getColumnCount() {
        return columns.length;
    }

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
        return columns[column];
    }
}
