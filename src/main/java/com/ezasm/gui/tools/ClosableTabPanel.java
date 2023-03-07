package com.ezasm.gui.tools;

import com.ezasm.gui.util.IThemeable;
import com.ezasm.gui.util.Theme;

import java.awt.BorderLayout;
import java.awt.Font;

import javax.swing.JLabel;
import javax.swing.JPanel;

public class ClosableTabPanel extends JPanel implements IThemeable {

    private final TabCloseButton button;
    private final JLabel label;

    public ClosableTabPanel(BorderLayout layout, TabCloseButton button, JLabel label) {
        super(layout);
        setOpaque(false);

        add(label, BorderLayout.WEST);
        add(button, BorderLayout.EAST);
        this.label = label;
        this.button = button;
    }

    @Override
    public void applyTheme(Font font, Theme theme) {
        setBackground(theme.background());
        button.applyTheme(font, theme);
        label.setFont(font);
        label.setForeground(theme.foreground());
    }

}
