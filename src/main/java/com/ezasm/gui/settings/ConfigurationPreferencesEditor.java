package com.ezasm.gui.settings;
import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyEvent;
public class ConfigurationPreferencesEditor implements PreferencesEditor {

    private JPanel panel;
    private JTextField fontInput;
    private JSlider speedSlider, tabSizeSlider;
    private AutoSaveSliderToggleButton autoSaveButton;
    private JComboBox<String> themeInput;

    public ConfigurationPreferencesEditor(Config config) {
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
        // Save logic here using your config instance
    }

    @Override public void matchGuiToDefaultPreferences() {
        // Set values back to defaults
    }
}
