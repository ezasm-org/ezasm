package com.ezasm.gui.settings;

import com.ezasm.gui.util.IThemeable;
import com.ezasm.gui.util.Theme;
import com.ezasm.util.Properties;

import javax.swing.*;
import javax.swing.border.Border;
import java.awt.*;

public class AboutPopup implements IThemeable {

    private static AboutPopup instance;

    private static final String NAME = Properties.NAME;
    private static final String VERSION = String.format("Version: %s", Properties.VERSION);
    private static final String URL = String.format("URL: %s", Properties.URL);

    private static JLabel nameLabel;
    private static JLabel versionLabel;
    private static JLabel urlLabel;
    private static JPanel grid;

    private Config config;

    private JFrame popup;

    protected AboutPopup() {
        instance = this;
        config = new Config();
        initialize();
    }

    public static void instantiate() {
        if (instance == null || !instance.popup.isVisible())
            new AboutPopup();
    }

    public void applyTheme(Font font, Theme theme) {
        Theme.applyFontAndTheme(grid, font, theme);
        Theme.applyFontAndTheme(nameLabel, font, theme);
        Theme.applyFontAndTheme(versionLabel, font, theme);
        Theme.applyFontAndTheme(urlLabel, font, theme);
    }

    private void initialize() {
        BorderLayout layout = new BorderLayout();
        popup = new JFrame(NAME);
        popup.setLayout(layout);
        popup.setMinimumSize(new Dimension(200, 100));
        popup.setResizable(true);
        popup.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        nameLabel = new JLabel(NAME);
        versionLabel = new JLabel(VERSION);
        urlLabel = new JLabel(URL);

        GridLayout gridLayout = new GridLayout(0, 1);
        gridLayout.setVgap(10);
        grid = new JPanel(gridLayout);
        Border padding = BorderFactory.createEmptyBorder(10, 10, 10, 10);
        grid.setBorder(padding);
        grid.add(nameLabel);
        grid.add(versionLabel);
        grid.add(urlLabel);

        popup.add(grid, BorderLayout.CENTER);

        popup.validate();
        popup.pack();
        popup.setVisible(true);
        // This is kind of a hack tbh. Needs something better.
        // Needs a better way to get the config honestly. Same thing happens in the settings popup
        this.applyTheme(new Font(Config.DEFAULT_FONT, Font.PLAIN, config.getFontSize()),
                Theme.getTheme(config.getTheme()));
    }

}
