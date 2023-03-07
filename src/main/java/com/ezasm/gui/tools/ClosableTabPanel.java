package com.ezasm.gui.tools;

import com.ezasm.gui.util.IThemeable;
import com.ezasm.gui.util.TabCloseButton;
import com.ezasm.gui.util.Theme;

import java.awt.BorderLayout;
import java.awt.Font;

import javax.swing.JLabel;
import javax.swing.JPanel;

public class ClosableTabPanel extends JPanel implements IThemeable {
    private TabCloseButton btn;
    private JLabel label;

    public ClosableTabPanel(BorderLayout layout, TabCloseButton btn, JLabel label) {
        super(layout);
        setOpaque(false);

        add(label, BorderLayout.WEST);
        add(btn, BorderLayout.EAST);
        this.label = label;
        this.btn = btn;
    }

    @Override
    public void applyTheme(Font font, Theme theme) {
        setBackground(theme.background());
        btn.applyTheme(font, theme);
        label.setFont(font);
        label.setForeground(theme.foreground());
    }

}
