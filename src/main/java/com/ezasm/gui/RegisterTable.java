package com.ezasm.gui;

import com.ezasm.simulation.Registers;

import javax.swing.*;
import javax.swing.table.AbstractTableModel;
import java.awt.*;
import javax.swing.JTable;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.TableCellRenderer;

/**
 * The GUI display table of the registers. Has a scroll pane embedded.
 */
public class RegisterTable extends JPanel implements IThemeable {

    private final JTable table;
    private final Registers registers;
    private final JScrollPane scrollPane;
    private int changedRegisterNum = 0;
    private static final String[] columns = { "Register", "Value" };
    private static final Dimension MIN_SIZE = new Dimension(200, 2000);
    private static final Dimension MAX_SIZE = new Dimension(350, 2000);

    /**
     * Given the registers, construct a table which displays the names and values of each one.
     *
     * @param registers the registers to read from.
     */
    public RegisterTable(Registers registers) {
        super();
        this.registers = registers;
        table = new JTable();
        table.setModel(new RegistersTableModel(registers));
        scrollPane = new JScrollPane(table);
        scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
        scrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
        scrollPane.setPreferredSize(table.getPreferredSize());

        setPreferredSize(new Dimension(MAX_SIZE.width, getHeight()));
        setMinimumSize(MIN_SIZE);
        setMaximumSize(MAX_SIZE);
        setLayout(new BorderLayout());
        add(scrollPane);
    }

    /**
     *      * The function to highlight changed registers      
     */
    public void ChangeCellColor(int row_index) {
        TableCellRenderer render = new DefaultTableCellRenderer() {
            @Override
            public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected,
                    boolean hasFocus, int row, int col) {
                final Component c = super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, col);
                if (row == row_index || (row == 1 && row_index != -1)) {
                    c.setForeground(Color.red);
                } else {
                    c.setForeground(Color.black);
                }
                return c;
            }
        };
        table.getColumnModel().getColumn(0).setCellRenderer(render);
    }

    /**
     * Applies the proper theming to the editor area
     */
    public void applyTheme(Font font, Theme theme) {
        scrollPane.setBackground(theme.background());
        theme.applyThemeScrollbar(scrollPane.getVerticalScrollBar());
        Theme.applyFontAndTheme(this, font, theme);
        Theme.applyFontAndTheme(table, font, theme);
        Theme.applyFontAndTheme(table.getTableHeader(), font, theme);
        table.setRowHeight(font.getSize() + 3);
        table.getTableHeader().setOpaque(false);
    }

    /**
     * Forcibly refreshes the display of the table
     */
    public void update() {
        ChangeCellColor(changedRegisterNum - 1);
        SwingUtilities.invokeLater(table::updateUI);
    }

    /**
     * Helper model class to inform the TableModel of how to construct and read from itself.
     */
    private class RegistersTableModel extends AbstractTableModel {

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

    /**
     * Tell the table what the changed resgiter when execute the line
     *
     * @param number the index of changed register
     */
    public void addHighlightValue(int number) {
        this.changedRegisterNum = number;
        // Don't need to care about Register Zero
        this.changedRegisterNum += 1;
    }

}
