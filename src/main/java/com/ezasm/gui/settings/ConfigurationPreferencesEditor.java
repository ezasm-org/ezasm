package com.ezasm.gui.settings;

import com.ezasm.gui.ui.EzComboBoxUI;
import com.ezasm.gui.util.EditorTheme;

import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyEvent;
import java.util.Arrays;
import java.util.Objects;
import java.util.Vector;

import com.ezasm.gui.util.IThemeable;

/**
 * Provides a GUI panel for editing user-configurable preferences such as font size, simulation delay, tab size, theme
 * selection, auto-save and memory randomize-on-reset behavior. This class implements both {@link PreferencesEditor} and
 * {@link IThemeable} to support configuration management and dynamic theme application.
 */
public class ConfigurationPreferencesEditor implements PreferencesEditor, IThemeable {
    private final Config config;
    private JPanel panel;
    private JTextField fontInput;
    private JSlider speedSlider, tabSizeSlider;
    private AutoSaveSliderToggleButton autoSaveButton;
    private MemoryRandomizeOnResetButton memoryRandomizeOnResetButton;
    private JComboBox<String> themeInput;

    /**
     * Constructs a configuration editor panel using the given config instance. Populates the panel with inputs
     * initialized to current configuration values.
     *
     * @param config the configuration object to edit and update
     */
    public ConfigurationPreferencesEditor(Config config) {
        this.config = config;
        panel = new JPanel(new GridLayout(0, 2, 5, 20));
        fontInput = new JTextField(String.valueOf(config.getFontSize()));
        speedSlider = new JSlider(50, 1000, config.getSimulationDelay());
        tabSizeSlider = new JSlider(1, 8, config.getTabSize());
        autoSaveButton = new AutoSaveSliderToggleButton(config.getAutoSaveSelected(), config.getAutoSaveInterval());
        memoryRandomizeOnResetButton = new MemoryRandomizeOnResetButton(config.getMemoryRandomizeOnReset());
        themeInput = new JComboBox<>(new Vector<>(Arrays.asList(EditorTheme.getThemeNames())));
        themeInput.setSelectedItem(config.getTheme().name());

        panel.add(new JLabel("Font Size"));
        panel.add(fontInput);
        panel.add(new JLabel("Instruction Delay"));
        panel.add(speedSlider);
        panel.add(new JLabel("Theme"));
        panel.add(themeInput);
        panel.add(new JLabel("Tab Size"));
        panel.add(tabSizeSlider);
        panel.add(new JLabel("Auto Save"));
        panel.add(autoSaveButton);
        panel.add(new JLabel("Randomize Memory"));
        panel.add(memoryRandomizeOnResetButton);
    }

    /**
     * Returns the root UI component for this editor so it can be embedded in other dialogs.
     *
     * @return the editor panel component
     */
    @Override
    public JComponent getUI() {
        return panel;
    }

    /**
     * Returns the name of this configuration section.
     *
     * @return the section title ("Configuration")
     */
    @Override
    public String getTitle() {
        return "Configuration";
    }

    /**
     * Returns the keyboard mnemonic associated with this preferences tab.
     *
     * @return the key code for 'C' (used as mnemonic)
     */
    @Override
    public int getMnemonic() {
        return KeyEvent.VK_C;
    }

    /**
     * Applies user inputs to the configuration object and persists changes. Invalid font sizes will trigger a warning
     * dialog but not crash the app.
     */
    @Override
    public void savePreferences() {

        try {
            config.setFontSize(Integer.parseInt(fontInput.getText()));
            fontInput.setText(String.valueOf(config.getFontSize()));
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(null, "Invalid font size.");
        }
        config.setSimulationDelay(speedSlider.getValue());
        config.setTabSize(tabSizeSlider.getValue());
        config.setTheme(Objects.requireNonNull(themeInput.getSelectedItem()).toString());
        config.setAutoSaveInterval(autoSaveButton.getSliderValue());
        config.setAutoSaveSelected(autoSaveButton.getToggleButtonStatus());
        // does memory randomization setting have to be inverted from its button?
        config.setMemoryRandomizeOnReset(memoryRandomizeOnResetButton.isSelected());
        config.saveChanges();
    }

    /**
     * Resets all controls in the GUI to reflect default configuration values.
     */
    @Override
    public void matchGuiToDefaultPreferences() {
        // Set values back to defaults
        fontInput.setText(Config.DEFAULT_FONT_SIZE);
        speedSlider.setValue(Integer.parseInt(Config.DEFAULT_SIMULATION_DELAY));
        tabSizeSlider.setValue(Integer.parseInt(Config.DEFAULT_TAB_SIZE));
        themeInput.setSelectedIndex(0);
        autoSaveButton.setToggleButtonStatus(Boolean.parseBoolean(Config.DEFAULT_AUTO_SAVE_SELECTED));
        autoSaveButton.setSliderValue(Integer.parseInt(Config.DEFAULT_AUTO_SAVE_INTERVAL));
        memoryRandomizeOnResetButton.setSelected(Boolean.parseBoolean(Config.DEFAULT_MEMORY_RANDOMIZE_ON_RESET));
    }

    /**
     * Applies a visual theme and font to the entire editor panel, including labels, sliders, combo boxes, and custom
     * components like the auto-save control.
     *
     * @param font  the font to apply
     * @param theme the visual theme to apply
     */
    @Override
    public void applyTheme(Font font, EditorTheme theme) {
        panel.setBackground(theme.background());
        panel.setForeground(theme.foreground());
        panel.setFont(font);

        applyTo(fontInput, theme, font);
        applyTo(speedSlider, theme, font);
        applyTo(tabSizeSlider, theme, font);
        applyTo(themeInput, theme, font);
        autoSaveButton.applyTheme(font, theme);
        applyTo(memoryRandomizeOnResetButton, theme, font);

        for (Component c : panel.getComponents()) {
            if (c instanceof JLabel label) {
                label.setForeground(theme.foreground());
                label.setBackground(theme.background());
                label.setFont(font);
            } else if (c instanceof JButton button) {
                button.setFont(font);
                button.setForeground(theme.foreground());
                button.setBackground(theme.background());
                button.setBorder(BorderFactory.createLineBorder(theme.foreground()));
            }
        }

    }

    /**
     * Applies theme and font styling to a generic component. Special handling is done for combo boxes and buttons.
     *
     * @param comp  the component to apply theme and font to
     * @param theme the theme to apply
     * @param font  the font to apply
     */
    private void applyTo(JComponent comp, EditorTheme theme, Font font) {
        comp.setFont(font);
        comp.setBackground(theme.background());
        comp.setForeground(theme.foreground());

        if (comp instanceof AbstractButton btn) {
            theme.applyThemeButton(btn, font);
        }

        if (comp instanceof JComboBox<?> box) {
            box.setUI(new EzComboBoxUI(theme)); // <-- your custom UI!
        }
    }

}
