package com.ezasm.gui;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.*;
import javax.swing.border.Border;

public class SettingsPopup implements IThemeable {
    private static SettingsPopup instance;

    private static final String FONTSIZE = "Font Size";
    private static final String SIMULATION_SPEED = "Instruction Delay";
    private static final String THEME = "Theme";
    public static final String SAVE = "Save Changes";
    public static final String RESET = "Reset to Defaults";

    private JFrame popup;
    private JSlider speedSlider;
    private JTextField fontInput;
    private JComboBox themeInput;
    private JPanel grid;
    private JButton resetDefaults;
    private JButton save;
    private JLabel speedLabel, fontSizeLabel, themeLabel;
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

    public void applyTheme(Font font, Theme theme) {
        Border border = BorderFactory.createMatteBorder(1, 1, 1, 1, theme.foreground());
        Border buttonBorder = BorderFactory.createMatteBorder(0, 0, 0, 1, theme.foreground());
        grid.setBackground(theme.background());
        fontInput.setCaretColor(theme.foreground());
        themeLabel.setOpaque(true);
        fontSizeLabel.setOpaque(true);
        speedLabel.setOpaque(true);
        Theme.applyFontAndTheme(speedSlider, font, theme);
        Theme.applyFontAndTheme(themeInput, font, theme);
        Theme.applyFontThemeBorder(fontInput, font, theme, border);
        Theme.applyFontThemeBorder(save, font, theme, buttonBorder);
        Theme.applyFontThemeBorder(resetDefaults, font, theme, buttonBorder);
        Theme.applyFontAndTheme(speedLabel, font, theme);
        Theme.applyFontAndTheme(fontSizeLabel, font, theme);
        Theme.applyFontAndTheme(themeLabel, font, theme);
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

        GridLayout gridLayout = new GridLayout(0, 2);
        gridLayout.setVgap(20);
        grid = new JPanel(gridLayout);
        grid.add(fontSizeLabel);
        grid.add(fontInput);
        grid.add(speedLabel);
        grid.add(speedSlider);
        grid.add(themeLabel);
        grid.add(themeInput);

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
                Theme.getTheme(config.getTheme()));
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
                instance.config.setSimSpeed(instance.speedSlider.getValue());
                instance.config.setTheme(instance.themeInput.getSelectedItem().toString());
                instance.config.saveChanges();
                instance.applyTheme(new Font(Config.DEFAULT_FONT, Font.PLAIN, instance.config.getFontSize()),
                        Theme.getTheme(instance.config.getTheme()));
                Window.getInstance().applyConfiguration(instance.config);
            }
            if (action.startsWith("Reset")) {
                instance.config.resetDefaults();
                instance.fontInput.setText(Config.DEFAULT_FONT_SIZE);
                instance.speedSlider.setValue(Integer.parseInt(Config.DEFAULT_SIMULATION_SPEED));
                instance.themeInput.setSelectedIndex(0);
            }
        }

    }
}