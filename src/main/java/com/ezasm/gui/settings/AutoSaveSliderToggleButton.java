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

    public boolean getToggleButtonStatus() {
        return toggleButton.isSelected();
    }

    public void setToggleButtonStatus(boolean status) {
        toggleButton.setSelected(status);
    }

    public int getSliderValue() {
        return slider.getValue();
    }

    public void setSliderValue(int value) {
        slider.setValue(value);
    }
}
