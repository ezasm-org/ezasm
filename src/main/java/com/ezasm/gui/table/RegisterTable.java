package com.ezasm.gui.table;

import com.ezasm.gui.util.IThemeable;
import com.ezasm.gui.util.EditorTheme;
import com.ezasm.simulation.Registers;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import javax.swing.JTable;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.TableCellRenderer;

/**
 * The GUI display table of the registers. Has a scroll pane embedded.
 */
public class RegisterTable extends JPanel implements IThemeable {

    private final AlternatingColorTable table;
    private final JScrollPane scrollPane;
    private final ArrayList<Integer> changedRegisterNumbers;
    private boolean reset = false;
    private Color cellForeground;
    private Color DefaultCellForeground;
    private static final Dimension MIN_SIZE = new Dimension(200, 100);
    private static final Dimension MAX_SIZE = new Dimension(350, 2000);

    /**
     * Given the registers, construct a table which displays the names and values of each one.
     *
     * @param registers the registers to read from.
     */
    public RegisterTable(Registers registers) {
        super();
        this.changedRegisterNumbers = new ArrayList<>();
        this.table = new AlternatingColorTable(EditorTheme.Light);
        this.scrollPane = new JScrollPane(table);
        table.setModel(new RegisterTableModel(registers));
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
    public void changeCellColor() {
        TableCellRenderer render = new DefaultTableCellRenderer() {
            @Override
            public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected,
                    boolean hasFocus, int row, int col) {
                final Component c = super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, col);
                if (changedRegisterNumbers.contains(row)) {
                    c.setForeground(cellForeground);
                } else {
                    c.setForeground(DefaultCellForeground);
                }
                return c;
            }
        };
        table.getColumnModel().getColumn(0).setCellRenderer(render);
    }

    /**
     * Applies the given theme and font to the component itself, the tabbed pane, and all subcomponents of the tabbed
     * pane. If the components are IThemable, uses their IThemable#applyTheme method to do so.
     *
     * @param font        the font to apply.
     * @param editorTheme the theme to apply.
     */
    public void applyTheme(Font font, EditorTheme editorTheme) {
        scrollPane.getViewport().setBackground(editorTheme.currentLine());
        editorTheme.applyThemeScrollbar(scrollPane.getVerticalScrollBar());
        EditorTheme.applyFontThemeBorderless(this, font, editorTheme);

        table.applyTheme(font, editorTheme);
        table.setRowHeight(font.getSize() + 3);

        cellForeground = editorTheme.red();
        DefaultCellForeground = editorTheme.foreground();
    }

    /**
     * Forcibly refreshes the display of the table
     */
    public void update() {
        changeCellColor();
        reset = true;
        SwingUtilities.invokeLater(table::updateUI);
    }

    /**
     * Tell the table which register changed, and reset the array when new value comes
     *
     * @param number the index of changed register
     */
    public void addHighlightValue(int number) {
        if (reset) {
            changedRegisterNumbers.clear();
            reset = false;
        }
        this.changedRegisterNumbers.add(number);
    }

    public void removeHighlightValue() {
        this.changedRegisterNumbers.clear();
    }
}
