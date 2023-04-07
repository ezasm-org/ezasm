package com.ezasm.gui.settings;

import javax.swing.*;
import java.awt.*;

public class AutoSaveSliderToggleButton extends JPanel {

    private final JToggleButton toggleButton;
    private final JSlider slider;

    public static final String ON_TEXT = "ON";
    public static final String OFF_TEXT = "OFF";

    public AutoSaveSliderToggleButton(String label, Config config) {
        setLayout(new BorderLayout());

        slider = new JSlider(0, 30, config.getAutoSaveInterval());
        slider.setMajorTickSpacing(5);
        slider.setPaintTicks(true);
        slider.setPaintLabels(true);
        slider.setVisible(config.getAutoSaveSelected());
        add(slider, BorderLayout.CENTER);

        toggleButton = new JToggleButton(label);
        toggleButton.setSelected(config.getAutoSaveSelected());
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
