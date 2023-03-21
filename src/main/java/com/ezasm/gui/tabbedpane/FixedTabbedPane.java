package com.ezasm.gui.tabbedpane;

import java.awt.*;

import javax.swing.*;

import com.ezasm.gui.Window;
import com.ezasm.gui.ui.EzTabbedPaneUI;
import com.ezasm.gui.util.IThemeable;
import com.ezasm.gui.util.EditorTheme;

/**
 * Represents a tabbed pane on which the tabs are not closable by default. This class is to be extended by a class which
 * implements closable tabs.
 */
public class FixedTabbedPane extends JPanel implements IThemeable {

    protected final JTabbedPane tabbedPane;

    /**
     * Constructs an empty tabbed pane.
     */
    public FixedTabbedPane() {
        super(new BorderLayout());

        tabbedPane = new JTabbedPane();
        tabbedPane.setUI(new EzTabbedPaneUI(Window.getInstance().getTheme()));
        tabbedPane.setFocusable(false);

        add(tabbedPane);
    }

    /**
     * Applies the given theme and font to the component itself, the tabbed pane, and all subcomponents of the tabbed
     * pane. If the components are IThemable, uses their IThemable#applyTheme method to do so.
     *
     * @param font        the font to apply.
     * @param editorTheme the theme to apply.
     */
    public void applyTheme(Font font, EditorTheme editorTheme) {
        setBackground(editorTheme.currentLine());
        setForeground(editorTheme.foreground());
        setFont(font);
        tabbedPane.setUI(new EzTabbedPaneUI(editorTheme));
        tabbedPane.setFont(font);

        for (Component component : tabbedPane.getComponents()) {
            if (component instanceof IThemeable themeable) {
                themeable.applyTheme(font, editorTheme);
            } else {
                component.setBackground(editorTheme.background());
                component.setForeground(editorTheme.foreground());
                component.setFont(font);
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
    public void addTab(JComponent component, Icon icon, String title, String tip) {
        addTab(component, icon, title, tip, -1);
    }

    /**
     * Adds a tab to display the specified component with a mnemonic.
     *
     * @param component the component to be displayed when the tab is selected.
     * @param icon      the icon to be displayed next to the text.
     * @param title     the title of the tab.
     * @param tip       the tooltip displayed when the tab is hovered on.
     */
    public void addTab(JComponent component, Icon icon, String title, String tip, int mnemonic) {
        tabbedPane.addTab(title, icon, component, tip);
        if (mnemonic != -1) {
            tabbedPane.setMnemonicAt(tabbedPane.getTabCount() - 1, mnemonic);
        }
    }

    /**
     * Removes the component at a given index.
     *
     * @param index the index of the component to remove.
     */
    public void removeTab(int index) {
        tabbedPane.removeTabAt(index);
    }

    /**
     * Removes the given component if it is in the data structure.
     *
     * @param component the component to remove.
     */
    public void removeTab(JComponent component) {
        for (int i = 0; i < tabbedPane.getTabCount(); ++i) {
            if (tabbedPane.getTabComponentAt(i) == component) {
                removeTab(i);
                return;
            }
        }
    }

    /**
     * Gets the index of a tab given the title.
     *
     * @param title the title to seek for.
     * @return the index of a tab given the title.
     */
    public int indexOfTab(String title) {
        return tabbedPane.indexOfTab(title);
    }

    public int indexOfComponent(Component c) {
        return tabbedPane.indexOfComponent(c);
    }

    /**
     * Returns the number of tabs in this object.
     *
     * @return the number of tabs in this object.
     */
    public int getTabCount() {
        return tabbedPane.getTabCount();
    }

    /**
     * Gets the component at the given index.
     *
     * @param index the index to seek a component at.
     * @return the component at the given index.
     */
    public JComponent getComponentAt(int index) {
        return (JComponent) tabbedPane.getTabComponentAt(index);
    }

    /**
     * Gets the currently selected component.
     *
     * @return the currently selected component.
     */
    public JComponent getSelectedComponent() {
        return (JComponent) tabbedPane.getSelectedComponent();
    }

    public void setActiveTab(JComponent comp) {
        tabbedPane.setSelectedIndex(tabbedPane.indexOfComponent(comp));
    }

    public void setActiveTab(int idx) {
        tabbedPane.setSelectedIndex(idx);
    }
}
