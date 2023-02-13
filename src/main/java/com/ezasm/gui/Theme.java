package com.ezasm.gui;

import javax.swing.*;
import javax.swing.border.Border;
import java.awt.*;

/**
 * Represents a theme for components and text in the application.
 */
public class Theme {

    private final Color BACKGROUND;
    private final Color FOREGROUND;
    private final Color CURRENT_LINE;
    private final Color COMMENT;
    private final Color CYAN;
    private final Color GREEN;
    private final Color ORANGE;
    private final Color PINK;
    private final Color PURPLE;
    private final Color RED;
    private final Color YELLOW;
    private final boolean isLight;

    // based on https://github.com/dracula/dracula-theme
    public static Theme Dracula = new Theme(0x282a36, 0x44475a, 0xf8f8f2, 0x6272a4, 0x8be9fd, 0x50fa7b, 0xffb86c,
            0xff79c6, 0xbd93f9, 0xff5555, 0xf1fa8c, false);
    // based on https://github.com/endormi/vscode-2077-theme
    public static Theme Purple = new Theme(0x0d0936, 0x210b66, 0xe4eeff, 0x0098df, 0x0ab2fa, 0x06ad00, 0xffd400,
            0xea00d9, 0x6f46af, 0xee1682, 0xffff99, false);
    // based on https://github.com/atom/one-light-syntax
    public static Theme Light = new Theme(0xfafafa, 0xb0b1b7, 0x383a42, 0x404045, 0x0184bc, 0x28a626, 0x986801,
            0xd548d3, 0x8c329a, 0xd92020, 0xfffb0f, true);

    public Theme(int bg, int cl, int fg, int cmt, int cyan, int grn, int org, int pnk, int prp, int red, int ylw,
            boolean isLight) {
        BACKGROUND = new Color(bg);
        FOREGROUND = new Color(fg);
        CURRENT_LINE = new Color(cl);
        COMMENT = new Color(cmt);
        CYAN = new Color(cyan);
        GREEN = new Color(grn);
        ORANGE = new Color(org);
        PINK = new Color(pnk);
        PURPLE = new Color(prp);
        RED = new Color(red);
        YELLOW = new Color(ylw);
        this.isLight = isLight;
    }

    public Color getBackground() {
        return BACKGROUND;
    }

    public Color getForeground() {
        return FOREGROUND;
    }

    public Color getCurrentLine() {
        return CURRENT_LINE;
    }

    public Color getComment() {
        return COMMENT;
    }

    public Color getCyan() {
        return CYAN;
    }

    public Color getGreen() {
        return GREEN;
    }

    public Color getOrange() {
        return ORANGE;
    }

    public Color getPink() {
        return PINK;
    }

    public Color getPurple() {
        return PURPLE;
    }

    public Color getRed() {
        return RED;
    }

    public Color getYellow() {
        return YELLOW;
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
        component.setBackground(BACKGROUND);
        component.setForeground(FOREGROUND);
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
