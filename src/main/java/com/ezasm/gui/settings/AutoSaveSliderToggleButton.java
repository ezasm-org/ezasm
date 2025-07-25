package com.ezasm.gui.settings;

import com.ezasm.gui.util.EditorTheme;

import javax.swing.*;
import java.awt.*;

/**
 * A toggle button combined with a slider when enabled representing the auto-save delay.
 */
public class AutoSaveSliderToggleButton extends JPanel {

    private final JToggleButton toggleButton;
    private final JSlider slider;

    public static final String ON_TEXT = "  ON  ";
    public static final String OFF_TEXT = "  OFF  ";

    /**
     * Constructs this button slider.
     *
     * @param enabled         the initial state of the button.
     * @param intervalSeconds the initial interval for the slider.
     */
    public AutoSaveSliderToggleButton(boolean enabled, int intervalSeconds) {
        setLayout(new BoxLayout(this, BoxLayout.X_AXIS));

        slider = new JSlider(0, 30, intervalSeconds);
        slider.setMajorTickSpacing(5);
        slider.setPaintTicks(true);
        slider.setPaintLabels(true);

        toggleButton = new JToggleButton(enabled ? ON_TEXT : OFF_TEXT);
        toggleButton.setContentAreaFilled(false);
        toggleButton.setOpaque(true);
        toggleButton.setSelected(enabled);

        toggleButton.addChangeListener(event -> {
            boolean selected = toggleButton.isSelected();
            toggleButton.setText(selected ? ON_TEXT : OFF_TEXT);
            updateSliderVisibility(selected);
        });

        add(toggleButton);
        add(slider);
        updateSliderVisibility(enabled);
    }

    /**
     * Applies the given theme and font to the component itself, the tabbed pane, and all subcomponents of the tabbed
     * pane. If the components are IThemable, uses their IThemable#applyTheme method to do so.
     *
     * @param font        the font to apply.
     * @param editorTheme the theme to apply.
     */
    public void applyTheme(Font font, EditorTheme editorTheme) {
        setBackground(editorTheme.background());
        setForeground(editorTheme.foreground());

        editorTheme.applyThemeButton(toggleButton, font);
        EditorTheme.applyFontTheme(slider, font, editorTheme);
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
        updateSliderVisibility(status);
    }
    /**
     * Updates the visibility of the slider component based on the toggle state.
     * Also updates the toggle button's label and refreshes the layout.
     *
     * @param showSlider {@code true} to make the slider visible, {@code false} to hide it
     */
    private void updateSliderVisibility(boolean showSlider) {
        toggleButton.setText(showSlider ? ON_TEXT : OFF_TEXT);

        slider.setVisible(showSlider);

        revalidate();
        repaint();
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
