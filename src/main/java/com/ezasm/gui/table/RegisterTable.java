package com.ezasm.gui.table;

import com.ezasm.gui.util.EditorTheme;
import com.ezasm.gui.util.IThemeable;
import com.ezasm.simulation.Registers;

import java.awt.*;
import javax.swing.*;

/**
 * The GUI display table of the registers. Has a scroll pane embedded.
 */
public class RegisterTable extends JPanel implements IThemeable {

    private final AlternatingHighlightTable table;
    private final JScrollPane scrollPane;
    private boolean reset = false;
    private static final Dimension MIN_SIZE = new Dimension(200, 100);
    private static final Dimension MAX_SIZE = new Dimension(350, 2000);

    /**
     * Given the registers, construct a table which displays the names and values of each one.
     *
     * @param registers the registers to read from.
     */
    public RegisterTable(Registers registers) {
        super();
        this.table = new AlternatingHighlightTable(EditorTheme.Light);
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
        table.getTableHeader().setReorderingAllowed(false);

        table.applyTheme(new Font("monospaced", Font.PLAIN, 12), EditorTheme.Light);

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
    }

    /**
     * Forcibly refreshes the display of the table
     */
    public void update() {
        reset = true;
        SwingUtilities.invokeLater(table::updateUI);
    }

    /**
     * Highlights a register row whose value has changed
     *
     * @param number the index of changed register
     */
    public void addHighlightValue(int number) {
        if (reset) {
            table.clearAllFlashes();
            reset = false;
        }
        table.flashRow(number);
    }

    /**
     * Removes the current highlights in the register table.
     */
    public void removeHighlightValue() {
        table.clearAllFlashes();
    }
}
