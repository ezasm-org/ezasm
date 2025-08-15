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
    private JList<Object> colHeader;

    private final JScrollPane decodeScrollPane;
    private final AlternatingColorTable decodeTable;
    private JPanel tableContainer;  // new

    /**
     * Table's default initialization uses a word memory display
     */
    private MemoryFormatStrategy strategy = new WordFormatStrategy();

    /**
     * Tracks the state of visiblity for decoding table
     */
    private boolean decodeView = false;

    /**
     * The standard number of rows for a memory table.
     */
    public static final int ROWS = 32;

    /**
     * The standard number of columns for a memory table.
     */
    public static final int COLUMNS = 16;

    private int offset;

    private int fontSize;

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
        table.setModel(new MemoryTableModel(memory, ROWS, COLUMNS, strategy));
        table.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
        table.getTableHeader().setReorderingAllowed(false);

        updateRowHeaders();
        updateColHeaders();
        scrollPane.setColumnHeaderView(table.getTableHeader());
        scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
        scrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
        scrollPane.setWheelScrollingEnabled(true);

        // Set up the right (decode) table
        decodeTable = new AlternatingColorTable(EditorTheme.Light);  // you can set a different model here
        decodeScrollPane = new JScrollPane(decodeTable);
        decodeTable.setModel(new DecodingTableModel(memory, ROWS, COLUMNS));
        decodeTable.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
        decodeScrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
        decodeScrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
        decodeScrollPane.setWheelScrollingEnabled(true);
        decodeScrollPane.setVisible(false); // hidden initially

        // Create container and add both
        tableContainer = new JPanel();
        tableContainer.setLayout(new BoxLayout(tableContainer, BoxLayout.X_AXIS));
        tableContainer.add(scrollPane);
        tableContainer.add(decodeScrollPane);

        // Set layout and add
        setLayout(new BorderLayout());
        add(tableContainer, BorderLayout.CENTER);
        /*
        *setLayout(new BorderLayout());
        *add(scrollPane);
        */

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
        table.setCellSelectionEnabled(false);

        ((IThemeable) rowHeader.getCellRenderer()).applyTheme(font, editorTheme);
        rowHeader.setBackground(editorTheme.currentLine());
        rowHeader.setForeground(editorTheme.foreground());

        fontSize = font.getSize();

        table.setIntercellSpacing(new Dimension(2, 2));
        table.setRowHeight(fontSize + 2);

        int width = 20 + (strategy.getDisplaySize() * 2 * fontSize);
        //display size is either 1 when displaying bytes, or 4 when displaying words

        DefaultTableCellRenderer rightRenderer = new DefaultTableCellRenderer();
        rightRenderer.setHorizontalAlignment(JLabel.RIGHT);
        for (int i = 0; i < table.getColumnCount(); ++i) {
            table.getColumnModel().getColumn(i).setPreferredWidth(width);
            table.getColumnModel().getColumn(i).setCellRenderer(rightRenderer);
        }

        rowHeader.setFixedCellWidth(20 + (Memory.getWordSize() * 2 * fontSize));
        rowHeader.setFixedCellHeight(table.getRowHeight());

        // === Apply theme to rightScrollPane and decodeTable ===
        if (decodeScrollPane != null) {
            decodeScrollPane.setBackground(editorTheme.currentLine());
            decodeScrollPane.getViewport().setBackground(editorTheme.currentLine());
            editorTheme.applyThemeScrollbar(decodeScrollPane.getVerticalScrollBar());
            editorTheme.applyThemeScrollbar(decodeScrollPane.getHorizontalScrollBar());

            if (decodeTable != null) {
                decodeTable.applyTheme(font, editorTheme);
                decodeTable.setCellSelectionEnabled(false);
                decodeTable.setRowHeight(fontSize + 2);
                decodeTable.setIntercellSpacing(new Dimension(2, 2));
                for (int i = 0; i < decodeTable.getColumnCount(); ++i) {
                    decodeTable.getColumnModel().getColumn(i).setPreferredWidth(width);
                    decodeTable.getColumnModel().getColumn(i).setCellRenderer(rightRenderer);
                }
            }
        }
    }

    /**
     * Sets the offset of the memory viewer to a new address.
     *
     * @param offset the new offset to use.
     */
    public void setOffset(int offset) {
        this.offset = offset;
        ((MemoryTableModel) table.getModel()).setOffset(offset);
        ((DecodingTableModel) decodeTable.getModel()).setOffset(offset);
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
        SwingUtilities.invokeLater(this::updateRowHeaders);
        SwingUtilities.invokeLater(this::updateColHeaders);
        SwingUtilities.invokeLater(table::updateUI);
        SwingUtilities.invokeLater(decodeTable::updateUI);
    }

    /**
     * Updates the row headers based on any potential change in memory viewer offset.
     */
    private void updateRowHeaders() {
        Object[] rows = new Object[ROWS];
        for (int i = 0; i < ROWS; ++i) {
            rows[i] = (new RawData(offset + (long) i * strategy.getDisplaySize() * COLUMNS)).toHexString();
        }
        rowHeader = new JList<>(new SimpleListModel(rows));
        rowHeader.setCellRenderer(new RowHeaderRenderer(table));
        rowHeader.setFixedCellWidth(20 + (Memory.getWordSize() * 2 * fontSize));
        rowHeader.setFixedCellHeight(table.getRowHeight());
        scrollPane.setRowHeaderView(rowHeader);
    }
    /**
     * Updates the col headers and col width based on any potential change in viewing strategy.
     */
    private void updateColHeaders(){
        int width = 20 + (strategy.getDisplaySize() * 2 * fontSize);
        DefaultTableCellRenderer rightRenderer = new DefaultTableCellRenderer();
        rightRenderer.setHorizontalAlignment(JLabel.RIGHT);

        for (int i = 0; i < table.getColumnCount(); i++) {
            table.getColumnModel().getColumn(i).setHeaderValue( ((MemoryTableModel) table.getModel()).getColumnName(i));
            table.getColumnModel().getColumn(i).setPreferredWidth(width);
            table.getColumnModel().getColumn(i).setCellRenderer(rightRenderer);
        }

    }

    /**
     * Updates the decoding tables display mode to show memory decoded as that type
     *
     * @param mode either "Ascii", "Int", or "Float". otherwise, table will display hexcode memory like the memory table
     */
    public void switchDecodeMode(String mode){
        ((DecodingTableModel) decodeTable.getModel()).setDecodeMode(mode);
        tableContainer.revalidate();
        tableContainer.repaint();
    }

    /**
     * Toggles decoding table between visible and not visible
     */
    public void toggleDecoding(){
        decodeView = !decodeView;
        decodeScrollPane.setVisible(decodeView);
        tableContainer.revalidate();
        tableContainer.repaint();
        update();
    }
}
