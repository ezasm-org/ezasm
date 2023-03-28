package com.ezasm.gui.settings;

import com.ezasm.gui.Window;
import com.ezasm.gui.util.IThemeable;
import com.ezasm.gui.util.EditorTheme;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.*;
import javax.swing.border.Border;
import javax.swing.event.ChangeListener;
import javax.swing.event.ChangeEvent;

public class SettingsPopup implements IThemeable {
    private static SettingsPopup instance;

    private static final String FONTSIZE = "Font Size";
    private static final String SIMULATION_SPEED = "Instruction Delay";
    private static final String THEME = "Theme";
    private static final String TABSIZE = "Tab Size";
    private static final String AUTOSAVE = "Auto Save";
    public static final String SAVE = "Save Changes";
    public static final String RESET = "Reset to Defaults";

    private JFrame popup;
    private JSlider speedSlider;
    private JSlider tabSizeSlider;
    private SliderToggleButton AutoSaveButton;
    private JTextField fontInput;
    private JComboBox themeInput;
    private JPanel grid;
    private JButton resetDefaults;
    private JButton save;
    private JLabel speedLabel, fontSizeLabel, themeLabel, tabSizeLabel, autoSaveLabel;
    private BorderLayout layout;

    private Config config;

    private ButtonActionListener buttonActionListener;

    protected SettingsPopup() {
        instance = this;
        config = new Config();
        initialize();
    }

    public static SettingsPopup getInstance() {
        return instance;
    }

    public static void instantiate() {
        if (instance == null || !instance.popup.isVisible())
            new SettingsPopup();
    }

    public static boolean hasInstance() {
        return instance != null;
    }

    public void applyTheme(Font font, EditorTheme editorTheme) {
        Border border = BorderFactory.createMatteBorder(1, 1, 1, 1, editorTheme.foreground());
        Border buttonBorder = BorderFactory.createMatteBorder(0, 0, 0, 1, editorTheme.foreground());
        grid.setBackground(editorTheme.background());
        fontInput.setCaretColor(editorTheme.foreground());
        themeLabel.setOpaque(true);
        fontSizeLabel.setOpaque(true);
        speedLabel.setOpaque(true);
        tabSizeLabel.setOpaque(true);
        EditorTheme.applyFontAndTheme(speedSlider, font, editorTheme);
        EditorTheme.applyFontAndTheme(themeInput, font, editorTheme);
        EditorTheme.applyFontAndTheme(AutoSaveButton, font, editorTheme);
        EditorTheme.applyFontThemeBorder(fontInput, font, editorTheme, border);
        EditorTheme.applyFontThemeBorder(save, font, editorTheme, buttonBorder);
        EditorTheme.applyFontThemeBorder(resetDefaults, font, editorTheme, buttonBorder);
        EditorTheme.applyFontAndTheme(speedLabel, font, editorTheme);
        EditorTheme.applyFontAndTheme(fontSizeLabel, font, editorTheme);
        EditorTheme.applyFontAndTheme(themeLabel, font, editorTheme);
        EditorTheme.applyFontAndTheme(tabSizeLabel, font, editorTheme);
        EditorTheme.applyFontAndTheme(autoSaveLabel, font, editorTheme);
        EditorTheme.applyFontAndTheme(tabSizeSlider, font, editorTheme);
    }

    private void initialize() {
        buttonActionListener = new ButtonActionListener();
        layout = new BorderLayout();
        popup = new JFrame("EzASM Configurator");
        popup.setLayout(layout);
        popup.setMinimumSize(new Dimension(500, 300));
        popup.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        themeLabel = new JLabel(THEME);
        themeInput = new JComboBox<String>(Config.THEMES);
        themeInput.setSelectedItem(config.getTheme());

        fontSizeLabel = new JLabel(FONTSIZE);
        speedLabel = new JLabel(SIMULATION_SPEED);

        fontInput = new JTextField(String.valueOf(config.getFontSize()));
        speedSlider = new JSlider(10, 1000, config.getSimSpeed());

        tabSizeLabel = new JLabel(TABSIZE);
        tabSizeSlider = new JSlider(1, 8, config.getTabSize());
        tabSizeSlider.setMajorTickSpacing(1);
        tabSizeSlider.setPaintTicks(true);
        tabSizeSlider.setPaintLabels(true);

        autoSaveLabel = new JLabel(AUTOSAVE);
        AutoSaveButton = new SliderToggleButton(instance.config.getAutoSave());

        GridLayout gridLayout = new GridLayout(0, 2);
        gridLayout.setVgap(20);
        grid = new JPanel(gridLayout);
        grid.add(fontSizeLabel);
        grid.add(fontInput);
        grid.add(speedLabel);
        grid.add(speedSlider);
        grid.add(themeLabel);
        grid.add(themeInput);
        grid.add(tabSizeLabel);
        grid.add(tabSizeSlider);
        grid.add(autoSaveLabel);
        grid.add(AutoSaveButton);

        save = new JButton(SAVE);
        resetDefaults = new JButton(RESET);

        grid.add(save);
        grid.add(resetDefaults);

        resetDefaults.addActionListener(buttonActionListener);
        save.addActionListener(buttonActionListener);

        popup.add(grid, BorderLayout.CENTER);

        popup.validate();
        popup.pack();
        popup.setVisible(true);
        this.applyTheme(new Font(Config.DEFAULT_FONT, Font.PLAIN, config.getFontSize()),
                EditorTheme.getTheme(config.getTheme()));
    }

