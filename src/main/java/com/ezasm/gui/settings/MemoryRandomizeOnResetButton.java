package com.ezasm.gui.settings;

import javax.swing.*;

/**
 * A toggle button to represent whether memory is randomized or zeroed out on resets.
 */
public class MemoryRandomizeOnResetButton extends JToggleButton {

    public static final String ON_TEXT = "  ON  ";
    public static final String OFF_TEXT = "  OFF  ";

    /**
     * Constructs this toggle button.
     *
     * @param status the initial state of the button.
     */
    public MemoryRandomizeOnResetButton(boolean status) {
        setLayout(new BoxLayout(this, BoxLayout.X_AXIS));

        this.setContentAreaFilled(false);
        this.setOpaque(true);
        this.setSelected(status);
        updateText(status);

        this.addChangeListener(event -> {
            boolean selected = this.isSelected();
            updateText(selected);
        });
    }

    /**
     * Updates the toggle button's label.
     *
     * @param status the toggle button's new status
     */
    public void updateText(boolean status) {
        this.setText(status ? ON_TEXT : OFF_TEXT);
    }
}
