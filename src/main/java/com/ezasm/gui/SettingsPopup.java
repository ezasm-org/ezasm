package com.ezasm.gui;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.GridLayout;

import javax.swing.*;
import javax.swing.border.Border;

import com.ezasm.Config;


public class SettingsPopup {
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
    private JButton resetDefaults;
    private JButton save;
    private JLabel speedLabel, fontSizeLabel, themeLabel;
    private BorderLayout layout;

    private Config config;

    protected SettingsPopup() {
        instance = this;
        config = new Config();
        initialize();
    }

    public static SettingsPopup getInstance(){
        return instance;
    }

    public static void instantiate(){
        if(instance == null) new SettingsPopup();
    }

    public static boolean hasInstance(){
        return instance != null;
    }

    private void initialize(){
        layout = new BorderLayout();
        popup = new JFrame("EzASM Configurator");
        popup.setLayout(layout);
        popup.setMinimumSize(new Dimension(500,300));
        popup.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        fontSizeLabel = new JLabel(FONTSIZE);
        speedLabel = new JLabel(SIMULATION_SPEED);

        fontInput = new JTextField(String.valueOf(config.getFontSize()));
        speedSlider = new JSlider(50, 500, config.getSimSpeed());

        GridLayout gridLayout = new GridLayout(0, 2);
        gridLayout.setVgap(20);
        JPanel grid = new JPanel(gridLayout);
        grid.add(fontSizeLabel);
        grid.add(fontInput);
        grid.add(speedLabel);
        grid.add(speedSlider);
        // grid.add(themeLabel);
        // grid.add(themeInput);
        
        save = new JButton(SAVE);
        resetDefaults = new JButton(RESET);

        grid.add(save);
        grid.add(resetDefaults);

        popup.add(grid, BorderLayout.CENTER);

        popup.validate();
        popup.pack();
        popup.setVisible(true);
    }
}
