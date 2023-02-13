package com.ezasm.gui;

import javax.swing.BorderFactory;
import javax.swing.JComponent;
import javax.swing.JScrollBar;
import javax.swing.border.Border;

import java.awt.Color;
import java.awt.Font;

/**
 * Represents a theme for components and text in the application.
 */
public record Theme(Color background, Color foreground, Color currentLine, Color selection, Color comment, Color cyan,
        Color green, Color orange, Color pink, Color purple, Color red, Color yellow, boolean isLight) {

    // based on https://github.com/dracula/dracula-theme
    public static Theme Dracula = new Theme(new Color(0x282a36), // background
            new Color(0xf8f8f2), // foreground
            new Color(0x44475a), // currentLine
            new Color(0x44475a), // selection
            new Color(0x6272a4), // comment
            new Color(0x8be9fd), // cyan
            new Color(0x50fa7b), // green
            new Color(0xffb86c), // orange
            new Color(0xff79c6), // pink
            new Color(0xbd93f9), // purple
            new Color(0xff5555), // red
            new Color(0xf1fa8c), // yellow
            false); // is a light theme

    // based on https://github.com/endormi/vscode-2077-theme
    public static Theme Purple = new Theme(new Color(0x030d22), // background
            new Color(0xfdfeff), // foreground
            new Color(0x310072), // currentLine
            new Color(0x35008b), // selection
            new Color(0x6272a4), // comment
            new Color(0x0ab2fa), // cyan
            new Color(0x06ad00), // green
            new Color(0xffd400), // orange
            new Color(0xea00d9), // pink
            new Color(0x6f46af), // purple
            new Color(0xd92020), // red
            new Color(0xffff99), // yellow
            false); // is a light theme

    // based on https://github.com/atom/one-light-syntax
    public static Theme Light = new Theme(new Color(0xebf8ff), // background
            new Color(0x161b1d), // foreground
            new Color(0xc1e4f6), // currentLine
            new Color(0x7ea2b4), // selection
            new Color(0x383a42), // comment
            new Color(0x004E9D), // cyan
            new Color(0x28a626), // green
            new Color(0xB28822), // orange
            new Color(0x8c329a), // pink
            new Color(0xd548d3), // purple
            new Color(0xd92020), // red
            new Color(0xfffb0f), // yellow
            true); // is a light theme

    /**
     * Takes a string theme name and returns the corresponding theme object
     *
     * @param s the theme in plain text
     * @return the theme object
     */
    public static Theme getTheme(String s) {
        return switch (s) {
        case "Dark" -> Theme.Dracula;
        case "Purple" -> Theme.Purple;
        case "Light" -> Theme.Light;
        default -> Theme.Light;
        };
    }

    public Color modifyAwayFromBackground(Color c) {
        return isLight ? c.darker() : c.brighter();
    }

    public Color modifyAwayFromBackground(Color c, int times) {
        for (int i = 0; i < times; ++i) {
            c = modifyAwayFromBackground(c);
        }
        return c;
    }

    public Color modifyTowardsBackground(Color c) {
        return isLight ? c.brighter() : c.darker();
    }

    public Color modifyTowardsBackground(Color c, int times) {
        for (int i = 0; i < times; ++i) {
            c = modifyAwayFromBackground(c);
        }
        return c;
    }

    public void applyTheme(JComponent component) {
        component.setBackground(background);
        component.setForeground(foreground);
        component.setBorder(BorderFactory.createEmptyBorder());
    }

    public void applyThemeScrollbar(JScrollBar scrollbar) {
        applyTheme(scrollbar);
        scrollbar.setUI(new EzASMScrollBarUI(this));
    }

    public static void applyFontAndTheme(JComponent component, Font font, Theme theme) {
        theme.applyTheme(component);
        component.setFont(font);
    }

    public static void applyFontThemeBorder(JComponent component, Font font, Theme theme, Border border) {
        theme.applyTheme(component);
        component.setBorder(border);
        component.setFont(font);
    }
}
