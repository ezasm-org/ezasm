package com.ezasm.gui.tabbedpane;

import java.awt.BorderLayout;
import java.awt.Font;

import javax.swing.JLabel;
import javax.swing.JTabbedPane;
import javax.swing.border.EmptyBorder;

import com.ezasm.gui.settings.Config;
import com.ezasm.gui.util.EditorTheme;

/**
 * Builder class for closable tabs in a <code>TabbedPane</code>.
 */
public class ClosableTabBuilder {

    private Font font = new Font(Config.DEFAULT_FONT, Font.PLAIN, 12);
    private EditorTheme editorTheme = EditorTheme.Light;
    private String name = ""; // empty by default
    private ClosableTabbedPane parent;

    /**
     * Constructs a closable tab builder.
     *
     * @param parent the parent element.
     */
    public ClosableTabBuilder(ClosableTabbedPane parent) {
        this.parent = parent;
    }

    /**
     * Builds a closable tab based on the current configuration.
     *
     * @return the generated closable tab.
     */
    public ClosableTabPanel build() {
        JLabel label = new JLabel(name);
        label.setFont(font);
        label.setForeground(editorTheme.foreground());

        TabCloseButton btn = new TabCloseButton(parent, name);
        btn.applyTheme(font, editorTheme);

        return new ClosableTabPanel(new BorderLayout(), btn, label);
    }

    /**
     * Sets the font for the label of the closable tab.
     *
     * @param font the new font.
     */
    public ClosableTabBuilder setFont(Font font) {
        this.font = font;
        return this;
    }

    /**
     * Sets the theme of the closable tab.
     *
     * @param editorTheme the new theme.
     */
    public ClosableTabBuilder setTheme(EditorTheme editorTheme) {
        this.editorTheme = editorTheme;
        return this;
    }

    /**
     * Sets the name of the closable tab.
     *
     * @param name the new name.
     */
    public ClosableTabBuilder setTabName(String name) {
        this.name = name;
        return this;
    }

    /**
     * Sets the parent structure of the closable tab.
     *
     * @param parent the new TabbedPane.
     */
    public ClosableTabBuilder setParent(ClosableTabbedPane parent) {
        this.parent = parent;
        return this;
    }
}
