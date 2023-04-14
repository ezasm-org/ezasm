package com.ezasm.gui.tabbedpane;

import com.ezasm.gui.util.IThemeable;
import com.ezasm.gui.util.EditorTheme;

import javax.swing.*;

import java.awt.*;

/**
 * Represents a tabbed pane on which the tabs have a button which allows the user to close an individual tab.
 */
public class ClosableTabbedPane extends FixedTabbedPane {

    private final ClosableTabBuilder closeableTabBuilder;

    /**
     * Constructs a tabbed pane where the tabs on it can be closed by the user by pressing a button.
     */
    public ClosableTabbedPane() {
        super();
        closeableTabBuilder = new ClosableTabBuilder(this);
    }

    /**
     * Applies the given theme and font to the component itself, the tabbed pane, and all subcomponents of the tabbed
     * pane. If the components are IThemable, uses their IThemable#applyTheme method to do so.
     *
     * @param font        the font to apply.
     * @param editorTheme the theme to apply.
     */
    @Override
    public void applyTheme(Font font, EditorTheme editorTheme) {
        super.applyTheme(font, editorTheme);
        closeableTabBuilder.setFont(font).setTheme(editorTheme);

        for (int i = 0; i < tabbedPane.getTabCount(); i++) {
            Component component = tabbedPane.getTabComponentAt(i);
            if (component instanceof IThemeable themeable) {
                themeable.applyTheme(font, editorTheme);
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
        closeableTabBuilder.setParent(this).setTabName(title);
        tabbedPane.setTabComponentAt(tabbedPane.getTabCount() - 1, closeableTabBuilder.build());
    }

    @Override
    public void removeTab(int index) {
        if (index < 0 || index > tabbedPane.getComponents().length) {
            return;
        }
        boolean close = true;
        if (getComponentAt(index) instanceof JClosableComponent closable) {
            close = closable.close();
        }
        if (close) {
            super.removeTab(index);
        }
    }
}
