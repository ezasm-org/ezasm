package com.ezasm.gui.table;

import com.ezasm.gui.Window;
import com.ezasm.gui.ui.EzComboBoxUI;
import com.ezasm.gui.util.EditorTheme;
import com.ezasm.gui.util.spinner.HexFormatterFactory;
import com.ezasm.gui.util.IThemeable;
import com.ezasm.gui.util.spinner.SpinnerIntegerModel;
import com.ezasm.simulation.Memory;

import javax.swing.*;
import javax.swing.border.Border;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Vector;

/**
 * Acts as a control panel for the memory view provided by a MemoryTable. Wraps the given MemoryTable.
 */
public class MemoryViewerPanel extends JPanel implements IThemeable {

    private final Memory memory;

    private final MemoryTable memoryTable;
    private final JPanel controls;
    private final Map<String, Integer> nameToAddress;

    private final int numTableWords = MemoryTable.COLUMNS * MemoryTable.ROWS * Memory.getWordSize();

    private JLabel seekInputLabel;
    private JSpinner seekSpinner;
    private JComboBox<String> seekComboBox;
    private JButton seekButton;
    private JButton forwardButton;
    private JButton backButton;

    private static final MemoryViewerActionListener actionListener = new MemoryViewerActionListener();

    private static final String SEEK = "  Go  ";
    private static final String FORWARD = " ---> ";
    private static final String BACK = " <--- ";

    /**
     * Constructs a memory viewer panel bested on a given memory.
     *
     * @param memory the memory to be displayed by this element.
     */
    public MemoryViewerPanel(Memory memory) {
        super();
        this.memory = memory;
        this.memoryTable = new MemoryTable(memory);
        this.controls = new JPanel();
        this.nameToAddress = new LinkedHashMap<>() {
            {
                put("Initial Stack", memory.initialStackPointer() - numTableWords);
                put("Initial Heap", memory.initialHeapPointer());
                put("Text Section", memory.initialTextPointer());
                put("Byte Converter", memory.initialTextPointer()); //TODO: adujst this to accurate memory pointer
            }
        };

        initializeControls();
        setLayout(new BorderLayout());
        add(controls, BorderLayout.NORTH);
        add(memoryTable, BorderLayout.CENTER);
    }

    /**
     * Initializes the controls for the viewer.
     */
    private void initializeControls() {
        seekInputLabel = new JLabel("Memory position: ");

        SpinnerIntegerModel longModel = new SpinnerIntegerModel(memoryTable.getOffset(), 0,
                memory.initialStackPointer() - numTableWords, Memory.getWordSize());
        seekSpinner = new JSpinner(longModel);
        JSpinner.DefaultEditor editor = (JSpinner.DefaultEditor) seekSpinner.getEditor();
        editor.getTextField().setFormatterFactory(new HexFormatterFactory());

        seekComboBox = new JComboBox<>(new Vector<>(nameToAddress.keySet()));
        seekComboBox.addActionListener((actionEvent) -> {
            if (actionEvent.getActionCommand().equals("comboBoxChanged")) {
                seekSpinner.setValue(
                        nameToAddress.get((String) ((JComboBox<?>) actionEvent.getSource()).getSelectedItem()));
            }
        });

        addButton(SEEK);
        addButton(FORWARD);
        addButton(BACK);

        controls.add(seekInputLabel);
        controls.add(seekSpinner);
        controls.add(seekComboBox);
        controls.add(seekButton);
        controls.add(backButton);
        controls.add(forwardButton);
        controls.setOpaque(true);
    }

    // Helper method to automate the tasks completed for all buttons on the toolbar (disable, add action listener, etc.)
    private void addButton(String text) {
        JButton button = new JButton(text);
        button.addActionListener(actionListener);
        button.setContentAreaFilled(false);
        button.setOpaque(true);

        switch (text) {
        case SEEK -> seekButton = button;
        case FORWARD -> forwardButton = button;
        case BACK -> backButton = button;
        }
    }

    /**
     * Applies the given theme and font to the component itself, the tabbed pane, and all subcomponents of the tabbed
     * pane. If the components are IThemable, uses their IThemable#applyTheme method to do so.
     *
     * @param font        the font to apply.
     * @param editorTheme the theme to apply.
     */
    @Override
    public void applyTheme(Font font, EditorTheme editorTheme) {
        memoryTable.applyTheme(font, editorTheme);

        Color buttonBackground = editorTheme.modifyAwayFromBackground(editorTheme.background());
        Color borderColor = editorTheme.modifyAwayFromBackground(buttonBackground);
        Border border = BorderFactory.createMatteBorder(1, 1, 1, 1, borderColor);
        EditorTheme.applyFontTheme(controls, font, editorTheme);
        EditorTheme.applyFontTheme(seekInputLabel, font, editorTheme);
        EditorTheme.applyFontThemeBorder(seekSpinner, font, editorTheme, border);
        EditorTheme.applyFontThemeBorder(seekComboBox, font, editorTheme, border);

        editorTheme.applyThemeButton(seekButton, font);
        editorTheme.applyThemeButton(forwardButton, font);
        editorTheme.applyThemeButton(backButton, font);

        seekComboBox.setUI(new EzComboBoxUI(editorTheme));

        ((JSpinner.NumberEditor) seekSpinner.getEditor()).getTextField().setCaretColor(editorTheme.foreground());
        seekSpinner.setPreferredSize(
                new Dimension(8 + (2 * Memory.getWordSize() * font.getSize()), seekSpinner.getPreferredSize().height));
    }

    /**
     * Forcibly refreshes the display of the table and UI elements.
     */
    public void update() {
        memoryTable.update();
    }

    /**
     * Moves the view to begin at the address within the spinner.
     */
    private void seek() {
        if (isViewable((int) seekSpinner.getValue())) {
            memoryTable.setOffset((int) seekSpinner.getValue());
            update();
        }
    }

    /**
     * Moves the view such that the address directly after the currently last address becomes the first address.
     */
    private void forward() {
        int newOffset = memoryTable.getOffset() + numTableWords;
        if (isViewable(newOffset)) {
            memoryTable.setOffset(newOffset);
            seekSpinner.setValue(newOffset);
            update();
        }
    }

    /**
     * Moves the view such that the address directly before the currently last address becomes the last address.
     */
    private void back() {
        int newOffset = memoryTable.getOffset() - numTableWords;
        if (isViewable(newOffset)) {
            memoryTable.setOffset(newOffset);
            seekSpinner.setValue(newOffset);
            update();
        }
    }

    /**
     * Determines if the address is within the viewable range.
     *
     * @param address the address to check.
     * @return true if the address is within the viewable range, false otherwise.
     */
    private boolean isViewable(int address) {
        return 0 <= address && address <= memory.initialStackPointer() - numTableWords;
    }

    /**
     * Helper action listener class to handle options in the toolbar.
     */
    private static class MemoryViewerActionListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            switch (e.getActionCommand()) {
            case SEEK -> Window.getInstance().getMemoryControlPanel().seek();
            case FORWARD -> Window.getInstance().getMemoryControlPanel().forward();
            case BACK -> Window.getInstance().getMemoryControlPanel().back();
            }
        }
    }
}
