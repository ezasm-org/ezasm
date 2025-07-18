package com.ezasm.gui.settings;
import com.ezasm.gui.util.EditorTheme;

import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyEvent;
public class ConfigurationPreferencesEditor implements PreferencesEditor {
    private final Config config;
    private JPanel panel;
    private JTextField fontInput;
    private JSlider speedSlider, tabSizeSlider;
    private AutoSaveSliderToggleButton autoSaveButton;
    private JComboBox<String> themeInput;

    public ConfigurationPreferencesEditor(Config config) {
        this.config=config;
        panel = new JPanel(new GridLayout(0, 2, 5, 20));
        fontInput = new JTextField(String.valueOf(config.getFontSize()));
        speedSlider = new JSlider(50, 1000, config.getSimulationDelay());
        tabSizeSlider = new JSlider(1, 8, config.getTabSize());
        autoSaveButton = new AutoSaveSliderToggleButton(config.getAutoSaveSelected(), config.getAutoSaveInterval());
        themeInput = new JComboBox<>(Config.THEMES);
        themeInput.setSelectedItem(config.getTheme().name());

        panel.add(new JLabel("Font Size")); panel.add(fontInput);
        panel.add(new JLabel("Instruction Delay")); panel.add(speedSlider);
        panel.add(new JLabel("Theme")); panel.add(themeInput);
        panel.add(new JLabel("Tab Size")); panel.add(tabSizeSlider);
        panel.add(new JLabel("Auto Save")); panel.add(autoSaveButton);
    }

    public void applyTheme(EditorTheme theme) {
        panel.setBackground(theme.background());
    }
    @Override public JComponent getUI() {
        return panel;
    }

    @Override public String getTitle() {
        return "Configuration";
    }

    @Override public int getMnemonic() {
        return KeyEvent.VK_C;
    }

    @Override public void savePreferences() {

        try {
            config.setFontSize(Integer.parseInt(fontInput.getText()));
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(null, "Invalid font size.");
        }
        config.setSimulationDelay(speedSlider.getValue());
        config.setTabSize(tabSizeSlider.getValue());
        config.setTheme(themeInput.getSelectedItem().toString());
        config.setAutoSaveInterval(autoSaveButton.getSliderValue());
        config.setAutoSaveSelected(autoSaveButton.getToggleButtonStatus());
        config.saveChanges();
    }

    @Override public void matchGuiToDefaultPreferences() {
        // Set values back to defaults
        fontInput.setText(Config.DEFAULT_FONT_SIZE);
        speedSlider.setValue(Integer.parseInt(Config.DEFAULT_SIMULATION_DELAY));
        tabSizeSlider.setValue(Integer.parseInt(Config.DEFAULT_TAB_SIZE));
        themeInput.setSelectedIndex(0);
        autoSaveButton.setToggleButtonStatus(Boolean.parseBoolean(Config.DEFAULT_AUTO_SAVE_SELECTED));
        autoSaveButton.setSliderValue(Integer.parseInt(Config.DEFAULT_AUTO_SAVE_INTERVAL));
    }
}
