package com.ezasm.gui.tools;

import java.awt.BorderLayout;
import java.awt.Font;

import javax.swing.JLabel;
import javax.swing.JTabbedPane;

import com.ezasm.gui.util.TabCloseButton;
import com.ezasm.gui.util.Theme;

public class ClosableTabBuilder {
    private Font font = new Font("mono", Font.PLAIN, 12);
    private Theme theme = Theme.getTheme("Dark");
    private String name = ""; // empty by default
    private JTabbedPane parent = new JTabbedPane();

    public ClosableTabPanel build() {
        JLabel label = new JLabel(name);
        label.setFont(font);
        label.setForeground(theme.foreground());

        TabCloseButton btn = new TabCloseButton(parent, name);
        btn.applyTheme(font, theme);

        return new ClosableTabPanel(new BorderLayout(), btn, label);
    }

    public ClosableTabBuilder setFont(Font font) {
        this.font = font;
        return this;
    }

    public ClosableTabBuilder setTheme(Theme theme) {
        this.theme = theme;
        return this;
    }

    public ClosableTabBuilder setTabName(String name) {
        this.name = name;
        return this;
    }

    public ClosableTabBuilder setParent(JTabbedPane parent) {
        this.parent = parent;
        return this;
    }
}
