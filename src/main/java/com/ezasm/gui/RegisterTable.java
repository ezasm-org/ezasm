package com.ezasm.gui;

import com.ezasm.gui.util.IThemeable;
import com.ezasm.gui.util.Theme;
import com.ezasm.simulation.Registers;
import com.ezasm.gui.settings.Config;
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
    private final Registers registers;
    private final JScrollPane scrollPane;
    private ArrayList<Integer> changedRegisterNums = new ArrayList<Integer>();
    private Config config;
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
     * The function to highlight changed registers      
     */
    public void ChangeCellColor(Config config) {
        Theme theme = Theme.getTheme(config.getTheme());
        TableCellRenderer render = new DefaultTableCellRenderer() {
            @Override
            public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected,
                    boolean hasFocus, int row, int col) {
                final Component c = super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, col);
                if (changedRegisterNums.size() < 5 && changedRegisterNums.contains(row)) {
                    c.setForeground(theme.red());
                } else {
                    c.setForeground(theme.foreground());
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
        ChangeCellColor(config);
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
     *
     * @param config the instance used to get theme
     */
    public void addHighlightValue(Long number, Config config) {
        if (!changedRegisterNums.contains(number.intValue())) {
            this.changedRegisterNums.add(number.intValue());
        }
        this.config = config;
    }

    public void removeHighlightValue() {
        this.changedRegisterNums.clear();
    }

    public void removeExtra() {
        if (changedRegisterNums.size() <= 1) {
            return;
        }
        for (int i = 0; i < changedRegisterNums.size(); i++) {
            if (changedRegisterNums.get(i) == 1) {
                changedRegisterNums.remove(i);
                break;
            } else {
                changedRegisterNums.remove(i);
                i--;
            }
        }
    }
}
