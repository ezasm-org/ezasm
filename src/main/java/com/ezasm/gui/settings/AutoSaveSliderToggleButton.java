package com.ezasm.gui.settings;

import javax.swing.*;
import java.awt.*;

/**
 * A toggle button combined with a slider when enabled representing the auto-save delay.
 */
public class AutoSaveSliderToggleButton extends JPanel {

    private final JToggleButton toggleButton;
    private final JSlider slider;

    public static final String ON_TEXT = "ON";
    public static final String OFF_TEXT = "OFF";

    /**
     * Constructs this button slider.
     *
     * @param enabled         the initial state of the button.
     * @param intervalSeconds the initial interval for the slider.
     */
    public AutoSaveSliderToggleButton(boolean enabled, int intervalSeconds) {
        setLayout(new BorderLayout());

        slider = new JSlider(0, 30, intervalSeconds);
        slider.setMajorTickSpacing(5);
        slider.setPaintTicks(true);
        slider.setPaintLabels(true);
        slider.setVisible(enabled);
        add(slider, BorderLayout.CENTER);

        toggleButton = new JToggleButton(enabled ? ON_TEXT : OFF_TEXT);
        toggleButton.setSelected(enabled);
        toggleButton.addChangeListener(event -> {
            if (getToggleButtonStatus()) {
                toggleButton.setText(ON_TEXT);
                slider.setVisible(true);
            } else {
                toggleButton.setText(OFF_TEXT);
                slider.setVisible(false);
            }
        });
        add(toggleButton, BorderLayout.WEST);
    }

    /**
     * Gets whether the toggle button is pressed.
     *
     * @return true if the toggle button is pressed, false otherwise.
     */
    public boolean getToggleButtonStatus() {
        return toggleButton.isSelected();
    }

    /**
     * Sets whether the toggle button is pressed.
     *
     * @param status whether the toggle button displays as pressed.
     */
    public void setToggleButtonStatus(boolean status) {
        toggleButton.setSelected(status);
    }

    /**
     * Gets the numeric value from the slider.
     *
     * @return the numeric value from the slider.
     */
    public int getSliderValue() {
        return slider.getValue();
    }

    /**
     * Sets the position of the slider to the given integer.
     *
     * @param value the new position of the slider.
     */
    public void setSliderValue(int value) {
        slider.setValue(value);
    }
}
