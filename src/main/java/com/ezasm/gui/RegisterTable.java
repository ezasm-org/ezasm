package com.ezasm.gui;

import com.ezasm.simulation.Registers;

import javax.swing.*;
import javax.swing.table.AbstractTableModel;
import java.awt.*;
import javax.swing.JTable;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.CellRendererPane;

/**
 * The GUI display table of the registers. Has a scroll pane embedded.
 */
public class RegisterTable extends JPanel {

    private final JTable table;
    private final Registers registers;
    private static final Dimension MIN_SIZE = new Dimension(150, 2000);
    private static final Dimension MAX_SIZE = new Dimension(200, 2000);
    private String changedRegister = "";
    private int changedRegisterNum = 0;
    private CellRendererPane rendererPane = new CellRendererPane();
    /**
     * Given the registers, construct a table which displays the names and values of each one.
     * @param registers the registers to read from.
     */
    public RegisterTable(Registers registers) {
        super();
        this.registers = registers;
        table = new JTable();
        AbstractTableModel model = new RegistersTableModel(registers);
        table.setModel(model);
        
        JScrollPane scrollPane = new JScrollPane(table);

        scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
        scrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
        scrollPane.setPreferredSize(table.getPreferredSize());
        setPreferredSize(new Dimension(MAX_SIZE.width, getHeight()));
        setMaximumSize(MAX_SIZE);
        setLayout(new BorderLayout());
        add(scrollPane);
    }

    /**
     * The function to highlight changed registers
     */
    public void ChangeCellColor(JTable table, int col_index, int row_index){
        table.getColumnModel().getColumn(col_index).setCellRenderer(new DefaultTableCellRenderer(){
            @Override
            public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int col){
                final Component c = super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, col);
                if(row == row_index){
                    c.setForeground(Color.red);
                }
                else{
                    c.setForeground(Color.black);
                }
                rendererPane.add(c);
                return c;
            }
        });
    }
    
    

    /**
     * Forcibly refreshes the display of the table
     */
    public void update() {
        ChangeCellColor(table, 0, this.changedRegisterNum-1);
        table.updateUI();
    }

    /**
     * Helper model class to inform the TableModel of how to construct and read from itself.
     */
    private class RegistersTableModel extends AbstractTableModel {

        private static final String[] columns = { "Register", "Value" };

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
                return "$" + Registers.getRegisterName(row);
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
    /**
     *Tell the table what the changed resgiter when execute the line
     * @param var the string of the changed variable
     */
    public void addHighlightValue(String var,int number){
          this.changedRegister=var;
          this.changedRegisterNum=number;
          //Don't need to care about Register Zero
          this.changedRegisterNum+=1;
    }
    
}