    private static class ButtonActionListener implements ActionListener {

        public ButtonActionListener() {
            super();
        }

        @Override
        public void actionPerformed(ActionEvent e) {
            String action = e.getActionCommand();
            SettingsPopup instance = SettingsPopup.getInstance();
            if (action.startsWith("Save")) {
                try {
                    instance.config.setFontSize(Integer.parseInt(instance.fontInput.getText()));
                } catch (NumberFormatException er) {
                    JOptionPane.showMessageDialog(new JFrame(), "Bad format for font size, please input a number");
                    return;
                }
                if (instance.AutoSaveButton.getSliderValue() == 0) {
                    instance.config.setAutoSaveInterval(1);
                    instance.AutoSaveButton.setToggleButtonStatus(false);
                    instance.config.setAutoSave(instance.AutoSaveButton.getToggleButtonText());
                }
                instance.config.setSimSpeed(instance.speedSlider.getValue());
                instance.config.setTabSize(instance.tabSizeSlider.getValue());
                instance.config.setTheme(instance.themeInput.getSelectedItem().toString());
                instance.config.setAutoSave(instance.AutoSaveButton.getToggleButtonText());
                instance.config.setAutoSaveInterval(instance.AutoSaveButton.getSliderValue());
                instance.config.setAutoSaveSelected(instance.AutoSaveButton.getToggleButtonStatus());
                instance.config.saveChanges();
                instance.applyTheme(new Font(Config.DEFAULT_FONT, Font.PLAIN, instance.config.getFontSize()),
                        EditorTheme.getTheme(instance.config.getTheme()));
                Window.getInstance().applyConfiguration(instance.config);
            }
            if (action.startsWith("Reset")) {
                instance.config.resetDefaults();
                instance.fontInput.setText(Config.DEFAULT_FONT_SIZE);
                instance.speedSlider.setValue(Integer.parseInt(Config.DEFAULT_SIMULATION_SPEED));
                instance.tabSizeSlider.setValue(Integer.parseInt(Config.DEFAULT_TAB_SIZE));
                instance.themeInput.setSelectedIndex(0);
                instance.AutoSaveButton.setToggleButtonText(Config.DEFAULT_AUTO_SAVE);
                instance.AutoSaveButton.setToggleButtonStatus(false);
                instance.AutoSaveButton.setSliderValue(Integer.parseInt(Config.DEFAULT_AUTO_SAVE_INTERVAL));
            }
        }
    }

    public class SliderToggleButton extends JPanel {
        private JToggleButton toggleButton;
        private JSlider slider;

        public SliderToggleButton(String label) {
            setLayout(new BorderLayout());

            toggleButton = new JToggleButton(label);
            toggleButton.setSelected(instance.config.getAutoSaveSelected());
            toggleButton.addChangeListener(new ChangeListener() {
                @Override
                public void stateChanged(ChangeEvent event) {
                    if (AutoSaveButton.getToggleButtonStatus()) {
                        AutoSaveButton.setToggleButtonText("ON");
                        AutoSaveButton.setSliderVisible(true);
                    } else {
                        AutoSaveButton.setToggleButtonText("OFF");
                        AutoSaveButton.setSliderVisible(false);
                    }
                }
            });
            add(toggleButton, BorderLayout.WEST);

            slider = new JSlider(0, 30, instance.config.getAutoSaveInterval());
            slider.setMajorTickSpacing(5);
            slider.setPaintTicks(true);
            slider.setPaintLabels(true);
            slider.setVisible(instance.config.getAutoSaveSelected());
            add(slider, BorderLayout.CENTER);
        }

        public JToggleButton getToggleButton() {
            return toggleButton;
        }

        public boolean getToggleButtonStatus() {
            return toggleButton.isSelected();
        }

        public void setToggleButtonStatus(boolean status) {
            toggleButton.setSelected(status);
        }

        public String getToggleButtonText() {
            return toggleButton.getText();
        }

        public void setToggleButtonText(String text) {
            toggleButton.setText(text);
        }

        public JSlider getSlider() {
            return slider;
        }

        public void setSliderVisible(Boolean visible) {
            slider.setVisible(visible);
        }

        public int getSliderValue() {
            return slider.getValue();
        }

        public void setSliderValue(int value) {
            slider.setValue(value);
        }
    }
}
