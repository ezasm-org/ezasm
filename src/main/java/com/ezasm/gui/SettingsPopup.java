package com.ezasm.gui;

import java.awt.Dimension;

import javax.swing.*;

import com.ezasm.Config;


public class SettingsPopup {
    private static SettingsPopup instance;

    private static final String FONTSIZE = "Font Size";
    private static final String SIMULATION_SPEED = "Instruction Delay";
    private static final String THEME = "Theme";

    private JFrame popup;
    private JSlider speedSlider;
    private JTextField fontInput;
    private JComboBox themeInput;
    private JButton resetDefaults;
    private JButton save;
    private JLabel speedLabel, fontSizeLabel, themeLabel;
    private GroupLayout layout;

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
        popup = new JFrame("EzASM Configurator");
        popup.setMinimumSize(new Dimension(500,300));
        popup.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        layout = new GroupLayout(popup);
        layout.setAutoCreateGaps(true);
        layout.setAutoCreateContainerGaps(true);

        fontSizeLabel = new JLabel(FONTSIZE);
        speedLabel = new JLabel(SIMULATION_SPEED);

        fontInput = new JTextField(String.valueOf(config.getFontSize()));
        speedSlider = new JSlider(50, 500, config.getSimSpeed());

        layout.setHorizontalGroup(layout.createSequentialGroup()
                .addComponent(fontSizeLabel)
                .addComponent(fontInput)
                );
        // layout.setHorizontalGroup(layout.createSequentialGroup()
        //         .addComponent(speedLabel)
        //         .addComponent(speedSlider)
        //         );
        // layout.setHorizontalGroup(layout.createSequentialGroup()
        //         .addComponent(themeLabel)
        //         .addComponent(themeInput)
        //         );

        popup.validate();
        popup.pack();
        popup.setVisible(true);
    }
}
