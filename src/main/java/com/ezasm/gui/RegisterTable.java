package com.ezasm.gui;

import com.ezasm.gui.util.IThemeable;
import com.ezasm.gui.util.Theme;
import com.ezasm.simulation.Registers;

import javax.swing.*;
import javax.swing.table.AbstractTableModel;
import java.awt.*;
import java.util.ArrayList;
import javax.swing.JTable;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.TableCellRenderer;

/**
 * The GUI display table of the registers. Has a scroll pane embedded.
 */
public class RegisterTable extends JPanel implements IThemeable {

    private final JTable table;
    private final JScrollPane scrollPane;
    private final ArrayList<Integer> changedRegisterNumbers;
    private boolean reset = false;
    private Color CellForeground;
    private Color DefaultCellForeground;
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
        this.changedRegisterNumbers = new ArrayList<>();
        this.table = new JTable();
        this.scrollPane = new JScrollPane(table);
        table.setModel(new RegistersTableModel(registers));
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
     * The function to highlight changed registers
     */
    public void ChangeCellColor() {
        TableCellRenderer render = new DefaultTableCellRenderer() {
            @Override
            public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected,
                    boolean hasFocus, int row, int col) {
                final Component c = super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, col);
                if (changedRegisterNumbers.contains(row)) {
                    c.setForeground(CellForeground);
                } else {
                    c.setForeground(DefaultCellForeground);
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
        CellForeground = theme.red();
        DefaultCellForeground = theme.foreground();
    }

    /**
     * Forcibly refreshes the display of the table
     */
    public void update() {
        ChangeCellColor();
        reset = true;
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
     * Tell the table which register changed, and reset the array when new value comes
     *
     * @param number the index of changed register
     */
    public void addHighlightValue(int number) {
        if (reset == true) {
            changedRegisterNumbers.clear();
            reset = false;
        }
        this.changedRegisterNumbers.add(number);
    }

    public void removeHighlightValue() {
        this.changedRegisterNumbers.clear();
    }
}
