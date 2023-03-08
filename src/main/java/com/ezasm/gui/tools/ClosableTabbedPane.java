package com.ezasm.gui.tools;

import com.ezasm.gui.util.IThemeable;
import com.ezasm.gui.util.Theme;

import javax.swing.*;
import java.awt.*;

/**
 * <code>TabbedPane</code> extension for closable tabs
 */
public class ClosableTabbedPane extends TabbedPane {

    private final ClosableTabBuilder closeableTabBuilder;

    public ClosableTabbedPane() {
        super();
        closeableTabBuilder = new ClosableTabBuilder();
    }

    /**
     * Applies the given theme and font to the component itself, the tabbed pane, and all subcomponents of the tabbed
     * pane. If the components are IThemable, uses their IThemable#applyTheme method to do so.
     *
     * @param font  the font to apply.
     * @param theme the theme to apply.
     */
    @Override
    public void applyTheme(Font font, Theme theme) {
        super.applyTheme(font, theme);
        closeableTabBuilder.setFont(font).setTheme(theme);

        for (int i = 0; i < tabbedPane.getTabCount(); i++) {
            Component component = tabbedPane.getTabComponentAt(i);
            if (component instanceof IThemeable themeable) {
                themeable.applyTheme(font, theme);
            }
        }
    }

    /**
     * Adds a tab to display the specified component with no mnemonic.
     *
     * @param component the component to be displayed when the tab is selected.
     * @param icon      the icon to be displayed next to the text.
     * @param title     the title of the tab.
     * @param tip       the tooltip displayed when the tab is hovered on.
     */
    @Override
    public void addTab(JComponent component, Icon icon, String title, String tip) {
        super.addTab(component, icon, title, tip);
        closeableTabBuilder.setParent(tabbedPane).setTabName(title);
        tabbedPane.setTabComponentAt(tabbedPane.getTabCount() - 1, closeableTabBuilder.build());
    }
}
