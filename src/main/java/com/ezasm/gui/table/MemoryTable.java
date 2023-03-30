package com.ezasm.gui.table;

import com.ezasm.gui.util.EditorTheme;
import com.ezasm.gui.util.IThemeable;
import com.ezasm.simulation.Memory;
import com.ezasm.util.RawData;

import javax.swing.*;
import javax.swing.table.DefaultTableCellRenderer;
import java.awt.*;

/**
 * Represents a view of the memory. This view begins at the given offset which by default is the initial heap pointer.
 * Each row displays the values from memory for (offset + (row * columns + column) * word size.
 */
public class MemoryTable extends JPanel implements IThemeable {

    private final AlternatingColorTable table;
    private final JScrollPane scrollPane;
    private JList<Object> rowHeader;

    public static final int ROWS = 32;
    public static final int COLUMNS = 16;

    private int offset;

    /**
     * Constructs a memory table with a default offset at the initial heap pointer.
     *
     * @param memory the memory to view.
     */
    public MemoryTable(Memory memory) {
        super();
        this.table = new AlternatingColorTable(EditorTheme.Light);
        this.scrollPane = new JScrollPane(table);
        this.offset = memory.initialHeapPointer();
        table.setModel(new MemoryTableModel(memory, ROWS, COLUMNS));
        table.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);

        updateRowHeaders();
        scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
        scrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
        scrollPane.setWheelScrollingEnabled(true);

        setLayout(new BorderLayout());
        add(scrollPane);
    }

    /**
     * Applies the given theme and font to the component itself, the tabbed pane, and all subcomponents of the tabbed
     * pane. If the components are IThemable, uses their IThemable#applyTheme method to do so.
     *
     * @param font        the font to apply.
     * @param editorTheme the theme to apply.
     */
    public void applyTheme(Font font, EditorTheme editorTheme) {
        scrollPane.setBackground(editorTheme.currentLine());
        scrollPane.getViewport().setBackground(editorTheme.currentLine());
        editorTheme.applyThemeScrollbar(scrollPane.getVerticalScrollBar());
        editorTheme.applyThemeScrollbar(scrollPane.getHorizontalScrollBar());
        EditorTheme.applyFontThemeBorderless(this, font, editorTheme);

        table.applyTheme(font, editorTheme);

        if (rowHeader.getCellRenderer() instanceof RowHeaderRenderer rowHeaderRenderer) {
            rowHeaderRenderer.applyTheme(font, editorTheme);
        }

        rowHeader.setBackground(editorTheme.currentLine());
        rowHeader.setForeground(editorTheme.foreground());

        table.setIntercellSpacing(new Dimension(2, 2));
        table.setRowHeight(font.getSize() + 2);

        int width = 20 + (Memory.wordSize() * 2 * font.getSize());

        DefaultTableCellRenderer rightRenderer = new DefaultTableCellRenderer();
        rightRenderer.setHorizontalAlignment(JLabel.RIGHT);
        for (int i = 0; i < table.getColumnCount(); ++i) {
            table.getColumnModel().getColumn(i).setPreferredWidth(width);
            table.getColumnModel().getColumn(i).setCellRenderer(rightRenderer);
        }

        rowHeader.setFixedCellWidth(width);
        rowHeader.setFixedCellHeight(table.getRowHeight());
    }

    /**
     * Sets the offset of the memory viewer to a new address.
     *
     * @param offset the new offset to use.
     */
    public void setOffset(int offset) {
        this.offset = offset;
        ((MemoryTableModel) table.getModel()).setOffset(offset);
        update();
    }

    /**
     * Gets the offset address of the memory viewer.
     *
     * @return the current view offset address.
     */
    public int getOffset() {
        return offset;
    }

    /**
     * Forcibly refreshes the display of the table.
     */
    public void update() {
        updateRowHeaders();
        SwingUtilities.invokeLater(table::updateUI);
    }

    private void updateRowHeaders() {
        Object[] rows = new Object[ROWS];
        for (int i = 0; i < ROWS; ++i) {
            rows[i] = (new RawData(offset + (long) i * Memory.wordSize() * COLUMNS)).toHexString();
        }
        rowHeader = new JList<>(new SimpleListModel(rows));
        rowHeader.setCellRenderer(new RowHeaderRenderer(table));
        rowHeader.setFixedCellWidth(table.getColumnModel().getColumn(0).getWidth());
        rowHeader.setFixedCellHeight(table.getRowHeight());
        scrollPane.setRowHeaderView(rowHeader);
    }
}
