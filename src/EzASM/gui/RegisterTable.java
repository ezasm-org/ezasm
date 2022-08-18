package EzASM.gui;

import EzASM.Registers;

import javax.swing.*;
import javax.swing.table.AbstractTableModel;
import javax.swing.table.DefaultTableColumnModel;
import javax.swing.table.JTableHeader;
import java.awt.*;

public class RegisterTable extends JPanel {

    private JTable table;
    private Registers registers;
    private static final Dimension MIN_SIZE = new Dimension(200, 400);
    private static final Dimension MAX_SIZE = new Dimension(200, 2000);

    public RegisterTable(Registers registers) {
        super();
        this.registers = registers;
        initTable();
        JScrollPane scrollPane = new JScrollPane(table);
        scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
        scrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
        scrollPane.setMinimumSize(table.getSize());
        scrollPane.setPreferredSize(table.getPreferredSize());
        setMaximumSize(MAX_SIZE);
        setLayout(new BorderLayout());
        add(scrollPane);
    }

    public void update() {
        table.updateUI();
    }

    private void initTable() {
        table = new JTable();
        AbstractTableModel model = new RegistersTableModel(registers);
        table.setModel(model);
        table.setMinimumSize(MIN_SIZE);
    }

    private class RegistersTableModel extends AbstractTableModel {

        private String[] columns = { "Register", "Value" };


        private final Registers registers;

        public RegistersTableModel(Registers registers) {
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
            if(col == 0) {
                // labels
                return Registers.getRegisterName(row);
            } else if(col == 1) {
                // values
                return registers.getRegister(row).getLong();
            } else {
                // Error
                throw new RuntimeException();
            }
        }

        @Override
        public void setValueAt(Object aValue, int rowIndex, int columnIndex) {}

        @Override
        public boolean isCellEditable(int row, int col) {
            return false;
        }

        @Override
        public String getColumnName(int column) {
            return columns[column];
        }
    }

}
